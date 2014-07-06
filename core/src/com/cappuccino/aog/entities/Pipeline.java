package com.cappuccino.aog.entities;

import com.badlogic.gdx.physics.box2d.World;

public class Pipeline extends Entity{

	public static enum PipePiece{
		HORIZONTAL, VERTICAL, ANGLE_DOWN, ANGLE_UP, ANGLE_LEFT, ANGLE_RIGHT;
	}
	
	public Pipeline( World world, float x, float y, int...route) {
		super("Pipeline", world);
		// TODO Auto-generated constructor stub
	}

}
