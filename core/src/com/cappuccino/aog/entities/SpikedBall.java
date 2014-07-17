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
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.levels.Level;
import com.cappuccino.aog.mapeditor.EntityModel;
import com.cappuccino.aog.mapeditor.EntityModel.Property;
import com.cappuccino.aog.scene.GameScene;

public class SpikedBall extends Entity {
	
	private Joint attacched;

	public SpikedBall(World world) {
		this(world, 0, 0, 1, 1);
	}
	
	public SpikedBall(World world, EntityModel model) {
		this(world, model.position.x, model.position.y, model.internalProp1.value, model.scale.x);
	}
	
	
	
	public SpikedBall(World world, float x, float y, float speed, float scale) {
		super("SpikedBall", world);
		scaleX = scaleY = scale;
		initBody(world, BodyType.KinematicBody);
		initFixtures();
		setCenter(x, y);
		setAngularVelocity(speed);
	}

	
	@Override
	public void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = ENTITY_MASK;
		
		bodyLoader.attachFixture(body, "SpikedBall_ball", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "SpikedBall_sensor", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);

		origin.set(bodyLoader.getOrigin("SpikedBall_ball", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}

	@Override
	public void onCollide(final Fixture sender, final Fixture collided, final Contact contact) {
		final String collidedName = (String)collided.getUserData();
		final Entity collidedEntity = ((EntityData)collided.getBody().getUserData()).getEntity();
		final Alexy alexy = Level.getPlayer();
		
		if(collidedName.contains("Umbrella")){
			
		}else if(attacched == null && alexy.getState()!=Status.DYING ){
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					while(getBody().getWorld().isLocked());
					Vector2 contactPoint = contact.getWorldManifold().getPoints()[0].scl(GameScene.WORLD_TO_BOX);
					Vector2 senderAttach = new Vector2();
					
					float dst = contactPoint.dst(getCenter());
					float angle = MathUtils.atan2(collidedEntity.getCenter().y-getCenter().y, collidedEntity.getCenter().x-getCenter().x);
					
					senderAttach.set(dst*MathUtils.cos(angle), dst*MathUtils.sin(angle));
					senderAttach.rotateRad(-getAngle());
					
					attacched = JointsFactory.createWeldJoint(body.getWorld(), collidedEntity, SpikedBall.this, Vector2.Zero, senderAttach, getAngle(), false);
					collidedEntity.setAngularVelocity(body.getAngularVelocity());
					collidedEntity.setFilter(getFilter());
					
				}
			});
			
		}
		alexy.setState(Status.DYING);
		alexy.setDeadType(DeadType.PIERCED);
	}
	
	@Override
	public Property getProp1() {
		return new Property("Omega", getAngularVelocity());
	}
	
	@Override
	public void setProp1(float value) {
		setAngularVelocity(value);
	}

}
