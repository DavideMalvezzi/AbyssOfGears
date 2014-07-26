package com.cappuccino.aog.entities;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.Scene;

public class ActorEntities {

	
public static class ContetxMenuEntity extends Entity{
		
		public ContetxMenuEntity(World world) {
			super("ContexMenuBg", world);
			initBody(world, BodyType.DynamicBody);
			initFixtures();
			body.setFixedRotation(true);
		}
		
		@Override
		protected void initFixtures() {
			FixtureDef fd = new FixtureDef();
			fd.density = 0.03f;
			fd.filter.categoryBits = ENTITY;
			fd.filter.maskBits = ENTITY_MASK;
			
			bodyLoader.attachFixture(body, "ContexMenuBg", fd, getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD);
			origin.set(bodyLoader.getOrigin("ContexMenuBg", getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD));
			body.setGravityScale(9.8f);
		}
	}
	
	public static class TitleEntity extends Entity{
	
		public TitleEntity(World world) {
			super("Title", world);
			initBody(world, BodyType.DynamicBody);
			initFixtures();
			body.setFixedRotation(true);
		}
	
		@Override
		protected void initFixtures() {
			FixtureDef fd = new FixtureDef();
			fd.density = 0.015f;
			fd.filter.categoryBits = ENTITY;
			fd.filter.maskBits = ENTITY_MASK;
			
			bodyLoader.attachFixture(body, "Title", fd, getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD);
			origin.set(bodyLoader.getOrigin("Title", getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD));
			body.setGravityScale(9.8f);
		}
	}
	
	public static class PlayEntity extends Entity{
		
		public PlayEntity(World world) {
			super("Play", world);
			initBody(world, BodyType.DynamicBody);
			initFixtures();
			body.setFixedRotation(true);
		}
	
		@Override
		protected void initFixtures() {
			FixtureDef fd = new FixtureDef();
			fd.density = 0.015f;
			fd.filter.categoryBits = ENTITY;
			fd.filter.maskBits = ENTITY_MASK;
			
			bodyLoader.attachFixture(body, "Play", fd, getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD);
			origin.set(bodyLoader.getOrigin("Play", getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD));
			body.setGravityScale(9.8f);
		}
	}


}
