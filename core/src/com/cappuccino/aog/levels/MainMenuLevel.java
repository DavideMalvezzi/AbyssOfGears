package com.cappuccino.aog.levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MainMenuLevel extends Level {

	
	
	public MainMenuLevel(World world) {
		levelColor = new Color(0x007d92ff);
		init(world, false);
	}
	
	
	
	@Override
	public void render(SpriteBatch batch) {
		renderEntities(batch);
		renderWalls(batch);
	}

	@Override
	public void update(float delta) {
		updateEntities(delta);
		updateWalls(delta);
	}

	@Override
	public Vector2 getSpawnPoint() {
		return new Vector2(0,-200);
	}

	@Override
	public String getLevelName() {
		return "MainMenuLevel";
	}

}
