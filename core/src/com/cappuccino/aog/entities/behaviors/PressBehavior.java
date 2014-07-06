package com.cappuccino.aog.entities.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.JointsFactory;

public class PressBehavior implements EntityBehavior{
	
	private final float PRESS_PIPE_W = 74f;
	
	public static enum PressDirection{
		RIGHT,LEFT;
	};

	private Entity pipe;
	private float vel;
	private int minLen, maxLen, DIR = 1;
	private final Vector2 startPos = new Vector2();
	private PressDirection pressDir;
	
	public PressBehavior(int minDst, int maxDst, float vel, PressDirection pressDir) {
		this.minLen = minDst;
		this.maxLen = maxDst;
		this.vel = vel;
		this.pressDir = pressDir;
	}
	
	
	public void initEntity(Entity entity) {
		entity.getBody().setFixedRotation(true);
		startPos.set(entity.getCenter());
		
		//pipe = EntityFactory.getFreeEntity();
		//pipe.init(entity.getBody().getWorld(), "PressTube", EntitiesModels.gear, maxLen/PRESS_PIPE_W, entity.getScaleY());
		pipe.setAngle(entity.getAngle());
		
		if(pressDir == PressDirection.RIGHT){
			pipe.setCenter(entity.getCenter());
			entity.setCenter(pipe.getCenter().add(pipe.getWidth()*MathUtils.cos(pipe.getAngle()), pipe.getWidth()*MathUtils.sin(pipe.getAngle())));
		}else{
			pipe.setCenter(entity.getCenter().sub(pipe.getWidth()*MathUtils.cos(pipe.getAngle()), pipe.getWidth()*MathUtils.sin(pipe.getAngle())));
			entity.setCenter(pipe.getCenter().sub(entity.getWidth()*MathUtils.cos(pipe.getAngle()),entity.getWidth()*MathUtils.sin(pipe.getAngle())));
			startPos.sub(entity.getWidth()*MathUtils.cos(pipe.getAngle()),entity.getWidth()*MathUtils.sin(pipe.getAngle()));
		}
		
		JointsFactory.createWeldJoint(entity.getBody().getWorld(), entity, pipe, Vector2.Zero, Vector2.Zero, entity.getAngle(), true);
		
		pipe.setScaleX(1);
	}

	@Override
	public void update(Entity entity, float dt) {
		float dst = startPos.dst(entity.getCenter());
		float percent = dst/maxLen;
		float scale = maxLen/pipe.getRealWidth()*percent;
		
		pipe.setScaleX(scale);
		
		if(pressDir == PressDirection.RIGHT){
			pipe.setTraslX((maxLen-pipe.getWidth())*MathUtils.cos(pipe.getAngle()));
			pipe.setTraslY((maxLen-pipe.getWidth())*MathUtils.sin(pipe.getAngle()));
		}

		if(dst<minLen && DIR == -1 || dst>maxLen && DIR == 1)
			DIR*=-1;
		
		pipe.getBody().setLinearVelocity(vel*DIR*MathUtils.cos(entity.getAngle()), vel*DIR*MathUtils.sin(entity.getAngle()));
		
	}

	@Override
	public void render(SpriteBatch batch) {
		pipe.draw(batch);
		
	}

	@Override
	public void dispose() {
		pipe.dispose();
	}



}
