package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.scene.GameScene;

public class Press extends Entity{

	private int dir;
	private float minLen, maxLen, vel;
	private final Vector2 startPos = new Vector2();
	private Entity tube;

	
	public Press(World world, float x, float y, float minLen, float maxLen, float vel, float angle, float scale) {
		super("PressPlate", world);
		tube = new Entity("PressTube", world);
		
		this.minLen = minLen;
		this.maxLen = maxLen;
		this.vel = vel;
		this.startPos.set(x, y);
		this.dir = 1;

		setScaleX(scale);
		setScaleY(scale);
		init(world, BodyType.DynamicBody);
		
		tube.setScaleX(maxLen/tube.getRealWidth());
		tube.setScaleY(scale);
		tube.init(world, BodyType.KinematicBody);
		
		initFixture();
		
		setAngle(angle);
		tube.setAngle(angle);
		
		tube.setCenter(x, y);
		setCenter(x+tube.getWidth()*MathUtils.cos(angle), y+tube.getWidth()*MathUtils.sin(angle));
		
		JointsFactory.createWeldJoint(body.getWorld(), this, tube, Vector2.Zero, Vector2.Zero, getAngle(), true);
		
	}

	@Override
	public void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = ENTITY_MASK;
		
		bodyLoader.attachFixture(body, "Press_plate", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(tube.getBody(), "Press_tube", fd, tube.getRealWidth()*tube.getScaleX()*GameScene.BOX_TO_WORLD, tube.getRealWidth()*tube.getScaleY()*GameScene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("Press_plate", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
		tube.setOrigin(bodyLoader.getOrigin("Press_tube", tube.getRealWidth()*tube.getScaleX()*GameScene.BOX_TO_WORLD, tube.getRealWidth()*tube.getScaleY()*GameScene.BOX_TO_WORLD));
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		tube.draw(batch);
	}

	public void update(float delta) {
		float dst = startPos.dst(getCenter());
		//current scale = maxScale * scale%
		float scale = (maxLen/tube.getRealWidth()) * dst/maxLen;
		float trasl = maxLen - dst;
		
		tube.setScaleX(scale+0.1f);
		tube.setTraslX(trasl*MathUtils.cos(getAngle()));
		tube.setTraslY(trasl*MathUtils.sin(getAngle()));
		
		if((dst<minLen && dir == -1) || (dst>maxLen && dir == 1)){
			dir *= -1;
		}
		tube.setLinearVelocity(vel*dir*MathUtils.cos(getAngle()), vel*dir*MathUtils.sin(getAngle()));
	}

	public boolean isClosed(){
		float dst = startPos.dst(getCenter());
		float scale = (maxLen/tube.getRealWidth()) * dst/maxLen;
		return scale>0.985f;
	}
	
	public void open(){
		tube.setCenter(tube.getCenter().x-(maxLen-minLen)*MathUtils.cos(getAngle()), tube.getCenter().y-(maxLen-minLen)*MathUtils.sin(getAngle()));
		setCenter(getCenter().x-(maxLen-minLen)*MathUtils.cos(getAngle()), getCenter().y-(maxLen-minLen)*MathUtils.sin(getAngle()));
	}
	
	@Override
	public void disactive() {
		tube.getBody().setActive(false);
		getBody().setActive(false);
	}
	
	@Override
	public void active() {
		tube.getBody().setActive(true);
		getBody().setActive(true);
	}
	
	@Override
	public void onCollide(Fixture sender, Fixture collided, Contact contact) {
		/*
		final Alexy alexy = Level.getPlayer();
		int pressure = 0;
		float[] normal = impulse.getNormalImpulses();
		for(int i=0; i<normal.length; i++){
			pressure+=normal[i];
		}
		System.out.println(pressure);
		if(pressure>30*vel){
			alexy.setState(Status.DYING);
			alexy.setDeadType(DeadType.BLOWED_UP);
		}
		*/
	}

	@Override
	public void dispose() {
		super.dispose();
		tube.dispose();
	}
	
}
