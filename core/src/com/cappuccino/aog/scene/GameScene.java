package com.cappuccino.aog.scene;


import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.Alexy;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.game.ParallaxLayer;
import com.cappuccino.aog.levels.Level;
import com.cappuccino.aog.levels.LevelManager;
import com.cappuccino.aog.mapeditor.MapEditingInputListener;


public class GameScene extends Scene{
	
	private ParallaxLayer parallaxbg0, parallaxbg1, parallaxbg2;
	private Level level;
	private Alexy player;
	private GameSceneHud hud;
	
	
	private MapEditingInputListener mapEditor;
	
	public GameScene(){
		super();
		parallaxbg0 = new ParallaxLayer(Assets.layer0Background, -CAM_TRASL_X);
		parallaxbg1 = new ParallaxLayer(Assets.layer1Background, -CAM_TRASL_X);
		parallaxbg2 = new ParallaxLayer(Assets.layer2Background, -CAM_TRASL_X);
		
		level = LevelManager.load(world, 0);
		player = Level.getPlayer();
		
		hud = new GameSceneHud();
		
		PointLight l = new PointLight(rayHandler, 32, new Color(1f, 0.45f, 0f, 0.75f), 20, 0, 0);
		l.attachToBody(player.getBody(), 0, -2);
		PointLight.setContactFilter(Entity.LIGHT, (short) 0, Entity.LIGHT_MASK);
		
		mapEditor = new MapEditingInputListener(level, world, camera);
		Gdx.input.setInputProcessor(mapEditor);
	}
	
	public void render(float delta){
		beginClip();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
		
				parallaxbg0.render(batch, camera);
				parallaxbg1.render(batch, camera);
				parallaxbg2.render(batch, camera);
				level.render(batch);
				
			batch.end();
		
			camera.combined.scl(WORLD_TO_BOX);
			camera.projection.scl(WORLD_TO_BOX);
			if(mapEditor.isDebugging()){
				box2dDebug.render(world, camera.combined);
			}
			rayHandler.setCombinedMatrix(camera.combined);
			//rayHandler.render();
			
			hud.draw();
			
			mapEditor.render(batch);
			
		endClip();
		
	}
	
	public void update(float delta){
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		if(!hud.isPaused()){
			if(player.getState()!=Status.DYING){
				//camera.position.y = player.getCenter().y*GameScene.BOX_TO_WORLD;
				parallaxbg0.update(delta, -player.getBody().getLinearVelocity().y*0.15f*BOX_TO_WORLD);
				parallaxbg1.update(delta, -player.getBody().getLinearVelocity().y*0.3f*BOX_TO_WORLD);
				parallaxbg2.update(delta, -player.getBody().getLinearVelocity().y*0.6f*BOX_TO_WORLD);
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
	
	
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		((ExtendViewport)hud.getViewport()).update(width, height);
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
