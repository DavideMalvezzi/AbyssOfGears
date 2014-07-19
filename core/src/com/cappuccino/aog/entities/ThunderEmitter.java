package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.mapeditor.EntityModel;

public class ThunderEmitter extends Entity{

	private Thunder thunder;
	private Vector2 emitPoint;
	
	public ThunderEmitter(World world){
		this(world, 0, 0, 0);
	}
	
	public ThunderEmitter(World world, EntityModel model) {
		this(world, model.position.x, model.position.y, model.angle);
	}
	
	public ThunderEmitter(World world, float x, float y, float angle){
		super("ThunderEmitter", world);
		initBody(world, BodyType.StaticBody);
		initFixtures();
		
		
		setAngle(angle);
		setCenter(x, y);
		
		emitPoint = new Vector2(getCenter()).add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle()) );		
	}

	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if(thunder!=null){
			thunder.draw(batch);
		}
	}

	@Override
	public void update(float delta) {
		if(thunder!=null){
			thunder.update(delta);
		}
	}
	
	public void setEnd(ThunderEmitter endEmitter, float duration){
		float angle = MathUtils.atan2(endEmitter.emitPoint.y-emitPoint.y, endEmitter.emitPoint.x-emitPoint.x);
		thunder = new Thunder(body.getWorld(), emitPoint.x, emitPoint.y, emitPoint.dst(endEmitter.emitPoint), 50, angle, 1, duration);
	}
	
	
	@Override
	protected void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;
		
		bodyLoader.attachFixture(body, "ThunderEmitter", fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("ThunderEmitter", getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
	}
	
	@Override
	public void disactive() {
		super.disactive();
		if(thunder!=null){
			thunder.dispose();
		}
	}
	
	
	
	
	@Override
	public void recalculate() {
		emitPoint = new Vector2(getCenter()).add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle()));
		if(thunder!=null){
			thunder.setCenter(emitPoint);
		}
	}
	
	@Override
	public void setAngle(float angle) {
		super.setAngle(angle);
		if(thunder!=null){
			thunder.setAngle(angle);
		}
	}
	
	
	
}
