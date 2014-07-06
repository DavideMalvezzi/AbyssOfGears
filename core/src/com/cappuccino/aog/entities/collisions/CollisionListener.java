package com.cappuccino.aog.entities.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.cappuccino.aog.entities.Alexy;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.levels.Level;

public interface CollisionListener {
	final Alexy alexy = Level.getPlayer();
	public void onCollision(Entity sender, Entity collidedEntity, Contact contact, ContactImpulse impulse);
	
}
