package com.cappuccino.aog.entities.collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Joint;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.JointsFactory;
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.scene.GameScene;

public class Pierce implements CollisionListener {

	private Joint attacched = null;
	
	@Override
	public void onCollision(final Entity sender, final Entity collidedEntity, final Contact contact, final ContactImpulse impulse) {
		
		if(alexy.getState()!=Status.DYING || attacched == null){
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					while(sender.getBody().getWorld().isLocked());
					Vector2 contactPoint = contact.getWorldManifold().getPoints()[0].scl(GameScene.WORLD_TO_BOX);
					Vector2 senderAttach = new Vector2();
					Vector2 playerAttach = new Vector2();
					
					if(alexy==collidedEntity){
					
						senderAttach.set(contactPoint.sub(sender.getCenter()));
						senderAttach.rotateRad(-sender.getAngle());
						//senderAttach.rotateRad(-sender.getAngle()).scl(0.9f-contact.getWorldManifold().getSeparations()[0]);
					
						contactPoint = contact.getWorldManifold().getPoints()[0].scl(GameScene.WORLD_TO_BOX);
						playerAttach.set(contactPoint.sub(alexy.getCenter()));
						playerAttach.rotateRad(-alexy.getAngle());
						
						attacched = JointsFactory.createWeldJoint(sender.getBody().getWorld(), collidedEntity, sender, playerAttach, senderAttach, sender.getAngle(), true);
						
					}
					alexy.setState(Status.DYING);
					alexy.setDeadType(DeadType.PIERCED);
				}
			});
			
			
		}
	}
	
}
