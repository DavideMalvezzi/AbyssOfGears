package com.cappuccino.aog.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.Arrow;
import com.cappuccino.aog.entities.ArrowEmitter;
import com.cappuccino.aog.entities.GasEmitter;
import com.cappuccino.aog.entities.LaserEmitter;
import com.cappuccino.aog.entities.Press;
import com.cappuccino.aog.entities.SpikedBall;
import com.cappuccino.aog.entities.ThunderEmitter;
import com.cappuccino.aog.entities.Wall;

public class Level0 extends Level {

	@Override
	public void init(World world, boolean usePlayer) {
		super.init(world, usePlayer);
		/*
		SpikedBall spike = new SpikedBall(world, 600, 600, 2, 1);
		GasEmitter smokeEmitter = new GasEmitter(world, 70, 620, 0*MathUtils.degRad);
		//SmokeEmitter smokeEmitter2 = new SmokeEmitter(world, 70, 620, 45*MathUtils.degRad);
		
		
		Spear spear4 = new Spear(world, 0, 560, 300, 30, -20*MathUtils.degRad, 1);
		Spear spear3 = new Spear(world, 0, 580, 300, 30, -10*MathUtils.degRad, 1);
		Spear spear = new Spear(world, 0, 600, 300, 30, 0*MathUtils.degRad, 1);
		Spear spear1 = new Spear(world, 0, 620, 300, 30, 10*MathUtils.degRad, 1);
		Spear spear2 = new Spear(world, 0, 640, 300, 30, 20*MathUtils.degRad, 1);
	
		
		active_entities.add(spike);
		active_entities.add(smokeEmitter);
		//active_entities.add(smokeEmitter2);
		
		/*
		active_entities.add(spear);
		active_entities.add(spear1);
		active_entities.add(spear2);
		active_entities.add(spear4);
		active_entities.add(spear3);
		*/
		/*
		LaserEmitter l = new LaserEmitter(world, 90, 750, 15*MathUtils.degRad, 400, 2.5f);
		active_entities.add(l);
		LaserEmitter l2 = new LaserEmitter(world, 590, 750, 165*MathUtils.degRad, 400, 2.5f);
		active_entities.add(l2);
		*/
		/*
		ArrowEmitter a = new ArrowEmitter(world, 90, 750, 20*MathUtils.degRad, 3);
		active_entities.add(a);
		*/
		
		/*
		Press press = new Press(world, 0, 800, 50, 320, 10, -15*MathUtils.degRad, 0.45f);
		Press press2 = new Press(world, 640, 630, 50, 320, 10, 165*MathUtils.degRad, 0.45f);
		press.setOtherPress(press2);
		active_entities.add(press);
		active_entities.add(press2);
		*/
		
		ThunderEmitter te = new ThunderEmitter(world, 60, 600, 30*MathUtils.degRad);
		ThunderEmitter te2 = new ThunderEmitter(world, 565, 600, 180*MathUtils.degRad);
		ThunderEmitter te3 = new ThunderEmitter(world, 565, 700, 180*MathUtils.degRad);
		
		
		te.setEnd(te3);
		
		active_entities.add(te2);
		active_entities.add(te);
		active_entities.add(te3);
		
		
		Wall wallL = new Wall("lvl0wallLeft", world, 0, 0);
		Wall wallR = new Wall("lvl0wallRight", world, 504, 0);
		active_entities.add(wallL);
		active_entities.add(wallR);
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
