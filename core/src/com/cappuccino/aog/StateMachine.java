package com.cappuccino.aog;

import com.cappuccino.aog.entities.Entity;

public class StateMachine<A extends Entity> {
	
	private State<A> currentState;
	private Entity entity;
	
	public StateMachine(Entity entity, State<A> state){
		this.entity = entity;
		this.currentState = state;
		this.currentState.onEnter();
	}
	
	
    @SuppressWarnings("unchecked")
	public void update(float dt){
    	currentState.update(dt, (A) entity);
    }
    
    public void changeState(State<A> newState){
    	currentState.onExit();
    	currentState = newState;
    	currentState.onEnter();
    }
    
    public State<A> getCurrentState(){
    	return currentState;
    }
}


