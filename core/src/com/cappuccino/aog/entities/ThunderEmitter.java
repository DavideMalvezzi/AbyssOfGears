package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.mapeditor.EntityModel;
import com.cappuccino.aog.mapeditor.EntityModel.Property;

public class ThunderEmitter extends Entity{

	private Thunder thunder;
	private float thunderLength, duration, delay;
	private Vector2 emitPoint;
	
	public ThunderEmitter(World world){
		this(world, 0, 0, 0, 450, 1, 0);
	}
	
	public ThunderEmitter(World world, EntityModel model) {
		this(world, model.position.x, model.position.y, model.angle, model.internalProp1.value, model.internalProp2.value,  model.internalProp3.value);
	}
	
	public ThunderEmitter(World world, float x, float y, float angle, float thunderLength, float duration, float delay){
		super("ThunderEmitter", world);
		this.thunderLength = thunderLength;
		this.duration = duration;
		this.delay = delay;
		
		initBody(world, BodyType.StaticBody);
		initFixtures();
		
		setCenter(x, y);
		setAngle(angle);
		
		emitPoint = new Vector2(getCenter()).add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle()));
		thunder = new Thunder(body.getWorld(), emitPoint.x, emitPoint.y, thunderLength, 50, angle, duration, delay);
	}

	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		thunder.draw(batch);
	}

	@Override
	public void update(float delta) {
		thunder.update(delta);
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
		thunder.setCenter(emitPoint);
		thunder.setAngle(getAngle());
	}
	
	@Override
	public Property getProp1() {
		return new Property("Length", thunderLength);
	}
	
	@Override
	public void setProp1(float value){
		thunderLength = value;
		thunder.dispose();
		thunder = new Thunder(body.getWorld(), emitPoint.x, emitPoint.y, thunderLength, 50, getAngle(), duration, delay);
	}
	
	@Override
	public Property getProp2() {
		return new Property("Duration", duration);
	}
	
	@Override
	public void setProp2(float value) {
		duration = value;
		thunder.setDuration(value);
	}
	
	public Property getProp3() {
		return new Property("Delay", delay);
	}
	
	@Override
	public void setProp3(float value) {
		delay = value;
		thunder.setDelay(delay);
	}
	
	
	
	@Override
	public void dispose() {
		super.dispose();
		if(thunder!=null){
			thunder.dispose();
		}
	}
	
}
