package com.cappuccino.aog;

import com.cappuccino.aog.entities.Entity;


public interface State<A extends Entity> {

	public void onEnter(A entity);
	public void update(float dt, A entity);
	public void onExit(A entity);
	
}
