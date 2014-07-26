package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.StateMachine;
import com.cappuccino.aog.scene.GameScene;

public class Alexy extends Entity {
	
	public static final float MAX_LIN_VEL = 4f;
	public static final float LIN_ACCEL = 30f;
	
	private static final Vector2 playerAttachPoint = new Vector2(-6, 30);
	private static final Vector2 umbrellaAttachPoint = new Vector2(-1, -35);
	
	public static enum DeadType{
		ARROWED,
		LASERED,
		PIERCED, 
		POISONED, 
		PRESSED,
		FULMINATED;
	};
	
	private DeadType deadType;

	
	public Umbrella umbrella;
	private Joint umbrellaJoint;
	private StateMachine<Alexy> stateMachine;

	
	public Alexy(World world) {
		super("Player", world);
		umbrella = new Umbrella(world);
		
		initBody(world, BodyType.DynamicBody);
		initFixtures();
		
		setMass(6);
		
		umbrellaJoint = JointsFactory.createWeldJoint(body.getWorld(), umbrella, this, umbrellaAttachPoint.cpy(), playerAttachPoint.cpy(), 0, true);
		
		stateMachine = new StateMachine<Alexy>(this, AlexyStatus.LANDING);
	}
	
	
	public void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = PLAYER;
		fd.filter.maskBits = PLAYER_MASK;
		
		bodyLoader.attachFixture(body, "Player_head", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_body", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_feet", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		fd.isSensor = true;
		bodyLoader.attachFixture(body, "Player_bodySensor", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_feetSensor", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		
		origin.set(bodyLoader.getOrigin("Player_bodySensor", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}


	public void draw(SpriteBatch batch) {
		super.draw(batch);
		umbrella.draw(batch);
	}
	
	@Override
	public void update(float delta) {
		stateMachine.update(delta);
	}
	


	
	@Override
	public void setPosition(Vector2 position) {
		super.setPosition(position.cpy());
		umbrella.setPosition(position);
	}
	
	@Override
	public void setCenter(Vector2 position) {
		super.setCenter(position.cpy());
		umbrella.setCenter(position);
	}
	
	@Override
	public void setCenter(float x, float y) {
		super.setCenter(x,y);
		umbrella.setCenter(x,y);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		umbrella.dispose();
	}
	
	public AlexyStatus getState(){
		return (AlexyStatus) stateMachine.getCurrentState();
	}

	public void setState(AlexyStatus newState){
		stateMachine.changeState(newState);
	}
	
	public void setDeadType(DeadType deadType){
		this.deadType = deadType;
	}



}
