package com.cappuccino.aog.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cappuccino.aog.Scene;

public class ParallaxLayer {
	
	private float posY, offX;
	private Texture texture;
	
	public ParallaxLayer(Texture texture, float offX){
		this.texture = texture;
		this.texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		this.offX = offX;
	}

	public void render(SpriteBatch batch, OrthographicCamera camera){
		batch.setProjectionMatrix(camera.projection);
		
		
			batch.draw(texture, offX, 0, 
					0, (int)posY, 
					texture.getWidth(), (int)(camera.viewportHeight*Scene.WORLD_TO_BOX));
			
		
		batch.setProjectionMatrix(camera.combined);
	}
	
	public void update(float delta, float velY){
		posY += velY;
		posY %=	texture.getHeight();
	}
	
	
}
