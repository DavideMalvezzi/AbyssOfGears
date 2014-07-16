package com.cappuccino.aog.entities;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.Scene;

public class ContetxMenuEntity extends Entity{

	
	public ContetxMenuEntity(World world) {
		super("ContexMenuBg", world);
		init(world, BodyType.DynamicBody);
		initFixture();
	}
	
	@Override
	protected void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.density = 0.05f;
		
		bodyLoader.attachFixture(body, "ContexMenuBg", fd, getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("ContexMenuBg", getRealWidth()*Scene.BOX_TO_WORLD, getRealWidth()*Scene.BOX_TO_WORLD));
		
		body.setGravityScale(9.8f);
		
	}

}
