package com.cappuccino.aog;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.cappuccino.aog.scene.MainMenuScene;

public class AOGGame extends ApplicationAdapter {
	
	private static Scene currentScene;
	private static FPSLogger fpsLogger;
	private static final Color clearColor = new Color();
	
	@Override
	public void create() {
		Assets.load();
		fpsLogger = new FPSLogger();
		currentScene = new MainMenuScene();
	}
	
	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		
		fpsLogger.log();
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		currentScene.update(dt);
		currentScene.render(dt);
	}
	
	@Override
	public void resize(int width, int height) {
		currentScene.resize(width, height);
	}
	
	@Override
	public void dispose() {
		currentScene.dispose();
		Assets.dispose();
	}
	
	
	public static void changeScene(Scene newScene){
		currentScene.dispose();
		currentScene = newScene;
	}
	
	public static void setClearColor(float r, float g, float b, float a){
		clearColor.set(r,g,b,a);
	}
}
