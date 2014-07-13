package com.cappuccino.aog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class BatchUtils {

	private static final ShaderProgram bloom_shader = new ShaderProgram(Gdx.files.internal("data/bloom_shader.vert"), Gdx.files.internal("data/gaussian_blur.frag"));
	private static  FrameBuffer blur_buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	private static final SpriteBatch buffer_batch = new SpriteBatch(1);
	
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
	
	
	public static void drawBloommed(SpriteBatch batch, Texture texture, float x, float y, float ox, float oy, float angle, float bloomFactor){
		batch.end();
		/*
		blur_buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 0, 0, 1);
			buffer_batch.setShader(bloom_shaderV);
			buffer_batch.setProjectionMatrix(projection);
			buffer_batch.begin();
						bloom_shaderV.setUniform1fv("values", gaussian_values, 0, 9);
						bloom_shaderV.setUniformf("pixelSize", 1f/texture.getWidth(), 1f/texture.getHeight());
						bloom_shaderV.setUniformf("gloomFactor", bloomFactor);
						buffer_batch.draw(texture, 0, 0);
			buffer_batch.end();
						
		blur_buffer.end();
		*/
		
		batch.setShader(bloom_shader);
		batch.begin();
			bloom_shader.setUniform1fv("values", gaussian_values, 0, 9);
			bloom_shader.setUniformf("pixelSize", 1f/texture.getWidth(), 1f/texture.getHeight());
			bloom_shader.setUniformf("gloomFactor", bloomFactor);
			batch.draw(texture, x, y, 
					ox, oy, texture.getWidth(), texture.getHeight(), 
					1,1, angle*MathUtils.radDeg, 
					0, 0, texture.getWidth(), texture.getHeight(),
					false, true);
		batch.setShader(null);
	}
	
	public static void drawBloommedOnBuffer(FrameBuffer buffer, Texture texture, SpriteBatch batch, float bloomFactor){
		Matrix4 old = batch.getProjectionMatrix().cpy();
		
		batch.flush();
		batch.setShader(bloom_shader);
		blur_buffer = new FrameBuffer(Format.RGBA8888, texture.getWidth(), texture.getHeight(), false);
		blur_buffer.begin();
			clear();
			batch.setProjectionMatrix(getBufferProj(blur_buffer));
			bloom_shader.setUniformf("pixelSize", new Vector2(1.0f/texture.getWidth(), 0));
			bloom_shader.setUniform1fv("values", BatchUtils.gaussian_values, 0, 9);
			bloom_shader.setUniformf("gloomFactor", 1.65f);
			batch.draw(texture, 0, 0);
			batch.flush();
		blur_buffer.end();
		
		buffer.begin();
			clear();
			batch.setProjectionMatrix(getBufferProj(buffer));
			bloom_shader.setUniformf("pixelSize", new Vector2(0, 1.0f/texture.getHeight()));
			bloom_shader.setUniform1fv("values", BatchUtils.gaussian_values, 0, 9);
			bloom_shader.setUniformf("gloomFactor", 1.65f);
			batch.draw(blur_buffer.getColorBufferTexture(), 0, 0, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
			batch.flush();
		buffer.end();
		
		batch.setProjectionMatrix(old);
		batch.setShader(null);
	}
	
	/*
	public static void drawBloommed(SpriteBatch batch, Texture texture, float x, float y, float bloomFactor){
		batch.end();
		buffer_batch.setShader(bloom_shader);
		blur_buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 0, 0, 1);
						
			buffer_batch.begin();
						bloom_shader.setUniformf("pixelSize", 1f/texture.getWidth(), 1f/texture.getHeight());
						bloom_shader.setUniformf("gloomFactor", bloomFactor);
						bloom_shader.setUniformi("isVertical", 1);
						buffer_batch.draw(texture, 0, 0);
			buffer_batch.end();
						
		blur_buffer.end();
		
		batch.setShader(bloom_shader);
		batch.begin();
			bloom_shader.setUniformi("isVertical", -1);
			batch.draw(blur_buffer.getColorBufferTexture(), x, y, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), false, true);
		batch.setShader(null);
	}
	
	
	public static void drawBlurV(SpriteBatch batch, Texture texture, float x, float y, float bloomFactor){
		batch.end();
		buffer_batch.setShader(bloom_shader);
		blur_buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 0, 0, 1);
						
			buffer_batch.begin();
						bloom_shader.setUniformf("pixelSize", 1f/texture.getWidth(), 1f/texture.getHeight());
						bloom_shader.setUniformf("gloomFactor", bloomFactor);
						bloom_shader.setUniformi("isVertical", 1);
						buffer_batch.draw(texture, 0, 0);
			buffer_batch.end();
						
		blur_buffer.end();
		
		batch.begin();
			batch.draw(blur_buffer.getColorBufferTexture(), x, y, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), false, true);
	}
	
	public static void drawBlurH(SpriteBatch batch, Texture texture, float x, float y, float bloomFactor){
		batch.end();
		buffer_batch.setShader(bloom_shader);
		blur_buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 0, 0, 1);
						
			buffer_batch.begin();
						bloom_shader.setUniformf("pixelSize", 1f/texture.getWidth(), 1f/texture.getHeight());
						bloom_shader.setUniformf("gloomFactor", bloomFactor);
						bloom_shader.setUniformi("isVertical", -1);
						buffer_batch.draw(texture, 0, 0);
			buffer_batch.end();
						
		blur_buffer.end();
		
		batch.begin();
			batch.draw(blur_buffer.getColorBufferTexture(), x, y, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), false, true);
	}
	*/
	
	
	public static Matrix4 getBufferProj(FrameBuffer f){
		return new Matrix4().setToOrtho(0, f.getWidth(), 0, f.getHeight(), 0, 10);
	}
	
	private static void clear(){
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 0, 0, 1);
	}
	
	
	
}
