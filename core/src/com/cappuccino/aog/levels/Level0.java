package com.cappuccino.aog.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;


public class Level0 extends Level {

	@Override
	public void init(World world, boolean usePlayer) {
		super.init(world, false);
		
		/*
		ThunderEmitter te = new ThunderEmitter(world, 60, 600, 30*MathUtils.degRad);
		ThunderEmitter te2 = new ThunderEmitter(world, 565, 600, 180*MathUtils.degRad);
		ThunderEmitter te3 = new ThunderEmitter(world, 565, 700, 180*MathUtils.degRad);
		te.setEnd(te3, 2);
		te2.setEnd(te, 2);
		
		
		active_entities.add(te2);
		active_entities.add(te);
		active_entities.add(te3);
		*/
		
		//TODO: Spear,press,chain
		
		/*
		
		Wall wallL = new Wall("lvl0wallLeft", world, 0, 0);
		Wall wallR = new Wall("lvl0wallRight", world, 504, 0);
		active_entities.add(wallL);
		active_entities.add(wallR);
		
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

	@Override
	public String getLevelName() {
		return "Level0";
	}



	
}
