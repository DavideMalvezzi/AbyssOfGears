package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.scene.GameScene;
import com.cappuccino.aog.scene.GameSceneHud;

public class Alexy extends Entity {
	
	public static enum Status{
		UMBRELLA_OPEN,
		UMBRELLA_CLOSED,
		FALLING_BACK,
		RESTART_FALLING,
		DYING;
	};
	
	public static enum DeadType{
		PIERCED, BLOWED_UP, POISONED;
	};
	
	private Status state = Status.UMBRELLA_OPEN;
	private DeadType deadType;

	private static final Vector2 playerAttachPoint = new Vector2(-6, 30);
	private static final Vector2 umbrellaAttachPoint = new Vector2(-1, -35);
	private static final float MAX_LIN_VEL = 4f;
	private static final float LIN_ACCEL = 30f;
	
	public Umbrella umbrella;
	private Joint umbrellaJoint;
	
	
	public Alexy(World world) {
		super("Player", world);
		umbrella = new Umbrella(world);
		
		init(world, BodyType.DynamicBody);
		initFixture();
		
		setMass(6);
		
		umbrellaJoint = JointsFactory.createWeldJoint(body.getWorld(), umbrella, this, umbrellaAttachPoint.cpy(), playerAttachPoint.cpy());
	}
	
	
	public void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = PLAYER;
		fd.filter.maskBits = PLAYER_MASK;
		
		bodyLoader.attachFixture(body, "Player_head", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_chest", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_legs", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_feet", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("Player_chest", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}


	public void draw(SpriteBatch batch) {
		super.draw(batch);
		umbrella.draw(batch);
		
		if(state==Status.UMBRELLA_CLOSED){
			Assets.fallingEffect.draw(batch);
		}
	}
	
	@Override
	public void active() {
		//TODO reset filter data
	}
	
	
	public void update(float delta){
		/*
		if(Gdx.input.isKeyPressed(Keys.LEFT))setLinearVelocity(-5, 0);
		if(Gdx.input.isKeyPressed(Keys.RIGHT))setLinearVelocity(5, 0);
		if(Gdx.input.isKeyPressed(Keys.DOWN))setLinearVelocity(0, -5);
		if(Gdx.input.isKeyPressed(Keys.UP))setLinearVelocity(0, 5);
		*/
		
		switch (state) {
		
			case UMBRELLA_OPEN:
				move(delta);
				
				if(GameSceneHud.useUmbrellaIsChecked()){
					state = Status.UMBRELLA_CLOSED;
				}
				break;
				
			case UMBRELLA_CLOSED:
				move(delta);
				Assets.fallingEffect.setPosition(getX()+getWidth()/2, getY()+getHeight()/2);
				Assets.fallingEffect.update(delta);
				
				getBody().setLinearVelocity(getBody().getLinearVelocity().x*0.8f, -20);
				umbrella.getBody().setLinearVelocity(getBody().getLinearVelocity().x*0.8f, -20);
				
				if(!GameSceneHud.useUmbrellaIsChecked()){
					getBody().setLinearVelocity(getBody().getLinearVelocity().x, 25);
					umbrella.getBody().setLinearVelocity(getBody().getLinearVelocity().x, 25);
					state = Status.FALLING_BACK;
				}
				break;
				
			case FALLING_BACK:
				Assets.fallingEffect.reset();
				if(getBody().getLinearVelocity().y>0f){
					getBody().applyForceToCenter(0, -350, true);
					umbrella.getBody().applyForceToCenter(0, -350, true);
				}else{
					state = Status.RESTART_FALLING;
				}
				break;
				
			case RESTART_FALLING:
				if(getBody().getLinearVelocity().y>-1.5f){
					getBody().applyForceToCenter(0, -250, true);
					umbrella.getBody().applyForceToCenter(0, -250, true);
				}else{
					state = Status.UMBRELLA_OPEN;
				}
				break;
				
		case DYING:
				if(umbrellaJoint!=null){
					getBody().getWorld().destroyJoint(umbrellaJoint);
					umbrellaJoint = null;
				}
				getBody().applyForceToCenter(0, -100, true);
				umbrella.getBody().applyForceToCenter(0, -50, true);
				umbrella.update(delta);
				deadAnimation(delta);
				break;
						
		default:
			break;

		}
		
	}
	
	private void move(float delta) {
		umbrella.update(delta);
		if(Gdx.app.getType()==ApplicationType.Desktop){
				if(Gdx.input.isKeyPressed(Keys.LEFT) && getLinearVelocity().x>-MAX_LIN_VEL){
					getBody().applyForceToCenter(-LIN_ACCEL, 0, true);
					umbrella.getBody().applyForceToCenter(-LIN_ACCEL, 0, true);
				}
				if(Gdx.input.isKeyPressed(Keys.RIGHT) && getBody().getLinearVelocity().x<MAX_LIN_VEL){
					getBody().applyForceToCenter(LIN_ACCEL, 0, true);
					umbrella.getBody().applyForceToCenter(LIN_ACCEL, 0, true);
				}
		}else{
			// accel = max_Accel * sin(angolo schermo)
			float accel = (float) (LIN_ACCEL * 3 * -MathUtils.sinDeg(Gdx.input.getPitch()));
			
			if((getLinearVelocity().x<MAX_LIN_VEL && accel>0) || (getLinearVelocity().x>-MAX_LIN_VEL && accel<0)){
				getBody().applyForceToCenter(accel, 0, true);
				umbrella.getBody().applyForceToCenter(accel, 0, true);
			}
		}
		setAngularVelocity(umbrella.getAngularVelocity()/10);
	}

	private void deadAnimation(float delta){
		switch (deadType) {
		case PIERCED:
			
			break;

		case BLOWED_UP:
			
			break;
			
		case POISONED:
			break;
		
		default:
			break;
		}
	}
	

	@Override
	public void dispose() {
		super.dispose();
		umbrella.dispose();
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
	
	public void setCenter(float x, float y) {
		super.setCenter(x,y);
		umbrella.setCenter(x,y);
	}
	
	public Status getState(){
		return state;
	}

	public void setState(Status state){
		this.state = state;
	}
	
	public void setDeadType(DeadType deadType){
		this.deadType = deadType;
	}



}
