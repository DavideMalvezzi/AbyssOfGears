package com.cappuccino.aog.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;


public class TryScene extends Scene{

	
	public ShaderProgram normal, bloomExtract, gaussianBlur, bloomCombine;
	public FrameBuffer sceneRenderTarget, renderTarget1, renderTarget2;
	public Matrix4 projection;
	
	float[] weights;
	float[] offset;
	
	public TryScene() {
		camera.position.y = camera.viewportHeight/2;
		
		sceneRenderTarget = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, false);
		renderTarget1 = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, false);
		renderTarget2 = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, false);
		
		normal = SpriteBatch.createDefaultShader();
		bloomExtract = new ShaderProgram(Gdx.files.internal("data/default.vert"), Gdx.files.internal("data/bloomExtract.frag"));
		gaussianBlur = new ShaderProgram(Gdx.files.internal("data/default.vert"), Gdx.files.internal("data/gaussianBlur.frag"));
		bloomCombine = new ShaderProgram(Gdx.files.internal("data/default.vert"), Gdx.files.internal("data/bloomCombine.frag"));
		
		projection = new Matrix4().setToOrtho(0, Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getHeight()/2, 0, 10);
		
		System.out.println(normal.getLog());
		System.out.println(bloomExtract.getLog());
		System.out.println(gaussianBlur.getLog());
		System.out.println(bloomCombine.getLog());
	}
	
	
	
	@Override
	public void render(float delta) {
		Matrix4 temp = batch.getProjectionMatrix().cpy();
		TextureRegion t = Assets.getTexture("Gear6");
		batch.setShader(null);
		batch.begin();
			batch.draw(Assets.layer0Background, 0, 0);
			batch.flush();
			
			batch.setProjectionMatrix(projection);
			
			Gdx.gl20.glClearColor(0, 0, 0, 0);
			sceneRenderTarget.begin();
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.draw(t, 0, 0);
				batch.flush();
			sceneRenderTarget.end();
			
			renderTarget1.begin();
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.setShader(bloomExtract);
				bloomExtract.setUniformf("BloomThreshold", 0.25f);
				batch.draw(sceneRenderTarget.getColorBufferTexture(), 0, 0);
				batch.flush();
			renderTarget1.end();
			
			renderTarget2.begin();
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.setShader(gaussianBlur);
				setBlurParam(t, 15, 1, 0);
				batch.draw(renderTarget1.getColorBufferTexture(), 0, 0);
				batch.flush();
			renderTarget2.end();
			
			renderTarget1.begin();
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
				setBlurParam(t, 15, 0, 1);
				batch.draw(renderTarget2.getColorBufferTexture(), 0, 0);
				batch.flush();
			renderTarget1.end();
			Gdx.gl20.glClearColor(1, 0, 0, 1);
			
			batch.setProjectionMatrix(temp);
			batch.setShader(bloomCombine);
			batch.setColor(Color.YELLOW);
			setBloomParams(sceneRenderTarget.getColorBufferTexture(), 1, 1, 2, 1.5f);
			batch.draw(renderTarget1.getColorBufferTexture(), 0, 0);
			batch.setColor(Color.WHITE);
		
		batch.end();
		
	}
	
	private void setBloomParams(Texture t, float baseIntensity, float baseSaturation, float bloomIntensity, float bloomSaturation){
		t.bind(1);
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
		
		bloomCombine.setUniformf("BaseIntensity", baseIntensity);
		bloomCombine.setUniformf("BaseSaturation", baseSaturation);
		bloomCombine.setUniformf("BloomIntensity", bloomIntensity);
		bloomCombine.setUniformf("BloomSaturation", bloomSaturation);
	}
	
	private void setBlurParam(TextureRegion t, int radius, int dirX, int dirY){
		float sum = 0;
		
		weights = new float[radius];
		offset = new float[radius*2];
		
		sum = weights[0] = computeGaussian(0, radius);
		offset[0] = 0;
		offset[1] = 0;
		
		
		for(int i=0; i<radius/2; i++){
			float w = computeGaussian(i+1, radius);
			weights[i*2+1] = w;
			weights[i*2+2] = w;
		
			sum+=w*2;
			
			Vector2 delta = new Vector2(1f/t.getRegionWidth(), 1f/t.getRegionHeight()).scl(i+1).scl(dirX, dirY);
			
			offset[i*2+1] = delta.x;
			offset[i*2+2] = delta.y;
			
			offset[i*2+3] = -delta.x;
			offset[i*2+4] = -delta.y;
		}
		
		for(int i=0; i<radius; i++){
			weights[i]/=sum;
		}
		
		gaussianBlur.setUniformi("samples_count", radius);
		gaussianBlur.setUniform1fv("weight", weights, 0, radius);
		gaussianBlur.setUniform2fv("offset", offset, 0, radius*2);
		
	}
	
	
	private float computeGaussian(float n, float theta){
        return (float)((1.0 / Math.sqrt(2 * Math.PI * theta)) * Math.exp(-(n * n) / (2 * theta * theta)));
    }
	
	
	@Override
	public void update(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		

		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
		
	}
	
	
	
	
	
}
