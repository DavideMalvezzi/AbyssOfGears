package com.cappuccino.aog.scene;


import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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


public class GameScene extends Scene{
	
	private ParallaxLayer parallaxbg0, parallaxbg1, parallaxbg2;
	private Level level;
	private Alexy player;
	private GameSceneHud hud;
	
	
	public GameScene(){
		super();
		
		parallaxbg0 = new ParallaxLayer(Assets.layer0Background, 160);
		parallaxbg1 = new ParallaxLayer(Assets.layer1Background, 160);
		parallaxbg2 = new ParallaxLayer(Assets.layer2Background, 160);
		
		level = LevelManager.load(world, 0);
		player = Level.getPlayer();
		
		hud = new GameSceneHud();
		
		PointLight l = new PointLight(rayHandler, 32, new Color(1f, 0.45f, 0f, 0.75f), 20, 0, 0);
		l.attachToBody(player.getBody(), 0, -2);
		PointLight.setContactFilter(Entity.LIGHT, (short) 0, Entity.LIGHT_MASK);
	}
	
	public void render(float delta){
		beginDraw();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
		
				parallaxbg0.render(batch, camera);
				parallaxbg1.render(batch, camera);
				parallaxbg2.render(batch, camera);
				level.render(batch);
				
			batch.end();
		
			camera.combined.scl(WORLD_TO_BOX);
			camera.projection.scl(WORLD_TO_BOX);
			//box2dDebug.render(world, camera.combined);
			rayHandler.setCombinedMatrix(camera.combined);
			//rayHandler.render();
			
			hud.draw();
			
		endDraw();
		
	}
	
	public void update(float delta){
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		if(!hud.isPaused()){
			if(player.getState()!=Status.DYING){
				camera.position.y = player.getCenter().y*GameScene.BOX_TO_WORLD;
				//parallaxbg0.update(delta, -player.getBody().getLinearVelocity().y*0.15f*BOX_TO_WORLD);
				//parallaxbg1.update(delta, -player.getBody().getLinearVelocity().y*0.3f*BOX_TO_WORLD);
				//parallaxbg2.update(delta, -player.getBody().getLinearVelocity().y*0.6f*BOX_TO_WORLD);
			}else{
				hud.showGameOverMenu();
			}
			
			level.update(delta);
			world.step(1/60f, 8, 3);
			rayHandler.update();
		}
		
		hud.act(delta);
		
		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
		
		
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
