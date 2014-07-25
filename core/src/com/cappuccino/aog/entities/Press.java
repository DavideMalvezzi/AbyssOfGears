package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.levels.Level;
import com.cappuccino.aog.mapeditor.EntityModel;
import com.cappuccino.aog.mapeditor.EntityModel.Property;
import com.cappuccino.aog.scene.GameScene;

public class Press extends Entity{

	private int dir;
	private float minLen, maxLen, vel;
	private final Vector2 startPos = new Vector2();
	private Entity tube, otherPress, wall;
	
	public Press(World world) {
		this(world, 0, 0, 10, 100, 1, 0 , 1);
	}
	
	public Press(World world, EntityModel model) {
		this(world, model.position.x, model.position.y, model.internalProp1.value, model.internalProp2.value, model.internalProp3.value, model.angle, model.scale.x); 
	}
	
	public Press(World world, float x, float y, float minLen, float maxLen, float vel, float angle, float scale) {
		super("PressPlate", world);
		this.tube = new Entity("PressTube", world);
		this.minLen = minLen;
		this.maxLen = maxLen;
		this.vel = vel;
		this.startPos.set(x, y);
		this.dir = 1;

		setScaleX(scale);
		setScaleY(scale);
		initBody(world, BodyType.DynamicBody);
		
		tube.setScaleX(maxLen/tube.getRealWidth());
		tube.setScaleY(scale);
		tube.initBody(world, BodyType.KinematicBody);
		
		initFixtures();
		
		setAngle(angle);
		tube.setAngle(angle);
		
		tube.setCenter(x, y);
		setCenter(x, y);
		
		JointsFactory.createWeldJoint(body.getWorld(), this, tube, Vector2.Zero, Vector2.Zero, getAngle(), true);
	}
	
	

	@Override
	public void initFixtures() {
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
		float dst = MathUtils.round(startPos.dst(getCenter()));
		//current scale = maxScale * scale%
		float scale = (maxLen/tube.getRealWidth()) * dst/maxLen;
		float trasl = maxLen - dst;
		
		
		tube.setScaleX(scale);
		tube.setTraslX(trasl*MathUtils.cos(getAngle()));
		tube.setTraslY(trasl*MathUtils.sin(getAngle()));
		
		if((dst<minLen && dir == -1) || (dst>maxLen && dir == 1)){
			dir *= -1;
		}
		
		float velX = vel*dir*MathUtils.cos(getAngle());
		float velY = vel*dir*MathUtils.sin(getAngle());
		velX = MathUtils.isZero(velX, 0.00001f)? 0 : velX;
		velY = MathUtils.isZero(velY, 0.00001f)? 0 : velY;
		tube.setLinearVelocity(velX, velY);
		
	}

	public boolean isClosed(){
		float dst = startPos.dst(getCenter());
		return dst/maxLen>=1;
	}
	
	public void open(){
		tube.setCenter(startPos.cpy().add(minLen*MathUtils.cos(getAngle()), minLen*MathUtils.sin(getAngle())));
		setCenter(startPos.cpy().add(minLen*MathUtils.cos(getAngle()), minLen*MathUtils.sin(getAngle())));
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
	
	public void setWall(Wall wall){
		this.wall = wall;
	}
	
	public void setOtherPress(Press otherPress){
		this.otherPress = otherPress;
	}
	
	@Override
	public void onCollide(Fixture sender, Fixture collided, Contact contact){
		String collidedName = (String)collided.getUserData();
		Alexy alexy = Level.getPlayer();
		
		if(otherPress!=null){
			float plateDst = getCenter().dst(otherPress.getCenter())-2*getWidth();

			if(plateDst<=alexy.getWidth() && dir == 1){
				if(collidedName.contains("Player") || collidedName.contains("Umbrella")){
					alexy.setState(Status.DYING);
					alexy.setDeadType(DeadType.PRESSED);
				}
			}
			
		}
	}

	public void setVelocity(float vel){
		this.vel = vel;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		tube.dispose();
	}
	
	
	@Override
	public void recalculate() {
		startPos.set(getCenter().cpy());
		tube.setAngle(getAngle());
		tube.setCenter(startPos.cpy());
		tube.setScaleX(maxLen/tube.getRealWidth());
		tube.setScaleY(getScaleX());
		
		tube.reloadFixtures();
		reloadFixtures();
		
	}
	
	@Override
	public Property getProp1() {
		return new Property("minLength", minLen);
	}
	@Override
	public Property getProp2() {
		return new Property("maxLength", maxLen);
	}
	@Override
	public Property getProp3() {
		return new Property("Velocity", vel);
	}
	
	@Override
	public void setProp1(float value) {
		this.minLen = value;
	}
	
	@Override
	public void setProp2(float value) {
		this.maxLen = value;
	}
	
	@Override
	public void setProp3(float value) {
		this.vel = value;
	}
	
	
	
}
