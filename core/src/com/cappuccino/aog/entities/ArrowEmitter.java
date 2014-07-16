package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.mapeditor.EntityModel.Property;

public class ArrowEmitter extends Entity {

	private static final Pool<Arrow> arrowsPool = new Pool<Arrow>(){
		protected Arrow newObject() {
			return new Arrow();
		};
	};
	
	private final Array<Arrow> emittedArrow = new Array<Arrow>();
	
	private float timer, shootTime = 5;
	private Vector2 emissionPoint;
	
	
	public ArrowEmitter(World world) {
		super("ArrowEmitter", world);
		
		init(world, BodyType.StaticBody);
		initFixture();
		
		emissionPoint = new Vector2(getCenter().add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle())));
	}
	
	
	public ArrowEmitter(World world, float x, float y, float angle, float shootTime) {
		super("ArrowEmitter", world);
		this.shootTime = shootTime;
		
		init(world, BodyType.StaticBody);
		initFixture();
		
		setAngle(angle);
		setCenter(x,y);
		
		emissionPoint = new Vector2(getCenter().add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle())));
	}

	@Override
	public void disactive() {
		arrowsPool.freeAll(emittedArrow);
		emittedArrow.clear();
	}
	
	@Override
	protected void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;

		bodyLoader.attachFixture(body, "ArrowEmitter", fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("ArrowEmitter", getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		for(int i=0; i<emittedArrow.size; i++){
			emittedArrow.get(i).draw(batch);
		}
	}
	
	@Override
	public void update(float delta) {
		timer+=delta;
		if(timer>=shootTime){
			Arrow a = arrowsPool.obtain();
			if(a.getBody()==null){
				a.init(body.getWorld(), BodyType.DynamicBody);
			}
			a.setCenter(emissionPoint);
			a.setAngle(getAngle());
			a.setLinearVelocity(Arrow.MAX_SPEED*MathUtils.cos(getAngle()), Arrow.MAX_SPEED*MathUtils.sin(getAngle()));
			a.getBody().setActive(true);
			emittedArrow.add(a);
			timer = 0;
		}
		
		for(int i=0; i<emittedArrow.size; i++){
			Arrow a = emittedArrow.get(i);
			a.update(delta);
			if(a.getDespawnTimer()>=Arrow.DESPAWN_TIME){
				arrowsPool.free(a);
				emittedArrow.removeIndex(i--);
			}
		}
		
	}
	
	
	@Override
	public void dispose() {
		super.dispose();
		for(int i=0; i<emittedArrow.size; i++){
			emittedArrow.get(i).dispose();
		}
		
		for(int i=0; i<arrowsPool.getFree(); i++){
			arrowsPool.obtain().dispose();
		}
	}
	
	
	@Override
	public Property getProp1() {
		return new Property("EmissionDelay", shootTime);
	}
	
	@Override
	public void setProp1(float value) {
		shootTime = value;
	}
	
	@Override
	public void recalculate() {
		emissionPoint = new Vector2(getCenter().add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle())));
	}
	
}
