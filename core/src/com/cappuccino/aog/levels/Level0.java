package com.cappuccino.aog.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;


public class Level0 extends Level {

	@Override
	public void init(World world, boolean usePlayer) {
		super.init(world, usePlayer);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		renderEntities(batch);
		renderWalls(batch);
	}

	@Override
	public void update(float delta) {
		updateEntities(delta);
	}

	@Override
	public Vector2 getSpawnPoint() {
		return new Vector2(Scene.SCENE_H/2, 1280);
	}

	@Override
	public String getLevelName() {
		return "Level0";
	}



	
}
