package com.cappuccino.aog.entities.collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.scene.GameScene;

public class Repulse implements CollisionListener {

	public void onCollision(Entity sender, Entity collidedEntity, Contact contact, ContactImpulse impulse) {
		Vector2 senderPos = sender.getPosition();
		Vector2 collidedPos = alexy.getPosition();
		Vector2 contactPoint = contact.getWorldManifold().getPoints()[0].scl(GameScene.WORLD_TO_BOX);
		Vector2 vel = alexy.getBody().getLinearVelocity();
		
		float collisionAngle = MathUtils.atan2(collidedPos.y-contactPoint.y, collidedPos.x-contactPoint.x) + 90*MathUtils.degRad;
		vel.rotateRad(collisionAngle);
		
		if((senderPos.x<collidedPos.x && vel.x<0) || (senderPos.x>collidedPos.x && vel.x>0)){
			alexy.getBody().setLinearVelocity(-vel.x, vel.y);
			
		}
		
		Gdx.input.vibrate(50);
	}

}
