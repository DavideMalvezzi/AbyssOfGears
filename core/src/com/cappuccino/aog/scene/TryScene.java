package com.cappuccino.aog.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.Thunder;


public class TryScene extends Scene{

	private Thunder t1, t2;
	
	public TryScene() {
		camera.position.y = camera.viewportHeight/2;
		//t1 = new Thunder(world, 100, 100, 100, 0, 1);
		
	}
	
	@Override
	public void render(float delta) {
		beginClip();
		 
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
			batch.setColor(Color.WHITE);
			batch.draw(Assets.layer0Background, 0, 0);
			
			t1.draw(batch);
		//	t2.draw(batch, camera);
			
		batch.end();
		
		
		
			
			
		endClip();
	}
	
	
	@Override
	public void update(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		
		t1.update(delta);

		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
		
	}
	
	
	
	
	
}
