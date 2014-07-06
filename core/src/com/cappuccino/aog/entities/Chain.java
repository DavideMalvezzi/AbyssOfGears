package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.Scene;

public class Chain extends Entity{

	private Entity[] chain;
	
	public Chain(World world, float x, float y, int len, float scale, float angle) {
		super("Chain", world);
		chain = new Entity[len];
		init(world, BodyType.KinematicBody);
		
		setScaleX(scale);
		setScaleY(scale);
		initFixture(this);
		
		for(int i=0; i<len; i++){
			chain[i] = new Entity("Chain", world);
			chain[i].init(world, BodyType.DynamicBody);
			chain[i].setScaleX(scale);
			chain[i].setScaleY(scale);
			initFixture(chain[i]);
			
			chain[i].setAngle(angle);
			chain[i].setCenter(x, y-chain[i].getWidth()*(i+1));
		}
		setAngle(angle);
		setCenter(x, y);
		
		JointsFactory.createRopeJoint(world, this, chain[0], new Vector2(getWidth(), 0), Vector2.Zero, chain[0].getWidth(), true);
		JointsFactory.createRevoluteJoint(world, this, chain[0], new Vector2(getWidth(), 0), Vector2.Zero, true);
		
		for(int i=0; i<len-1; i++){
			JointsFactory.createRopeJoint(world, 
					chain[i], chain[i+1], 
					new Vector2(chain[i].getWidth(), 0), Vector2.Zero, 
					chain[i].getWidth(), true);
			
			JointsFactory.createRevoluteJoint(world, 
					chain[i], chain[i+1], 
					new Vector2(chain[i].getWidth(), 0), Vector2.Zero, true);
		}
		
	}
	
	
	protected void initFixture(Entity e) {
		FixtureDef fd = new FixtureDef();
		fd.density = 2;
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = ENTITY_MASK;
		
		bodyLoader.attachFixture(e.getBody(), "Chain", fd, getRealWidth()*e.scaleX*Scene.BOX_TO_WORLD, getRealWidth()*e.scaleY*Scene.BOX_TO_WORLD);
		e.origin.set(bodyLoader.getOrigin("Chain", getRealWidth()*e.scaleX*Scene.BOX_TO_WORLD, getRealWidth()*e.scaleY*Scene.BOX_TO_WORLD));
		e.getBody().setGravityScale(9.8f);
	}
	
	
	public void attachEntity(Entity e, Vector2 anchor){
		JointsFactory.createRevoluteJoint(body.getWorld(), 
				chain[chain.length-1], e, 
				new Vector2(chain[chain.length-1].getWidth(), 0), anchor, true);
	}
	

	@Override
	public void draw(SpriteBatch batch){
		super.draw(batch);
		for(int i=0; i<chain.length; i++){
			chain[i].draw(batch);
		}
	}
	
	public void setCenter(float x, float y) {
		super.setCenter(x, y);
		for(int i=0; i<chain.length; i++){
			chain[i].setCenter(x+i*chain[i].getWidth()*MathUtils.cos(chain[i].getAngle()),y+i*chain[i].getWidth()*MathUtils.sin(chain[i].getAngle()) );
		}
	}
	
	@Override
	public void setLinearVelocity(float velX, float velY) {
		super.setLinearVelocity(velX, velY);
		for(int i=0; i<chain.length; i++){
			chain[i].setLinearVelocity(velX, velY);
		}
	}
	
	
	@Override
	public void dispose() {
		super.dispose();
		for (int i=0; i<chain.length; i++) {
			chain[i].dispose();
		}
	}
	
	
}
