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
	
	public TryScene() {
		camera.position.y = camera.viewportHeight/2;
		
		sceneRenderTarget = new FrameBuffer(Format.RGBA8888, SCENE_W/2, SCENE_H/2, false);
		renderTarget1 = new FrameBuffer(Format.RGBA8888, SCENE_W/2, SCENE_H/2, false);
		renderTarget2 = new FrameBuffer(Format.RGBA8888, SCENE_W/2, SCENE_H/2, false);
		
		normal = SpriteBatch.createDefaultShader();
		bloomExtract = new ShaderProgram(Gdx.files.internal("data/shader/default.vert"), Gdx.files.internal("data/bloomExtract.frag"));
		gaussianBlur = new ShaderProgram(Gdx.files.internal("data/shader/default.vert"), Gdx.files.internal("data/gaussianBlur.frag"));
		bloomCombine = new ShaderProgram(Gdx.files.internal("data/shader/default.vert"), Gdx.files.internal("data/bloomCombine.frag"));
		
		projection = new Matrix4().setToOrtho(0, SCENE_W/2, 0, SCENE_H/2, 0, 10);
		
		System.out.println(normal.getLog());
		System.out.println(bloomExtract.getLog());
		System.out.println(gaussianBlur.getLog());
		System.out.println(bloomCombine.getLog());
		
	}
	
	
	
	@Override
	public void render(float delta) {
		beginClip();
		batch.setProjectionMatrix(camera.combined);
		Matrix4 temp = batch.getProjectionMatrix().cpy();
		TextureRegion t = Assets.getTexture("Gear6");
		
		batch.setShader(null);
		batch.begin();
			
			batch.setProjectionMatrix(projection);
			
			Gdx.gl20.glClearColor(0, 0, 0, 0);
			sceneRenderTarget.begin();
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.draw(t, 0, 0);
				batch.flush();
			sceneRenderTarget.end();
			
			batch.setColor(Color.YELLOW);
			renderTarget2.begin();
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.setShader(gaussianBlur);
				setBlurParam(sceneRenderTarget.getColorBufferTexture(), 9, 1, 0);
				batch.draw(sceneRenderTarget.getColorBufferTexture(), 0, 0);
				batch.flush();
			renderTarget2.end();
			
			renderTarget1.begin();
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.setShader(gaussianBlur);
				setBlurParam(renderTarget2.getColorBufferTexture(), 9, 0, 1);
				batch.draw(renderTarget2.getColorBufferTexture(), 0, 0);
				batch.flush();
			renderTarget1.end();
			batch.setColor(Color.WHITE);
			
			batch.setProjectionMatrix(temp);
			batch.setShader(null);
			batch.setShader(bloomCombine);
			setBloomParams(sceneRenderTarget.getColorBufferTexture(), 1, 1, 20, 1f);
			
			batch.draw(renderTarget1.getColorBufferTexture(), 0, 0);
			
		batch.end();
		endClip();
	}
	
	
	
	private void setBloomParams(Texture t, float baseIntensity, float baseSaturation, float bloomIntensity, float bloomSaturation){
		t.bind(1);
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
		
		bloomCombine.setUniformf("BaseIntensity", baseIntensity);
		bloomCombine.setUniformf("BaseSaturation", baseSaturation);
		bloomCombine.setUniformf("BloomIntensity", bloomIntensity);
		bloomCombine.setUniformf("BloomSaturation", bloomSaturation);
	}
	
	private void setBlurParam(Texture t, int radius, int dirX, int dirY){
		float sum = 0;
		
		weights = new float[radius/2+1];
		
		sum = weights[0] = computeGaussian(0, radius/3);
		
		
		for(int i=1; i<radius/2; i++){
			float w = computeGaussian(i, radius/3);
			weights[i] = w;
		
			sum+=w*2;
		}
		
		for(int i=0; i<radius/2; i++){
			weights[i]/=sum;
		}
		
		gaussianBlur.setUniformi("samples_count", radius);
		gaussianBlur.setUniform1fv("weight", weights, 0, radius/2);
		gaussianBlur.setUniformf("pixelSize", new Vector2(1f/t.getWidth()*dirX, 1f/t.getHeight()*dirY));
		
	}
	
	
	private float computeGaussian(float n, float theta){
        return (float)((1.0 / Math.sqrt(2 * Math.PI * theta)) * Math.exp(-(n * n) / (2 * theta * theta)));
    }
	
	
	@Override
	public void update(float delta) {
		camera.update();
		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
		
	}
	
	
	
	
	
}
