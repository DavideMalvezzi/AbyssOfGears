package com.cappuccino.aog.scene;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.ExtendAndZoomViewport;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.game.ContactEvent;


public class TryScene2 extends Scene{

	private ExtendAndZoomViewport viewport;
	
	public TryScene2() {
		camera = new OrthographicCamera();
		camera.position.set(SCENE_W/2*BOX_TO_WORLD, SCENE_H/2*BOX_TO_WORLD, 0);
		viewport = new ExtendAndZoomViewport(SCENE_W*BOX_TO_WORLD, SCENE_H*BOX_TO_WORLD, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		batch = new SpriteBatch();
		
		world = new World(new Vector2(0, -1f), true);
		world.setContactListener(new ContactEvent());

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0, 0, 0, 0.3f);
		
		box2dDebug = new Box2DDebugRenderer(true,true,false,true,true,true);
		
		
	}
	
	
	
	
	public void render(float delta) {
		beginClip();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			batch.draw(Assets.layer0Background, 0, 0);
			batch.end();
		endClip();
	}
	
	
	
	
	
	public void update(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
		
	}
	
	
	
	
	
}
