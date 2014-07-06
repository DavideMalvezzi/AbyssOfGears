package com.cappuccino.aog.entities.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.Alexy.DeadType;
import com.cappuccino.aog.entities.Alexy.Status;

public class BlowUp implements CollisionListener{

	
	@Override
	public void onCollision(Entity sender, Entity collidedEntity, Contact contact, ContactImpulse impulse) {
		float[] normal = impulse.getNormalImpulses();
		
		if(sum(normal)>800){
			alexy.setState(Status.DYING);
			alexy.setDeadType(DeadType.BLOWED_UP);
			
		}
	}
	
	
	
	
	private float sum(float[] arr){
		float x = 0;
		for(int i=0; i<arr.length; i++){
			x += arr[i];
		}
		return x;
	}

}
