package com.cappuccino.aog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class BatchUtils {

	private static final ShaderProgram bloom_shader =  new ShaderProgram(Gdx.files.internal("data/bloom_shader.vert"), Gdx.files.internal("data/bloom_shader.frag"));
	private static final FrameBuffer blur_buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	
	
	
	public static void drawBloommed(SpriteBatch batch, Texture texture, float x, float y, float bloomFactor){
		batch.end();
		batch.setShader(bloom_shader);
		blur_buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					batch.begin();
						bloom_shader.setUniformf("pixelSize", 1f/texture.getWidth(), 1f/texture.getHeight());
						bloom_shader.setUniformf("gloomFactor", bloomFactor);
						bloom_shader.setUniformi("isVertical", 1);
						batch.draw(texture, x, y);
					batch.end();
		blur_buffer.end();
		
		batch.begin();
			bloom_shader.setUniformi("isVertical", -1);
			batch.draw(blur_buffer.getColorBufferTexture(), x+Scene.CAM_TRASL_X, y, (int)x, (int)y, texture.getWidth(), texture.getHeight());
			batch.setShader(null);
		
	}
	
	
	
	
}
