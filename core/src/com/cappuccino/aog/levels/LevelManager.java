package com.cappuccino.aog.levels;

import com.badlogic.gdx.physics.box2d.World;

public class LevelManager {
	
	private static final Level0 lvl0 = new Level0();
	
	public static Level load(World world, int level){
		Level currentLevel;

		switch (level) {
			
			default:
				currentLevel = lvl0;
		}
		currentLevel.init(world, true);
		return currentLevel;
	}
	
}
