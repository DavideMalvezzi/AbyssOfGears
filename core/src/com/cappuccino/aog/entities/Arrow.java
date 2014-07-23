package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.levels.Level;
import com.cappuccino.aog.scene.GameScene;

public class Arrow extends Entity implements Poolable {

	private Joint attached;
	private float timer;
	private boolean isRunnablePosted = false;
	public static final float DESPAWN_TIME = 5, MAX_SPEED = 50;
	
	
	public Arrow() {
		super("Arrow");
	}
	
	@Override
	protected void initBody(World world, BodyType type) {
		super.initBody(world, type);
		body.setBullet(true);
		body.setGravityScale(9.8f);
		initFixtures();
		setMass(6);
	}
	
	@Override
	protected void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = BULLET;
		fd.filter.maskBits = BULLET_MASK;

		bodyLoader.attachFixture(body, "Arrow_body", fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Arrow_sensor", fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("Arrow_body", getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
		
	}

	@Override
	public void reset() {
		isRunnablePosted = false;
		timer = 0;
		if(attached!=null){
			body.getWorld().destroyJoint(attached);
			attached = null;
		}
		
		
	}
	
	@Override
	public void update(float delta) {
		if(!body.isActive()){
			timer+=delta;
		}
	}
	
	@Override
	public void onCollide(Fixture sender, final Fixture collided, final Contact contact) {
		String senderName = (String)sender.getUserData();
		final String collidedName = (String)collided.getUserData();
		final Alexy alexy = Level.getPlayer();
		
		if(attached == null && senderName.contains("sensor") && !isRunnablePosted){
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					Entity collidedEntity = ((EntityData)collided.getBody().getUserData()).getEntity();
					
					Vector2 contactPoint = contact.getWorldManifold().getPoints()[0].scl(GameScene.WORLD_TO_BOX);
					Vector2 arrowAttach = new Vector2(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle()));
					arrowAttach.rotateRad(-getAngle());
					Vector2 collidedAttach = new Vector2(contactPoint).sub(collidedEntity.getCenter()).scl(0.6f);
					collidedAttach.rotateRad(-collidedEntity.getAngle());
					
					
					if(collidedName.contains("Player") || collidedName.contains("Umbrella")){
						alexy.setState(Status.DYING);
						alexy.setDeadType(DeadType.ARROWED);
					}else{
						body.setActive(false);
					}
					
					attached = JointsFactory.createWeldJoint(
							body.getWorld(), Arrow.this, collidedEntity, 
							arrowAttach, collidedAttach , getAngle(), false);
					
				}
			});
			isRunnablePosted = true;
		}
	}
	
	public float getDespawnTimer(){
		return timer;
	}

}
