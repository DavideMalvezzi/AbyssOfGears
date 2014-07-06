package com.cappuccino.aog;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

public class Scissor extends ScissorStack {

	private static final Rectangle area = new Rectangle();
	
	public static void setArea(Rectangle area, Rectangle scissor){
		Scissor.area.set(area);
		pushScissors(scissor);
	}
	
	public static Rectangle getArea(){
		return area;
	}
}
