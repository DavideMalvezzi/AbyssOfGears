package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.levels.Level;

public class LaserEmitter extends Entity {

	private Entity laser;
	private float maxLen, curLen, duration, timer;
	
	private final RayCastCallback callBack = new RayCastCallback() {
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			String name = (String) fixture.getUserData();
			float dst = laser.getCenter().dst(point.scl(Scene.WORLD_TO_BOX));
			if( dst < curLen && !name.equals("Laser")){
				curLen = dst;
			}
			return 1;
		}
	};
	
	public LaserEmitter(World world, float x, float y, float angle, float maxLen, float duration) {
		super("LaserEmitter", world);
		this.maxLen = maxLen;
		this.duration = duration;
		
		laser = new Entity("Laser", world);
		
		init(world, BodyType.StaticBody);
		laser.init(world, BodyType.KinematicBody);
		laser.setScaleX(maxLen/laser.getRealWidth());
		laser.setScaleY(0.5f);
		initFixture();
		
		setAngle(angle);
		setCenter(x, y);

		laser.setAngle(angle);
		laser.setCenter(x+(getWidth()-getOrigin().x)*MathUtils.cos(angle), y+(getWidth()-getOrigin().x)*MathUtils.sin(angle));
		
		((EntityData)laser.getBody().getUserData()).setEntity(this);
	}
	
	@Override
	protected void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = ENTITY_MASK;
		
		bodyLoader.attachFixture(body, "LaserEmitter", fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		
		fd.isSensor = true;
		bodyLoader.attachFixture(laser.getBody(), "Laser", fd, laser.getRealWidth()*laser.scaleX*Scene.BOX_TO_WORLD, laser.getRealWidth()*laser.scaleY*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("LaserEmitter", getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
		laser.origin.set(bodyLoader.getOrigin("Laser", laser.getRealWidth()*laser.scaleX*Scene.BOX_TO_WORLD, laser.getRealWidth()*laser.scaleY*Scene.BOX_TO_WORLD));
		
	}
	
	@Override
	public void update(float delta) {
		Vector2 start = laser.getCenter().cpy().scl(Scene.BOX_TO_WORLD);
		Vector2 end = laser.getCenter().cpy().add(maxLen*MathUtils.cos(getAngle()), maxLen*MathUtils.sin(getAngle())).scl(Scene.BOX_TO_WORLD);
		
		curLen = maxLen;
		body.getWorld().rayCast(callBack, start, end);
		laser.setScaleX(curLen/laser.getRealWidth()+0.05f);
		
		timer +=delta;
		if(timer>=duration){
			timer = 0;
			laser.getBody().setActive(!laser.getBody().isActive());
		}
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if(laser.getBody().isActive()){
			laser.draw(batch);
		}
	}
	
	@Override
	public void disactive() {
		super.disactive();
		laser.dispose();
	}

	
	@Override
	public void onCollide(Fixture sender, Fixture collided, Contact contact){
		String collidedName = (String) collided.getUserData();
		Alexy alexy = Level.getPlayer();
		if(collidedName.contains("Player") || collidedName.contains("Umbrella")){
			alexy.setState(Status.DYING);
			alexy.setDeadType(DeadType.LASERED);
		}
	}
}
