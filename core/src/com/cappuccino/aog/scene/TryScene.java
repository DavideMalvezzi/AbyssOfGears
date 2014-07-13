package com.cappuccino.aog.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.BatchUtils;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.Thunder.Segment;


public class TryScene extends Scene{

	
	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private static final Pool<Segment> segmentPool = new Pool<Segment>(){
			protected Segment newObject() {
				return new Segment();
			}
	};
	
	
	public FrameBuffer light_buf, bloom_buf,temp_buf;
	private final Array<Segment> thunderPoint = new Array<Segment>();
	
	
	private static final ShaderProgram sp = new ShaderProgram(Gdx.files.internal("data/bloom_shader.vert"), Gdx.files.internal("data/gaussian_blur.frag"));
	
	public TryScene() {
		camera.position.y = camera.viewportHeight/2;
		
		light_buf = new FrameBuffer(Format.RGBA8888, 300, 200, false);
		temp_buf = new FrameBuffer(Format.RGBA8888, 300, 200, false);
		bloom_buf = new FrameBuffer(Format.RGBA8888, 300, 200, false);
		
		createNew();
		drawOnBuffer(batch);
		//System.out.println(sp.getLog());
	}
	boolean draw = false;
	
	@Override
	public void render(float delta) {
	//	beginClip();
		 
			batch.setProjectionMatrix(camera.combined.scl(BOX_TO_WORLD));
			batch.begin();
			batch.draw(Assets.layer0Background, 0, 0);
			
			//batch.draw(light_buf.getColorBufferTexture(), 0, 0);
			if(!draw){
			
			Matrix4 old = batch.getProjectionMatrix().cpy();
			
			
			Texture light = light_buf.getColorBufferTexture();
			batch.flush();
				
			temp_buf.begin();
				batch.setColor(144f/255f, 208f/255f, 248f/255f, 1);
				batch.setProjectionMatrix(getBufferProj(temp_buf));
				batch.setShader(sp);
				sp.setUniformf("pixelSize", new Vector2(1.0f/light_buf.getWidth(), 0));
				sp.setUniform1fv("values", BatchUtils.gaussian_values, 0, 9);
				sp.setUniformf("gloomFactor", 1.65f);
				batch.draw(light, 0, 0);
				batch.flush();
			temp_buf.end();
				
			bloom_buf.begin();
				batch.setProjectionMatrix(getBufferProj(bloom_buf));
				sp.setUniformf("pixelSize", new Vector2(0, 1.0f/light_buf.getHeight()));
				sp.setUniform1fv("values", BatchUtils.gaussian_values, 0, 9);
				sp.setUniformf("gloomFactor", 1.65f);
				batch.draw(temp_buf.getColorBufferTexture(), 0, 0);
				batch.flush();
			bloom_buf.end();
			
			batch.setProjectionMatrix(old);
			batch.setColor(Color.WHITE);
			batch.setShader(null);
			
			draw = true;	
			}
			
			Texture t = bloom_buf.getColorBufferTexture();
			Pixmap p = new Pixmap(t.getWidth(), t.getHeight(), Format.RGBA8888);
			Texture t2 = new Texture(p);
			
			batch.draw(t2, 0, 0);
			
			
			batch.end();
			
	//	endClip();
	}
	
	
public void drawOnBuffer(SpriteBatch batch){
		
		light_buf.begin();
			Gdx.gl20.glClearColor(0, 0, 0, 0f);
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl20.glClearColor(1, 0, 0, 1f);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.setProjectionMatrix(getBufferProj(light_buf));
			shapeRenderer.begin(ShapeType.Filled);
				for(int i=0; i<thunderPoint.size; i++){
					Segment s = thunderPoint.get(i);
					shapeRenderer.rectLine(s.start, s.end, 2);
				}
		
			shapeRenderer.end();
			
		light_buf.end();
		
		
		

		
		
	}
	
	
	public void createNew(){
		float MAX_OFFSET = 100;
		float dst, angle, off;
		Vector2 midPoint = new Vector2();
		Vector2 branch = new Vector2();
		
		segmentPool.freeAll(thunderPoint);
		thunderPoint.clear();
		thunderPoint.add(segmentPool.obtain().set(new Vector2(0, MAX_OFFSET), new Vector2(300, MAX_OFFSET)));
		for(int i=0; i<0; i++){
			for(int j=0; j<thunderPoint.size; j++){
				Segment s = thunderPoint.get(j);
				
				midPoint.x = (s.start.x+s.end.x)/2;
				midPoint.y = (s.start.y+s.end.y)/2;
				dst = midPoint.dst(s.start);
				off = MathUtils.random(-MAX_OFFSET, MAX_OFFSET);
				angle = MathUtils.atan2(off, dst);
				
				midPoint.sub(s.start).rotateRad(angle).add(s.start);
				
				thunderPoint.removeIndex(j);
				thunderPoint.insert(j, segmentPool.obtain().set(s.start, midPoint));
				thunderPoint.insert(j+1,segmentPool.obtain().set(midPoint, s.end));
				segmentPool.free(s);
				
				if(MathUtils.random(-1, 1)==0){
					branch.set(s.end);
					angle = (MathUtils.random(-35, 35)+15)*MathUtils.degRad;
					branch.sub(midPoint).rotateRad(angle).add(midPoint);
					
					thunderPoint.insert(j+2, segmentPool.obtain().set(midPoint, branch));
					j++;
				}
				
				j++;
				
			}
			MAX_OFFSET*=0.5f;
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Matrix4 getBufferProj(FrameBuffer f){
		return new Matrix4().setToOrtho(0, f.getWidth(), 0, f.getHeight(), 0, 10);
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
