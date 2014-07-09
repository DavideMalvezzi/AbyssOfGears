package com.cappuccino.aog.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.EntityData;

public class ContactEvent implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		EntityData dataA = (EntityData) contact.getFixtureA().getBody().getUserData();
		Entity entityA = dataA.getEntity();
		EntityData dataB = (EntityData) contact.getFixtureB().getBody().getUserData();
		Entity entityB = dataB.getEntity();
		
		if(dataB.getName().equals("Umbrella") || dataB.getName().equals("Player")){
			entityA.onCollide(contact.getFixtureA(), contact.getFixtureB(), contact);
		}else if(dataA.getName().equals("Umbrella") || dataA.getName().equals("Player")){
			entityB.onCollide(contact.getFixtureB(), contact.getFixtureA(), contact);
		}else{
			if(entityA.getBody().getType() == BodyType.DynamicBody){
				entityA.onCollide(contact.getFixtureA(), contact.getFixtureB(), contact);
			}else if(entityB.getBody().getType() == BodyType.DynamicBody){
				entityB.onCollide(contact.getFixtureB(), contact.getFixtureA(), contact);
			}
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	
	}

}
