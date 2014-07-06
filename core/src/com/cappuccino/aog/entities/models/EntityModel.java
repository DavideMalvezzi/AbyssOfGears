package com.cappuccino.aog.entities.models;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class EntityModel extends FixtureDef {
	
	public static final short PLAYER = 0x0002;
	public static final short WALL = 0x0004;
	public static final short ENTITY = 0x0008;
	
	public static final short PLAYER_MASK = PLAYER | WALL | ENTITY;
	public static final short WALL_MASK = PLAYER | WALL;
	public static final short ENTITY_MASK = PLAYER | ENTITY;
	
	public BodyType type;
	
	public static class PlayerModel extends EntityModel{
		public PlayerModel() {
			this.type = BodyType.DynamicBody;
			this.density = 2;
			this.friction = 0;
			this.restitution = 0;
			this.filter.categoryBits = PLAYER;
			this.filter.maskBits = PLAYER_MASK;
		}
	}
	
	public static class UmbrellaModel extends EntityModel{
		public UmbrellaModel() {
			this.type = BodyType.DynamicBody;
			this.density = 1.603f;
			this.friction = 0;
			this.restitution = 0;
			this.filter.categoryBits = PLAYER;
			this.filter.maskBits = PLAYER_MASK;
		}
	}

	public static class WallModel extends EntityModel{
		public WallModel(){
			this.type = BodyType.StaticBody;
			this.density = 0;
			this.friction = 0;
			this.restitution = 0;
			this.filter.categoryBits = WALL;
			this.filter.maskBits = WALL_MASK;
		}
	}
	
	public static class GearModel extends EntityModel{
		public GearModel() {
			this.type = BodyType.KinematicBody;
			this.density = 2f;
			this.friction = 0;
			this.restitution = 0.5f;
			this.filter.categoryBits = ENTITY;
			this.filter.maskBits = ENTITY_MASK;
		}
	}
	
	public static class PressPlate extends EntityModel{
		public PressPlate() {
			this.type = BodyType.DynamicBody;
			this.density = 20f;
			this.friction = 0;
			this.restitution = 0.5f;
			this.filter.categoryBits = ENTITY;
			this.filter.maskBits = ENTITY_MASK;
		}
	}
	
	
	
	
	
}
