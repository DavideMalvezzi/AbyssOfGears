package com.cappuccino.aog.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.scene.GameScene;

public class Umbrella extends Entity{
	
	private int dir = 1;
	
	public Umbrella(World world) {
		super("Umbrella", world);
		initBody(world, BodyType.DynamicBody);
		initFixtures();
		setMass(6);
		setAngle(0.1f);
	}
	
	public void update(float delta) {
		//energia potenziale pendolo mgL(1-cos(angle))
		
		if((getAngle()>0.085f && dir == 1) || (getAngle()<-0.085f && dir == -1)){
			dir*=-1;
		}	
		
		setAngularVelocity(getAngularVelocity()+getMass()*9.8f*(1-MathUtils.cos(getAngle()))*dir*delta);
	}


	@Override
	public void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = PLAYER;
		fd.filter.maskBits = PLAYER_MASK;
		
		bodyLoader.attachFixture(body, "Umbrella_top", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Umbrella_sensor", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		bodyLoader.attachFixture(body, "Umbrella_bottom", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("Umbrella_bottom", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}


	

}
