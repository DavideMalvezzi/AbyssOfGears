package com.cappuccino.aog.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;


public class TryScene extends Scene{

	private FrameBuffer light_buffer;
	private FrameBuffer blur_buffer;
	private ShapeRenderer shape;
	ShaderProgram sp;
	private float timer;

	Array<Segment> light;
	final Pool<Segment> segmentPool = new Pool<TryScene.Segment>(){
			protected Segment newObject() {
				return new Segment();
			};
	};
	
	
	public TryScene() {
		light_buffer = new FrameBuffer(Format.RGBA8888, 960, 720, false);
		blur_buffer = new FrameBuffer(Format.RGBA8888, 960, 720, false);
		shape = new ShapeRenderer();
		sp = new ShaderProgram(Gdx.files.internal("data/shader.vert"), Gdx.files.internal("data/shader.frag"));
		System.out.println(sp.getLog());
		light = new Array<Segment>();
		
		
		
	}
	
	@Override
	public void render(float delta) {
		//beginClip();

		camera.combined.scl(BOX_TO_WORLD);
		
		light_buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			//batch.draw(Assets.layer0Background, 0, 0);
			batch.end();
		
		
			shape.setProjectionMatrix(camera.combined);
			shape.begin(ShapeType.Filled);
			shape.setColor(Color.YELLOW);
		
				for(int i=0; i<light.size; i++){
						Segment s = light.get(i);
						shape.rectLine(s.start, s.end, 2);
				}
		
			shape.end();
			
		light_buffer.end();
		
		
		//Applico blur verticale
		blur_buffer.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			batch.begin();
			batch.setShader(sp);
			
					batch.setShader(sp);
					
					Assets.layer0Background.bind(1);
					sp.setUniformi("u_bg", 1);
					Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
					
					sp.setUniformi("isVertical", 1);
					batch.draw(light_buffer.getColorBufferTexture(), -160, 0);
		
			batch.end();
		blur_buffer.end();
		
		
		
		batch.begin();
		
			batch.setShader(null);
			batch.setColor(Color.WHITE);
		//	batch.draw(Assets.layer0Background, 0, 0);
		
			batch.setShader(sp);
			sp.setUniformi("isVertical", -1);
			Assets.layer0Background.bind(1);
			sp.setUniformi("u_bg", 1);
			Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
			
			batch.setColor(Color.WHITE);
			batch.draw(blur_buffer.getColorBufferTexture(), -160, 0);
			
		batch.end();
		
	
		shape.setProjectionMatrix(camera.combined);
		shape.begin(ShapeType.Filled);
		shape.setColor(Color.WHITE);
	
			for(int i=0; i<light.size; i++){
					Segment s = light.get(i);
					shape.rectLine(s.start, s.end, 2);
			}
	
		shape.end();
		
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
			
			
		//endClip();
	}
	
	
	@Override
	public void update(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		timer+=delta;
		
		if(timer>=0.1f){
			float offset = 200;
			float len,angle,off;
			Vector2 midPoint = new Vector2();
			Vector2 branch = new Vector2();
			
			segmentPool.freeAll(light);
			light.clear();
			light.add(segmentPool.obtain().set(0, 0, 640, 300));
			for(int i=0; i<7; i++){
				for(int j=0; j<light.size; j++){
					Segment s = light.get(j);
					
					midPoint.x = (s.start.x+s.end.x)/2;
					midPoint.y = (s.start.y+s.end.y)/2;
					len = midPoint.dst(s.start);
					off = MathUtils.random(-offset, offset);
					angle = MathUtils.atan2(off, len);
					
					midPoint.sub(s.start).rotateRad(angle).add(s.start);
					
					light.removeIndex(j);
					light.insert(j, segmentPool.obtain().set(s.start, midPoint));
					light.insert(j+1,segmentPool.obtain().set(midPoint, s.end));
					segmentPool.free(s);
					
					if(MathUtils.random(-2, 2)==0){
						branch.set(s.end);
						angle = (MathUtils.random(-35, 35)+15)*MathUtils.degRad;
						branch.sub(midPoint).rotateRad(angle).add(midPoint);
						
						light.insert(j+2, segmentPool.obtain().set(midPoint, branch));
						j++;
					}
					
					j++;
					
				}
				offset*=0.5f;
				
			}
			timer = 0;
		}
		
	}
	
	
	static class Segment implements Poolable{
		public final Vector2 start = new Vector2();
		public final Vector2 end =  new Vector2();
		public Segment set(float sx, float sy, float ex, float ey) {
			this.start.x = sx;
			this.start.y = sy;
			this.end.x = ex;
			this.end.y = ey;
			return this;
		}
		
		public Segment set(Vector2 start, Vector2 end) {
			this.start.set(start);
			this.end.set(end);
			return this;
		}
		public void reset(){
			
		}
	}
	
	
}
