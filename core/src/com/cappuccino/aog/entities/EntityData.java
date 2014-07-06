package com.cappuccino.aog.entities;

public class EntityData {
	
	private String name;
	private Entity entity;
	
	public EntityData(String name, Entity entity) {
		this.name = name;
		this.entity = entity;
	}
	
	public String getName() {
		return name;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setEntity(Entity entity){
		this.entity = entity;
	}
}
