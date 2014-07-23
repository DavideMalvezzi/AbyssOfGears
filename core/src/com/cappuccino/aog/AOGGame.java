package com.cappuccino.aog;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.cappuccino.aog.scene.GameScene;
import com.cappuccino.aog.scene.MainMenuScene;
import com.cappuccino.aog.scene.TryScene;

public class AOGGame extends ApplicationAdapter {
	
	private static Scene currentScene;
	private static FPSLogger fpsLogger;
	
	@Override
	public void create() {
		Assets.load();
		
		fpsLogger = new FPSLogger();
		currentScene = new MainMenuScene();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		
		fpsLogger.log();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
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
}
