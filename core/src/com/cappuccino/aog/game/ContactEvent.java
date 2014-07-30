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
		
		BodyType entABodyType = contact.getFixtureA().getBody().getType();
		BodyType entBBodyType = contact.getFixtureB().getBody().getType();
		
		
		if(dataB.getName().equals("Umbrella") || dataB.getName().equals("Player")){
			if(entABodyType == BodyType.StaticBody){
				entityB.onStartCollide(contact.getFixtureB(), contact.getFixtureA(), contact);
			}else{
				entityA.onStartCollide(contact.getFixtureA(), contact.getFixtureB(), contact);
			}
			
		}else if(dataA.getName().equals("Umbrella") || dataA.getName().equals("Player")){
			if(entBBodyType == BodyType.StaticBody){
				entityA.onStartCollide(contact.getFixtureA(), contact.getFixtureB(), contact);
			}else{
				entityB.onStartCollide(contact.getFixtureB(), contact.getFixtureA(), contact);
			}
			
		}else{
			if(entABodyType == BodyType.DynamicBody){
				entityA.onStartCollide(contact.getFixtureA(), contact.getFixtureB(), contact);
			}else if(entBBodyType == BodyType.DynamicBody){
				entityB.onStartCollide(contact.getFixtureB(), contact.getFixtureA(), contact);
			}
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		EntityData dataA = (EntityData) contact.getFixtureA().getBody().getUserData();
		Entity entityA = dataA.getEntity();
		EntityData dataB = (EntityData) contact.getFixtureB().getBody().getUserData();
		Entity entityB = dataB.getEntity();
		
		BodyType entABodyType = contact.getFixtureA().getBody().getType();
		BodyType entBBodyType = contact.getFixtureB().getBody().getType();
		
		
		if(dataB.getName().equals("Umbrella") || dataB.getName().equals("Player")){
			if(entABodyType == BodyType.StaticBody){
				entityB.onEndCollide(contact.getFixtureB(), contact.getFixtureA(), contact);
			}
			
		}else if(dataA.getName().equals("Umbrella") || dataA.getName().equals("Player")){
			if(entBBodyType == BodyType.StaticBody){
				entityA.onEndCollide(contact.getFixtureA(), contact.getFixtureB(), contact);
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	
	}

}
