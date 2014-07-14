package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.cappuccino.aog.BatchUtils;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.levels.Level;

public class Thunder extends Entity {

	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private static final Pool<Segment> segmentPool = new Pool<Segment>(){
			protected Segment newObject() {
				return new Segment();
			}
	};
	
	public FrameBuffer light_buf, bloom_buf;
	private final Array<Segment> thunderPoint = new Array<Segment>();
	
	private float alpha, len, maxOffset, timer, duration;
	private boolean isTextureReady = false;
	
	
	public Thunder(World world, float x, float y, float len, float maxOffset, float angle, float delay, float duration) {
		super("LaserEmitter", world);
		init(world, BodyType.KinematicBody);
		origin.set(0, maxOffset).scl(Scene.BOX_TO_WORLD);
		
		this.len = len;
		this.maxOffset = maxOffset;
		this.alpha = delay;
		this.duration = duration;
		this.light_buf = new FrameBuffer(Format.RGBA8888, (int)len, (int)maxOffset*2, false);
		this.bloom_buf = new FrameBuffer(Format.RGBA8888, (int)len, (int)maxOffset*2, false);
		
		setCenter(x, y);
		setAngle(angle);
		
		createNew();
	}
	
	
	@Override
	public void draw(SpriteBatch batch){
		if(body.isActive()){
			Texture light_text = light_buf.getColorBufferTexture();
			Texture bloom_text = bloom_buf.getColorBufferTexture();
		
			if(!isTextureReady){
				drawOnBuffer(batch);
			}
		
			batch.setColor(144f/255f, 208f/255f, 248f/255f, alpha);
			batch.draw(bloom_text, getX(), getY(), 
					0, bloom_text.getHeight()/2, bloom_text.getWidth(), bloom_text.getHeight(), 
					1,1, getAngle()*MathUtils.radDeg, 
					0, 0, bloom_text.getWidth(), bloom_text.getHeight(),
					false, true);

		
			batch.setColor(1, 1, 1, alpha);
			batch.draw(light_text, getX(), getY(), 
					0, light_text.getHeight()/2, light_text.getWidth(), light_text.getHeight(), 
					1,1, getAngle()*MathUtils.radDeg, 
					0, 0, light_text.getWidth(), light_text.getHeight(),
					false, true);
		
			batch.setColor(Color.WHITE);
		}
	}
	
	public void update(float delta){
		if(timer>=duration){
			body.setActive(!body.isActive());
			timer = 0;
		}
		timer+=delta;

		if(body.isActive()){
			if(alpha<0){
				createNew();
				alpha = 1;
			}
			alpha-= delta * 3;
		}
	}
	
	
	public void drawOnBuffer(SpriteBatch batch){
		batch.end();
		light_buf.begin();
			Gdx.gl20.glClearColor(0, 0, 0, 0f);
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl20.glClearColor(1, 0, 0, 1f);
			shapeRenderer.setProjectionMatrix(BatchUtils.getBufferProj(light_buf));
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
				for(int i=0; i<thunderPoint.size; i++){
					Segment s = thunderPoint.get(i);
					shapeRenderer.rectLine(s.start, s.end, 2);
				}
		
			shapeRenderer.end();
			
		light_buf.end();
		batch.begin();
		
		BatchUtils.drawBloommedOnBuffer(bloom_buf, light_buf.getColorBufferTexture(), batch, 1.65f);
		
		isTextureReady = true;
	}
	
	
	public void createNew(){
		float MAX_OFFSET = maxOffset;
		float dst, angle, off;
		Vector2 midPoint = new Vector2();
		Vector2 branch = new Vector2();
		
		segmentPool.freeAll(thunderPoint);
		thunderPoint.clear();
		thunderPoint.add(segmentPool.obtain().set(new Vector2(0, maxOffset), new Vector2(len, maxOffset)));
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
					
					branch.y = MathUtils.clamp(branch.y, -maxOffset*2, maxOffset*2);
					
					thunderPoint.insert(j+2, segmentPool.obtain().set(midPoint, branch));
					j++;
				}
				
				j++;
			}
			MAX_OFFSET*=0.5f;
		}
		
		Array<Fixture> fixtures = body.getFixtureList();
		
		for(int i=0; i<fixtures.size; i++){
			body.destroyFixture(fixtures.get(i));
			i--;
		}
		
		for(int i=0; i<thunderPoint.size; i++){
			body.createFixture(thunderPoint.get(i).getFixDef(origin)).setUserData("Thunder");
		}
		
		
		isTextureReady = false;
	}
	
	@Override
	public void onCollide(Fixture sender, Fixture collided, Contact contact){
		Alexy alexy = Level.getPlayer();
		String collidedName = (String) collided.getUserData();
		if(alexy.getState() != Status.DYING){
			if(collidedName.contains("Umbrella") || collidedName.contains("Player")){
				alexy.setState(Status.DYING);
				alexy.setDeadType(DeadType.FULMINATED);
			}
		}
	}
	
	public void dispose() {
		super.dispose();
		thunderPoint.clear();
		segmentPool.clear();
		light_buf.dispose();
		bloom_buf.dispose();
	}
	
	public static class Segment implements Poolable{
		public final Vector2 start = new Vector2();
		public final Vector2 end =  new Vector2();
		public Segment set(Vector2 start, Vector2 end) {
			this.start.set(start);
			this.end.set(end);
			return this;
		}
		public FixtureDef getFixDef(Vector2 origin){
			FixtureDef def = new FixtureDef();
			ChainShape line = new ChainShape();
			float vertices[] = new float[]{
					start.x*Scene.BOX_TO_WORLD,
					start.y*Scene.BOX_TO_WORLD-origin.y,
					end.x*Scene.BOX_TO_WORLD,
					end.y*Scene.BOX_TO_WORLD-origin.y,
				};
			line.createChain(vertices);
			def.filter.categoryBits = ENTITY;
			def.filter.maskBits = ENTITY_MASK;
			def.isSensor = true;
			def.shape = line;
			
			
			return def;
		}
		public void reset(){
			
		}
	}
	
	
}
