package com.cappuccino.aog.entities;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.mapeditor.EntityModel;
import com.cappuccino.aog.scene.GameScene;

public class Wall extends Entity{
	

	public Wall(World world) {
		this(world, 0, 0, 0, 1, 1);
	}
	
	public Wall(World world, EntityModel model) {
		this(world, model.position.x, model.position.y, model.angle, model.scale.x, model.scale.y);
	}
	
	public Wall(World world, float x, float y, float angle, float scaleX, float scaleY) {
		super("Wall", world);
		initBody(world, BodyType.StaticBody);
		setScaleX(scaleX);
		setScaleY(scaleY);
		initFixtures();
		
		setCenter(x, y);
		setAngle(angle);
	}
	
	@Override
	public void disactive() {
		body.setActive(false);
	}
	
	@Override
	public void active() {
		body.setActive(true);
	}
	
	
	public void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;
		
		bodyLoader.attachFixture(body, "Wall", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
	
		origin.set(bodyLoader.getOrigin("Wall", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}
	
	@Override
	public void recalculate() {
		reloadFixtures();
	}

}
