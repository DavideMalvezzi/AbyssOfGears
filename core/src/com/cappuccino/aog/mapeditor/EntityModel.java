package com.cappuccino.aog.mapeditor;

import com.badlogic.gdx.math.Vector2;
import com.cappuccino.aog.entities.Entity;

public class EntityModel {
	Class<? extends Entity> type;
	Vector2 position,scale;
	float angle;
	Property prop1,prop2,prop3;
	
	public EntityModel(Class<? extends Entity> type) {
		this.type = type;
		this.position = new Vector2();
		this.scale = new Vector2(1, 1);
		this.angle = 0;
		this.prop1 = new Property();
		this.prop2 = new Property();
		this.prop3 = new Property();
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
