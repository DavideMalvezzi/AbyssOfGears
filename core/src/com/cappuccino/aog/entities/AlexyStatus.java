package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.cappuccino.aog.State;

public enum AlexyStatus implements State<Alexy>   {

	
	
	LANDED(){
		@Override
		public void onEnter(Alexy entity) {
			entity.getBody().setGravityScale(100);
			entity.getFoot().getBody().setGravityScale(100);
		}

		@Override
		public void update(float dt, Alexy entity){
			Alexy.ANIMATION_TIME += dt;
			if(Gdx.input.isKeyPressed(Keys.LEFT)){
				Alexy.WALK_DIR = -1;
				entity.getFoot().setAngularVelocity(-Alexy.WALK_DIR*5);
			}else if(Gdx.input.isKeyPressed(Keys.RIGHT)){
				Alexy.WALK_DIR = 1;
				entity.getFoot().setAngularVelocity(-Alexy.WALK_DIR*5);
			}else{
				entity.getFoot().setAngularVelocity(0);
			}
		}

		@Override
		public void onExit(Alexy entity) {
			Alexy.ANIMATION_TIME = 0;
			entity.getFoot().setAngularVelocity(0);
			entity.getBody().setGravityScale(1);
			entity.getFoot().getBody().setGravityScale(1);
		}
		
	},
	
	FALLING_UMBRELLA_OPEN(){

		@Override
		public void onEnter(Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			entity.getUmbrella().update(dt);
			entity.setAngularVelocity(entity.getUmbrella().getAngularVelocity());
			if(Gdx.input.isKeyPressed(Keys.LEFT) && entity.getLinearVelocity().x>-Alexy.MAX_LIN_VEL){
				entity.getBody().applyForceToCenter(-Alexy.LIN_ACCEL, 0, true);
				entity.getUmbrella().getBody().applyForceToCenter(-Alexy.LIN_ACCEL, 0, true);
			}
			if(Gdx.input.isKeyPressed(Keys.RIGHT) && entity.getBody().getLinearVelocity().x<Alexy.MAX_LIN_VEL){
				entity.getBody().applyForceToCenter(Alexy.LIN_ACCEL, 0, true);
				entity.getUmbrella().getBody().applyForceToCenter(Alexy.LIN_ACCEL, 0, true);
			}
		}

		@Override
		public void onExit(Alexy entity) {
			// TODO Auto-generated method stub
			
		}
	},
	
	FALLING_UMBRELLA_CLOSED(){

		@Override
		public void onEnter(Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExit(Alexy entity) {
			// TODO Auto-generated method stub
			
		}
	},
	RECOIL(){

		@Override
		public void onEnter(Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExit(Alexy entity) {
			// TODO Auto-generated method stub
			
		}
	},
	DYING(){

		@Override
		public void onEnter(Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExit(Alexy entity) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	
}
