package com.cappuccino.aog.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.levels.Level;
import com.cappuccino.aog.scene.GameScene;

public class Spear extends Entity{

	private float vel, maxLen;
	private final Vector2 startPos = new Vector2();
	private Joint attacched;
	private final RayCastCallback callBack = new RayCastCallback() {
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			String fixName = (String) fixture.getUserData();
			if(fixName.contains("Player")){
				body.setActive(true);
				return 0;
			}
			return 1;
		}
	};
	
	
	public Spear(World world, float x, float y, float maxLen, float vel, float angle, float scale) {
		super("Spear", world);
		this.startPos.set(x-maxLen*MathUtils.cos(angle),y-maxLen*MathUtils.sin(angle));
		this.maxLen = maxLen;
		this.vel = vel;
		
		setScaleX(maxLen/getRealWidth());
		setScaleX(scale);
		
		init(world, BodyType.KinematicBody);
		initFixture();
		
		setAngle(angle);
		setCenter(startPos);
		
		body.setActive(false);
	}
	
	@Override
	public void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = ENTITY_MASK;

		bodyLoader.attachFixture(body, "Spear_pole", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Spear_sensor", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("Spear_pole", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}
	
	@Override
	public void update(float delta) {
		float dst = getCenter().dst(startPos);
		Vector2 start = new Vector2(startPos);
		Vector2 end = new Vector2();
		
		start.add(getWidth()*MathUtils.cos(getAngle()),getWidth()*MathUtils.sin(getAngle()));
		end.set(start.x+1000*MathUtils.cos(getAngle()), start.y+1000*MathUtils.sin(getAngle()));
		start.scl(GameScene.BOX_TO_WORLD);
		end.scl(GameScene.BOX_TO_WORLD);
		
		if(!body.isActive()){
			body.getWorld().rayCast(callBack, start, end);
		}else if(dst<maxLen){
			setLinearVelocity(vel*MathUtils.cos(getAngle()), vel*MathUtils.sin(getAngle()));
		}else{
			setLinearVelocity(0, 0);
		}
	}
	
	@Override
	public void onCollide(final Fixture sender, final Fixture collided, final Contact contact) {
		final Alexy alexy = Level.getPlayer();
		final Entity collidedEntity = ((EntityData)collided.getBody().getUserData()).getEntity();
		final String senderFixName = (String)sender.getUserData();
		final String collidedFixName = (String)collided.getUserData();
		final Vector2 contactPoint = contact.getWorldManifold().getPoints()[0].scl(GameScene.WORLD_TO_BOX);
		
		if(attacched == null && !senderFixName.contains("pole") && alexy.getState()!=Status.DYING){
			
			if(!collidedFixName.contains("Player") && !collidedFixName.equals("Umbrella_sensor"))return;
			
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					while(getBody().getWorld().isLocked());
					Vector2 senderAttach = new Vector2();
					Vector2 collidedAttach = new Vector2();
					
					float dst = contactPoint.dst(getCenter());
					float angle = MathUtils.atan2(collidedEntity.getCenter().y-getCenter().y, collidedEntity.getCenter().x-getCenter().x);
					
					senderAttach.set(dst*MathUtils.cos(angle), dst*MathUtils.sin(angle));
					senderAttach.rotateRad(-getAngle());
					
					if(collidedFixName.contains("Umbrella")){
						dst = contactPoint.dst(collidedEntity.getCenter())*0.7f;
						collidedAttach.set(dst*MathUtils.cos(angle-180*MathUtils.degRad), dst*MathUtils.sin(angle-180*MathUtils.degRad));
						collidedAttach.rotateRad(-collidedEntity.getAngle());
					}else{
						collidedAttach.set(Vector2.Zero);
					}
					
					attacched = JointsFactory.createWeldJoint(body.getWorld(), collidedEntity, Spear.this, collidedAttach, senderAttach, getAngle(), false);
					collidedEntity.getBody().setAngularVelocity(body.getAngularVelocity());
				}
			});
			alexy.setState(Status.DYING);
			alexy.setDeadType(DeadType.PIERCED);
		}
	}

}
