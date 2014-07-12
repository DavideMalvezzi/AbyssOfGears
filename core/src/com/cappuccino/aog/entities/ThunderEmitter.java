package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Scene;

public class ThunderEmitter extends Entity{

	private Thunder thunder;
	private Vector2 emitPoint;
	
	public ThunderEmitter(World world, float x, float y, float angle){
		super("ThunderEmitter", world);
		init(world, BodyType.StaticBody);
		initFixture();
		
		
		setAngle(angle);
		setPosition(x, y);
		
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
	
	public void setEnd(ThunderEmitter endEmitter){
		float angle = MathUtils.atan2(endEmitter.emitPoint.y-emitPoint.y, endEmitter.emitPoint.x-emitPoint.x);
		
		thunder = new Thunder(body.getWorld(), emitPoint.x, emitPoint.y, emitPoint.dst(endEmitter.emitPoint), 50, angle, 1);
	}
	
	
	@Override
	protected void initFixture() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;
		
		bodyLoader.attachFixture(body, "ThunderEmitter", fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("ThunderEmitter", getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
	}
	
	
	
	
}
