package com.cappuccino.aog;

import com.cappuccino.aog.entities.Entity;

public class StateMachine<A extends Entity> {
	
	private State<A> currentState;
	private Entity entity;
	
	@SuppressWarnings("unchecked")
	public StateMachine(Entity entity, State<A> state){
		this.entity = entity;
		this.currentState = state;
		this.currentState.onEnter((A)entity);
	}
	
	
    @SuppressWarnings("unchecked")
	public void update(float dt){
    	currentState.update(dt, (A) entity);
    }
    
    @SuppressWarnings("unchecked")
    public void changeState(State<A> newState){
    	currentState.onExit((A)entity);
    	currentState = newState;
    	currentState.onEnter((A)entity);
    }
    
    public State<A> getCurrentState(){
    	return currentState;
    }
}


