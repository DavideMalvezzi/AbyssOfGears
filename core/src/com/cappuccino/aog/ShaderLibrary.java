package com.cappuccino.aog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class ShaderLibrary {

	public static final ShaderProgram softLight = new ShaderProgram(Gdx.files.internal("data/shader/default.vert"), Gdx.files.internal("data/shader/soft_light.frag"));
	public static final ShaderProgram gaussianBlur = new ShaderProgram(Gdx.files.internal("data/shader/default.vert"), Gdx.files.internal("data/shader/gaussian_blur.frag"));
	public static final ShaderProgram bloom = new ShaderProgram(Gdx.files.internal("data/shader/default.vert"), Gdx.files.internal("data/shader/bloom.frag"));
	
	
	public static void setSoftLightParams(Color color){
		softLight.setUniformf("mixColor", color);
	}
	
	public static void setBloomParams(float pixelSizeX, float pixelSizeY, float[] values, float gloomFactor){
		bloom.setUniformf("pixelSize", new Vector2(pixelSizeX, pixelSizeY));
		bloom.setUniform1fv("values", values, 0, 9);
		bloom.setUniformf("gloomFactor", gloomFactor);
	}
	
	public static void setBlurParam(float pixelSizeX, float pixelSizeY, int radius){
		float sum = 0;
		
		float[] weights = new float[radius/2+1];
		
		sum = weights[0] = computeGaussian(0, radius/3f);
		
		
		for(int i=1; i<radius/2; i++){
			float w = computeGaussian(i, radius/3f);
			weights[i] = w;
		
			sum+=w*2;
		}
		
		for(int i=0; i<radius/2; i++){
			weights[i]/=sum;
		}
		
		gaussianBlur.setUniformi("samples_count", radius);
		gaussianBlur.setUniform1fv("weight", weights, 0, radius/2);
		gaussianBlur.setUniformf("pixelSize", new Vector2(pixelSizeX*1.5f, pixelSizeY*1.5f));
		
	}
	
	
	private static float computeGaussian(float n, float theta){
        return (float)((1.0 / Math.sqrt(2 * Math.PI * theta)) * Math.exp(-(n * n) / (2 * theta * theta)));
    }
}
