package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.StateMachine;
import com.cappuccino.aog.scene.GameScene;

public class Alexy extends Entity {
	
	public static final float MAX_LIN_VEL = 4f;
	public static final float LIN_ACCEL = 50f;
	public static float ANIMATION_TIME = 0;
	public static int WALK_DIR = 1, TOUCHED_WALL = 0;
	
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

	
	private Umbrella umbrella;
	private Entity rotatingFoot;
	private Joint umbrellaJoint;
	private StateMachine<Alexy> stateMachine;

	public Alexy(World world) {
		super("Player", world);
		umbrella = new Umbrella(world);
		rotatingFoot = new Entity("Player");
		
		initBody(world, BodyType.DynamicBody);
		rotatingFoot.initBody(world, BodyType.DynamicBody);
		initFixtures();
		
		setMass(6);
		
		umbrellaJoint = JointsFactory.createWeldJoint(body.getWorld(), umbrella, this, umbrellaAttachPoint.cpy(), playerAttachPoint.cpy(), 0, true);
		//JointsFactory.createWeldJoint(world, this, rotatingFoot, new Vector2(0, -20), Vector2.Zero, 0, false);
		JointsFactory.createDistanceJoint(world, this, rotatingFoot, Vector2.Zero, Vector2.Zero, 20, false);
		JointsFactory.createRevoluteJoint(world, this, rotatingFoot, new Vector2(0, -20), Vector2.Zero,  false);
		
		stateMachine = new StateMachine<Alexy>(this, AlexyStatus.FALLING_UMBRELLA_OPEN);
	}
	
	
	public void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = PLAYER;
		fd.filter.maskBits = PLAYER_MASK;
		
		bodyLoader.attachFixture(body, "Player_head", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_body", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		bodyLoader.attachFixture(rotatingFoot.getBody(), "Player_feet", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		fd.isSensor = true;
		bodyLoader.attachFixture(body, "Player_bodySensor", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Player_feetSensor", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		
		origin.set(bodyLoader.getOrigin("Player_bodySensor", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
		rotatingFoot.origin.set(bodyLoader.getOrigin("Player_feetSensor", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
		
		
		
	}


	public void draw(SpriteBatch batch) {
		if(getState() == AlexyStatus.LANDED){
			TextureRegion frame = Assets.player_WALK.getKeyFrame(ANIMATION_TIME);
			batch.draw(
					frame.getTexture(), 
					getX()+trasl.x,getY()+trasl.y,
					getOrigin().x, getOrigin().y,
					frame.getRegionWidth(), frame.getRegionHeight(), 
					1, 1, 
					0, 
					frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(), 
					WALK_DIR == 1 ? false : true, false);
			
		}else{
			super.draw(batch);
			umbrella.draw(batch);
		}
		//super.draw(batch);
		//umbrella.draw(batch);
	}
	
	@Override
	public void update(float delta) {
		stateMachine.update(delta);
	}
	
	@Override
	public void onStartCollide(Fixture sender, Fixture collided, Contact contact){
		String senderName = (String)sender.getUserData();
		String collidedName = (String)collided.getUserData();
		
		if(senderName.equals("Player_feetSensor")){
			System.out.println("Alexy.onStartCollide()");
			TOUCHED_WALL++;
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					stateMachine.changeState(AlexyStatus.LANDED);
				}
			});
		}
	}

	@Override
	public void onEndCollide(Fixture sender, Fixture collided, Contact contact) {
		String senderName = (String)sender.getUserData();
		String collidedName = (String)collided.getUserData();
		
		if(senderName.equals("Player_feetSensor") && getState()==AlexyStatus.LANDED){
			System.out.println("Alexy.onEndCollide()");
			TOUCHED_WALL--;
			if(TOUCHED_WALL<=0){
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						System.err.println("Falling");
						stateMachine.changeState(AlexyStatus.FALLING_UMBRELLA_OPEN);
					}
				});
			}
				
		}
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
		rotatingFoot.setCenter(position);
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
	
	


	
	public Umbrella getUmbrella(){return umbrella;}
	public Entity getFoot(){return rotatingFoot;}
	public Joint getUmbrellaJoint(){return umbrellaJoint;}
	public AlexyStatus getState(){return (AlexyStatus) stateMachine.getCurrentState();}

	public void setState(AlexyStatus newState){
		stateMachine.changeState(newState);
	}
	
	public void setDeadType(DeadType deadType){
		this.deadType = deadType;
	}


}
