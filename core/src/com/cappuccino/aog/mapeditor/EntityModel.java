package com.cappuccino.aog.mapeditor;

import com.badlogic.gdx.math.Vector2;
import com.cappuccino.aog.entities.Entity;

public class EntityModel {
	String type;
	Vector2 position,scale;
	float angle;
	Property internalProp1,internalProp2,internalProp3;
	Property externalEntity1, externalEntity2;
	
	public EntityModel(Entity e) {
		this.type = e.getClass().getName();
		this.position = e.getPosition();
		this.scale = new Vector2(e.getScaleX(), e.getScaleY());
		this.angle = e.getAngle();
		this.internalProp1 = e.getProp1();
		this.internalProp2 =  e.getProp2();
		this.internalProp3 =  e.getProp3();
		this.externalEntity1 = new Property("Null",-1);
		this.externalEntity2 = new Property("Null",-1);
	}
	
	
	public static class Property{
		String name;
		float value;
		
		public Property() {
			name = "NoProp";
			value = 0;
		}
		
		public Property(String name, float value) {
			this.name = name;
			this.value = value;
		}
	}
}
