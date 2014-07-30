package com.cappuccino.aog;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

public class Scissor extends ScissorStack {

	private static final Rectangle area = new Rectangle();
	private static final Rectangle scissor = new Rectangle();
	private static int cameraDirY = 0;
	
	public static void setClip(OrthographicCamera camera, float x, float y, float w, float h){
		cameraDirY = (int)Math.signum(camera.position.y-camera.viewportHeight/2-area.y);
		area.set(camera.position.x - camera.viewportWidth/2, camera.position.y - camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);
		scissor.set(x, y, w, h);
		pushScissors(scissor);
	}
	
	public static int getCameraDirectionY(){
		return cameraDirY;
	}
	
	public static Rectangle getArea(){
		return area;
	}
}
