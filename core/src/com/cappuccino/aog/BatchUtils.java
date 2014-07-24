package com.cappuccino.aog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;

public class BatchUtils {

	private static FrameBuffer blur_buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	
	public static final float[] gaussian_values = new float[]{
				0.05f,
				0.09f,
				0.11f,
				0.15f,
				0.2f,
				0.15f,
				0.11f,
				0.09f,
				0.05f
			};
	
	
	public static void drawBloommedOnBuffer(FrameBuffer buffer, Texture texture, SpriteBatch batch, float bloomFactor){
		Matrix4 old = batch.getProjectionMatrix().cpy();
		
		batch.flush();
		batch.setShader(ShaderLibrary.bloom);
		blur_buffer = new FrameBuffer(Format.RGBA8888, texture.getWidth(), texture.getHeight(), false);
		blur_buffer.begin();
			clearBuffer();
			batch.setProjectionMatrix(getBufferProj(blur_buffer));
			ShaderLibrary.setBloomParams(1f/texture.getWidth(), 0, gaussian_values, bloomFactor);
			batch.draw(texture, 0, 0);
			batch.flush();
		blur_buffer.end();
		
		buffer.begin();
			clearBuffer();
			batch.setProjectionMatrix(getBufferProj(buffer));
			ShaderLibrary.setBloomParams(0, 1f/texture.getHeight(), gaussian_values, bloomFactor);
			//Disegno solo porzione del buffer che contiene la texture
			batch.draw(blur_buffer.getColorBufferTexture(), 0, 0, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
			batch.flush();
		buffer.end();
		
		batch.setProjectionMatrix(old);
		batch.setShader(ShaderLibrary.softLight);
	}
	
	
	public static Matrix4 getBufferProj(FrameBuffer f){
		return new Matrix4().setToOrtho(0, f.getWidth(), 0, f.getHeight(), 0, 10);
	}
	
	public static void clearBuffer(){
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	
	
}
