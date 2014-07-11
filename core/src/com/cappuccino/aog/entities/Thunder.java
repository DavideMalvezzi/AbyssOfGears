package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.cappuccino.aog.Scene;

public class Thunder extends Entity {

	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private static final Pool<Segment> segmentPool = new Pool<Segment>(){
			protected Segment newObject() {
				return new Segment();
			};
	};
	
	private final Array<Segment> thunderPoint = new Array<Segment>();;
	private FrameBuffer buffer;
	
	private float alpha = 0, len = 0;
	private boolean isTextureReady = false;
	
	
	public Thunder(World world, float x, float y, float len, float angle, float delay, OrthographicCamera camera) {
		super("LaserEmitter", world);
		init(world, BodyType.KinematicBody);
		
		this.len = len;
		this.alpha = delay;
		this.buffer = new FrameBuffer(Format.RGBA8888, (int)(camera.viewportWidth*Scene.WORLD_TO_BOX), (int)(camera.viewportHeight*Scene.WORLD_TO_BOX), false);
		
		setCenter(x, y);
		setAngle(angle);
		
		
		
		createNew();
	}
	

	
	
	public void draw(SpriteBatch batch, OrthographicCamera camera){
		Texture t = getTexture();
		
		if(!isTextureReady){
			batch.end();
			drawOnBuffer(camera);
			batch.begin();
		}
		
		//batch.setColor(1, 1, 0, alpha);
		//BatchUtils.drawBloommed(batch, t, 0, 0, 1.65f);
		
		//batch.setColor(1, 1, 1, alpha);
		
		
		//batch.draw(t, getCenter().x, getCenter().y-100, len, 200, (int)0, (int)buffer.getHeight()/2-100, (int) len, 200, false, true);
		batch.draw(t, Scene.CAM_TRASL_X, 0);
	}
	
	public void update(float delta){
		
		if(alpha<=0){
			createNew();
			alpha = 1;
		}
		
		alpha-= delta * 3;
		
	}
	
	
	public void drawOnBuffer(OrthographicCamera camera){
		
		buffer.begin();
			Gdx.gl20.glClearColor(0, 0, 0, 0.5f);
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl20.glClearColor(1, 0, 0, 1f);
			shapeRenderer.setProjectionMatrix(camera.projection);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
				for(int i=0; i<thunderPoint.size; i++){
					Segment s = thunderPoint.get(i);
					shapeRenderer.rectLine(s.start, s.end, 2);
				}
		
			shapeRenderer.end();
			
		buffer.end();
		isTextureReady = true;
	}
	
	
	public void createNew(){
		float MAX_OFFSET = 100;
		float dst, angle, off;
		Vector2 midPoint = new Vector2();
		Vector2 branch = new Vector2();
		
		segmentPool.freeAll(thunderPoint);
		thunderPoint.clear();
		thunderPoint.add(segmentPool.obtain().set(new Vector2(-buffer.getWidth()/2, 0), new Vector2(-buffer.getWidth()/2+len, 0)));
		for(int i=0; i<7; i++){
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
				
				if(MathUtils.random(-2, 2)==0){
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
		isTextureReady = false;
	}
	
	
	
	
	public Texture getTexture(){
		return buffer.getColorBufferTexture();
	}
	
	
	static class Segment implements Poolable{
		public final Vector2 start = new Vector2();
		public final Vector2 end =  new Vector2();
		public Segment set(Vector2 start, Vector2 end) {
			this.start.set(start);
			this.end.set(end);
			return this;
		}
		public void reset(){
			
		}
	}
	
}
