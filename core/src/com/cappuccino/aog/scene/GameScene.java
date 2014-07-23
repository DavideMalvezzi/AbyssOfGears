package com.cappuccino.aog.scene;


import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.ShaderLibrary;
import com.cappuccino.aog.entities.Alexy;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.game.VParallaxLayer;
import com.cappuccino.aog.levels.Level;
import com.cappuccino.aog.levels.LevelManager;
import com.cappuccino.aog.mapeditor.MapEditor;


public class GameScene extends Scene{
	
	private VParallaxLayer parallaxbg0, parallaxbg1;
	private Level level;
	private Alexy player;
	private GameSceneHud hud;
	
	private MapEditor mapEditor;
	
	public GameScene(){
		super();
		parallaxbg0 = new VParallaxLayer(Assets.layer0Background);
		parallaxbg1 = new VParallaxLayer(Assets.layer1Background);
		
		level = LevelManager.load(world, 0);
		player = Level.getPlayer();
		
		hud = new GameSceneHud(level);
		
		rayHandler.setAmbientLight(0, 0, 0, 0.05f);
		rayHandler.setBlurNum(100);
		
		PointLight l = new PointLight(rayHandler, 8, new Color(1f, 1, 0f, 0.7f), 15, 0, 0);
		l.attachToBody(player.getBody(), 0, -2);
		PointLight.setContactFilter(Entity.LIGHT, (short) 0, Entity.LIGHT_MASK);
		
		mapEditor = new MapEditor(level, world, camera);
	}
	
	public void render(float delta){
		beginClip();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
				ShaderLibrary.softLight.setUniformf("mixColor", level.getColor());
				parallaxbg0.render(batch, camera);
				parallaxbg1.render(batch, camera);
				level.render(batch);
				
			batch.end();
		
			camera.combined.scl(WORLD_TO_BOX);
			camera.projection.scl(WORLD_TO_BOX);
			if(mapEditor.isDebugging()){
				//box2dDebug.render(world, camera.combined);
			}
			rayHandler.setCombinedMatrix(camera.combined);
			//rayHandler.render();
			
			hud.draw();
			
			mapEditor.draw(batch);
			
		endClip();
		
	}
	
	public void update(float delta){
		camera.update();
		
		if(!hud.isPaused()){
			if(player.getState()!=Status.DYING){
				//camera.position.y = player.getCenter().y*GameScene.BOX_TO_WORLD;
				parallaxbg0.update(delta, -player.getBody().getLinearVelocity().y*0.15f*BOX_TO_WORLD);
				parallaxbg1.update(delta, -player.getBody().getLinearVelocity().y*0.3f*BOX_TO_WORLD);
			}else{
				hud.showGameOverMenu();
			}
			
			if(mapEditor.isDebugging()){
				level.update(delta);
				world.step(1/60f, 8, 3);
			}
			
			rayHandler.update();
		}
		
		hud.act(delta);
		
		mapEditor.update();
	}
	
	
	
	
	public void dispose(){
		batch.dispose();
		level.dispose();
		rayHandler.dispose();
		world.dispose();
	}

	
	@Override
	public void pause() {
		hud.showPauseMenu();
	}
	
	@Override
	public void resume() {
		hud.disposePauseMenu();
	}
	
	
}
