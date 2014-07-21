package com.cappuccino.aog;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cappuccino.aog.game.ContactEvent;

public class Scene extends ScreenAdapter {
	public static final int SCENE_W = 1280, SCENE_H = 720;
	public static final int WIN_W = Gdx.graphics.getWidth(), WIN_H =  Gdx.graphics.getHeight();
	public static final float BOX_TO_WORLD = 0.04f, WORLD_TO_BOX = 25f, ZOOM = 1f; 
	
	protected OrthographicCamera camera;
	protected ExtendViewport viewport;
	protected SpriteBatch batch;
	protected World world;
	protected RayHandler rayHandler;
	protected Box2DDebugRenderer box2dDebug;
	
	public Scene() {
		camera = new OrthographicCamera(SCENE_W*BOX_TO_WORLD, SCENE_H*BOX_TO_WORLD);
		camera.position.set(SCENE_W/2*BOX_TO_WORLD, SCENE_H/2*BOX_TO_WORLD, 0);
		viewport = new ExtendViewport(SCENE_W*BOX_TO_WORLD, SCENE_H*BOX_TO_WORLD, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		camera.translate((viewport.getWorldWidth()-SCENE_W/2)/2*BOX_TO_WORLD, 0);
		camera.zoom = SCENE_H / (viewport.getWorldHeight()*WORLD_TO_BOX);
		camera.update();
		
		batch = new SpriteBatch();
		
		world = new World(new Vector2(0, -1f), true);
		world.setContactListener(new ContactEvent());

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0, 0, 0, 0.3f);
		
		box2dDebug = new Box2DDebugRenderer(true,true,false,true,true,true);
	}
	
	protected void beginClip(){
		Scissor.setClip(camera, 0, 0, SCENE_W, SCENE_H);
		
		camera.combined.scl(BOX_TO_WORLD);
		camera.projection.translate(0, -camera.viewportHeight/2, 0);
		camera.projection.scl(BOX_TO_WORLD);
		
	}
	
	protected void endClip(){
		Scissor.popScissors();
	}
	
	
	public void update(float delta){}
	
	
}
