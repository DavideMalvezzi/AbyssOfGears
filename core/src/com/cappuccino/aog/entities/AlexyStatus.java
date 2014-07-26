package com.cappuccino.aog.entities;

import static com.cappuccino.aog.entities.Alexy.LIN_ACCEL;
import static com.cappuccino.aog.entities.Alexy.MAX_LIN_VEL;
import com.cappuccino.aog.State;

public enum AlexyStatus implements State<Alexy>   {

	
	
	LANDING(){

		@Override
		public void onEnter() {
			System.out.println("AlexyStatus.enclosing_method()");
		}

		@Override
		public void update(float dt, Alexy entity) {
			System.out.println("AlexyStatus.enclosing_method()");
			
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
		
	},
	FALLING_UMBRELLA_OPEN(){

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	FALLING_UMBRELLA_CLOSED(){

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	RECOIL(){

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	DYING(){

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float dt, Alexy entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	
}
