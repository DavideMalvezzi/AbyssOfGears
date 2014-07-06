package com.cappuccino.aog.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.GasEmitter;
import com.cappuccino.aog.entities.SpikedBall;
import com.cappuccino.aog.entities.Wall;

public class Level0 extends Level {

	@Override
	public void init(World world, boolean usePlayer) {
		super.init(world, usePlayer);
		
		SpikedBall spike = new SpikedBall(world, 600, 600, 2, 1);
		GasEmitter smokeEmitter = new GasEmitter(world, 70, 620, 0*MathUtils.degRad);
		//SmokeEmitter smokeEmitter2 = new SmokeEmitter(world, 70, 620, 45*MathUtils.degRad);
		/*
		Press press = new Press(world, 0, 400, 50, 435, 10, -45*MathUtils.degRad, 0.45f);
		Press press2 = new Press(world, 640, -240, 50, 435, 10, 135*MathUtils.degRad, 0.45f);
		*/
		
		
		/*
		Spear spear4 = new Spear(world, 0, 560, 300, 30, -20*MathUtils.degRad, 1);
		Spear spear3 = new Spear(world, 0, 580, 300, 30, -10*MathUtils.degRad, 1);
		Spear spear = new Spear(world, 0, 600, 300, 30, 0*MathUtils.degRad, 1);
		Spear spear1 = new Spear(world, 0, 620, 300, 30, 10*MathUtils.degRad, 1);
		Spear spear2 = new Spear(world, 0, 640, 300, 30, 20*MathUtils.degRad, 1);
		*/
	
		Wall wallL = new Wall("lvl0wallLeft", world, 0, 0);
		Wall wallR = new Wall("lvl0wallRight", world, 504, 0);
		
		
		active_entities.add(wallL);
		active_entities.add(wallR);
		active_entities.add(spike);
		active_entities.add(smokeEmitter);
		//active_entities.add(smokeEmitter2);
		//active_entities.add(press);
		//active_entities.add(press2);
		/*
		active_entities.add(spear);
		active_entities.add(spear1);
		active_entities.add(spear2);
		active_entities.add(spear4);
		active_entities.add(spear3);
		*/
	}
	
	@Override
	public void render(SpriteBatch batch) {
		renderEntities(batch);
	}

	@Override
	public void update(float delta) {
		updateEntities(delta);
	}

	@Override
	public Vector2 getSpawnPoint() {
		return new Vector2(Scene.SCENE_H/2, 1024);
	}



	
}
