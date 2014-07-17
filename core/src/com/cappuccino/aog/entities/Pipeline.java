package com.cappuccino.aog.entities;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.mapeditor.EntityModel;
import com.cappuccino.aog.mapeditor.EntityModel.Property;

public class Pipeline extends Entity{

	public Pipeline(World world){
		this(world, 0, 0, 0, 1, 1, 0);
	}
	
	public Pipeline(World world, EntityModel model){
		this(world, (int) model.internalProp1.value, model.position.x, model.position.y, model.scale.x, model.scale.y, model.angle);
	}
	
	
	public Pipeline(World world, int type, float x, float y, float scaleX, float scaleY, float angle) {
		super("Pipe" + type, world);
		initBody(world, BodyType.StaticBody);
		
		setScaleX(scaleX);
		setScaleY(scaleY);
		initFixtures();
		
		setAngle(angle);
		setCenter(x, y);
	}
	
	
	
	protected void initFixtures(){
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;
		
		bodyLoader.attachFixture(body, texture.name, fd, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin(texture.name, getRealWidth()*scaleX*Scene.BOX_TO_WORLD, getRealWidth()*scaleY*Scene.BOX_TO_WORLD));
		
	}
	
	@Override
	public void recalculate() {
		Array<Fixture> fs = body.getFixtureList();
		for(Fixture f: fs){
			body.destroyFixture(f);
		}
		fs.clear();
		initFixtures();
	}
	
	@Override
	public Property getProp1() {
		return new Property("Type", Integer.parseInt(texture.name.replace("Pipe", "")));
	}
	
	@Override
	public void setProp1(float value) {
		if(value>=0 && value<=1){
			texture = Assets.getTexture("Pipe" + (int)value);
		}
	}

}
