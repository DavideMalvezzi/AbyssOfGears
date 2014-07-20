package com.cappuccino.aog.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cappuccino.aog.Scene;

public class VParallaxLayer {
	
	private float posY;
	private Texture texture;
	
	
	public VParallaxLayer(Texture texture){
		this.texture = texture;
		this.texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
	

	public void render(SpriteBatch batch, OrthographicCamera camera){
		batch.setProjectionMatrix(camera.projection);
		
		batch.draw(texture, -texture.getWidth()/2, 0, 
					0, (int)(posY), 
					texture.getWidth(), (int) Math.round(camera.viewportHeight*Scene.WORLD_TO_BOX));
		
		batch.setProjectionMatrix(camera.combined);
	}
	
	public void update(float delta, float velY){
		posY += velY;
		posY %=	texture.getHeight();
	}
	
	
}
