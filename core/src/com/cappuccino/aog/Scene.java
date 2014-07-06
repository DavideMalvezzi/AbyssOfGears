package com.cappuccino.aog;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cappuccino.aog.game.ContactEvent;

public class Scene extends ScreenAdapter {
	public static final int SCENE_W = 960, SCENE_H = 640;
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
		viewport = new ExtendViewport(SCENE_W*BOX_TO_WORLD, SCENE_H*BOX_TO_WORLD, camera);
		//Accentramento telecamera rispetto alle immagini
		camera.position.set(SCENE_W/2f*BOX_TO_WORLD*ZOOM, SCENE_H/2f*BOX_TO_WORLD*ZOOM, 0);
		camera.translate(-(SCENE_W-SCENE_H/ZOOM)/2*ZOOM*BOX_TO_WORLD, 0);
		camera.zoom = ZOOM;
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		batch = new SpriteBatch();
		
		world = new World(new Vector2(0, -1f), true);
		world.setContactListener(new ContactEvent());

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0, 0, 0, 0f);
		
		box2dDebug = new Box2DDebugRenderer(true,true,false,true,true,true);
	}
	
	protected void beginDraw(){
		Rectangle view = new Rectangle(
				camera.position.x-camera.viewportWidth/2f, 
				camera.position.y-camera.viewportHeight/2f, 
				camera.viewportWidth, camera.viewportHeight+15*BOX_TO_WORLD);
		
		Rectangle scissor = new Rectangle();
		Scissor.calculateScissors(camera, batch.getTransformMatrix(), view, scissor);
		Scissor.setArea(view, scissor);
		
		camera.combined.scl(BOX_TO_WORLD);
		camera.projection.scl(BOX_TO_WORLD);
		
	}
	
	protected void endDraw(){
		Scissor.popScissors();
	}
	
	public void init(){}
	public void update(float delta){}
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
}
