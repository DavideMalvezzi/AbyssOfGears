package com.cappuccino.aog.entities;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.Scene;

public class Gear extends Entity{

	public Gear(World world, int type, float x, float y, float vel, float scale) {
		super("Gear"+type, world);
		
		setScaleX(scale);
		setScaleY(scale);
		
		init(world, BodyType.KinematicBody);
		initFixture();
		
		
		setCenter(x, y);
		setAngularVelocity(vel);
	}
	
	@Override
	protected void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = ENTITY_MASK;
		bodyLoader.attachFixture(body, texture.name, fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin(texture.name, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
	}

	
	
}
