package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.mapeditor.EntityModel.Property;

public class Chain extends Entity{

	private Entity[] chain;
	private int chainLen = 1;
	
	public Chain(World world) {
		super("Chain", world);
		init(world, BodyType.KinematicBody);
		initFixture();
		createChain(world);
	}
	
	
	public Chain(World world, float x, float y, int len, float scale, float angle) {
		super("Chain", world);
		this.chainLen = len;
		init(world, BodyType.KinematicBody);
		
		setScaleX(scale);
		setScaleY(scale);
		initFixture();
		
		createChain(world);
		
		setAngle(angle);
		setCenter(x, y);
	}
	
	
	private void createChain(World world){
		chain = new Entity[chainLen];
		for(int i=0; i<chainLen; i++){
			chain[i] = new Entity("Chain", world);
			chain[i].init(world, BodyType.DynamicBody);
			chain[i].setScaleX(getScaleX());
			chain[i].setScaleY(getScaleY());
			initFixture(chain[i]);
			
			chain[i].setAngle(getAngle());
			chain[i].setCenter(getCenter().x+chain[i].getWidth()*2*(i+1), getCenter().y);
		}
		
		JointsFactory.createRopeJoint(world, this, chain[0], new Vector2(getWidth(), 0), Vector2.Zero, chain[0].getWidth(), true);
		JointsFactory.createRevoluteJoint(world, this, chain[0], new Vector2(getWidth(), 0), Vector2.Zero, true);
		
		
		for(int i=0; i<chainLen-1; i++){
			JointsFactory.createRopeJoint(world, 
					chain[i], chain[i+1], 
					new Vector2(chain[i].getWidth(), 0), Vector2.Zero, 
					chain[i].getWidth(), true);
			JointsFactory.createRevoluteJoint(world, 
					chain[i], chain[i+1], 
					new Vector2(chain[i].getWidth(), 0), Vector2.Zero, true);
		}
	}
	
	
	protected void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.density = 2;
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;
		
		bodyLoader.attachFixture(getBody(), "Chain", fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		origin.set(bodyLoader.getOrigin("Chain", getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
		getBody().setGravityScale(9.8f);
	}
	
	
	protected void initFixture(Entity e) {
		FixtureDef fd = new FixtureDef();
		fd.density = 2;
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = PLAYER | LIGHT;;
		
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
			chain[i].setCenter(x+i*chain[i].getWidth()*MathUtils.cos(getAngle()),y+i*chain[i].getWidth()*MathUtils.sin(getAngle()) );
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
	
	
	@Override
	public Property getProp1() {
		return new Property("Length", chainLen);
	}
	
	@Override
	public void setProp1(float value) {
		if(value>0){
			this.chainLen = (int)value;
			
			for (int i=0; i<chain.length; i++) {
				chain[i].dispose();
			}
			
			createChain(body.getWorld());
		}
		
	}
	
}
