package com.cappuccino.aog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderLibrary {

	public static final ShaderProgram softLight = new ShaderProgram(Gdx.files.internal("data/shader/default.vert"), Gdx.files.internal("data/shader/soft_light.frag"));
	
}
