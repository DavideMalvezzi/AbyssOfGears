package com.cappuccino.aog.entities.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.cappuccino.aog.entities.Entity;

public class MoveAlongPath implements EntityBehavior {

	public static enum PathType{
		Linear,
		Sinusoidal;
	}
	
	private int DIR = 1;
	private float velocity, limit, rotation;
	private PathType type;
	private float angle = 0;
	private final Vector2 startPos = new Vector2();
	
	
	public MoveAlongPath(PathType type, float velocity, float limit, float rotation){
		this.type = type;
		this.velocity = velocity;
		this.limit = limit;
		if(rotation<0)rotation+=360;
		this.rotation = rotation;
	}
	
	@Override
	public void initEntity(Entity entity) {
		startPos.set(entity.getCenter());
	}

	@Override
	public void update(Entity entity, float dt){
		Vector2 pos = entity.getCenter();
		Vector2 vel = new Vector2();
		float dst = pos.dst(startPos);
		
		switch (type) {
			case Linear:
				vel.x = velocity*MathUtils.cosDeg(rotation)*DIR;
				vel.y = velocity*MathUtils.sinDeg(rotation)*DIR;
				
				if(dst>limit){
					startPos.add(limit*MathUtils.cosDeg(rotation)*DIR,limit*MathUtils.sinDeg(rotation)*DIR);
					DIR *=-1;
				}
				entity.getBody().setLinearVelocity(vel);
			break;
			
			case Sinusoidal:
				angle += 360.0/limit;
				vel.x = velocity*DIR;
				vel.y = velocity*MathUtils.sinDeg(angle)*DIR;
				vel.rotate(rotation);
				
				if(dst>limit){
					startPos.add(limit*MathUtils.cosDeg(rotation)*DIR,limit*MathUtils.sinDeg(rotation)*DIR);
					DIR *=-1;
					angle = 0;
				}
				
				entity.getBody().setLinearVelocity(vel);
			break;

		}
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
