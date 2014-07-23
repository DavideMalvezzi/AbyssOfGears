package com.cappuccino.aog.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.mapeditor.EntityModel;
import com.cappuccino.aog.mapeditor.EntityModel.Property;
import com.cappuccino.aog.scene.GameScene;

public class Wall extends Entity{
	
	private static final Color[] color = new Color[]{
		new Color(0x151515ff),
		new Color(0x0a0a0aff)
	};

	int colorIndex;
	
	public Wall(World world) {
		this(world, 0, 0, 0, 1, 1, 0);
	}
	
	public Wall(World world, EntityModel model) {
		this(world, model.position.x, model.position.y, model.angle, model.scale.x, model.scale.y, (int)model.internalProp1.value);
	}
	
	public Wall(World world, float x, float y, float angle, float scaleX, float scaleY, int colorIndex) {
		super("Wall", world);
		this.colorIndex = colorIndex;
		initBody(world, BodyType.StaticBody);
		setScaleX(scaleX);
		setScaleY(scaleY);
		initFixtures();
		
		setCenter(x, y);
		setAngle(angle);
	}
	
	@Override
	public void disactive() {
		body.setActive(false);
	}
	
	@Override
	public void active() {
		body.setActive(true);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(color[colorIndex]);
		super.draw(batch);
		batch.setColor(Color.WHITE);
	}
	
	
	public void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;
		
		bodyLoader.attachFixture(body, "Wall", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
	
		origin.set(bodyLoader.getOrigin("Wall", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}
	
	@Override
	public void recalculate() {
		reloadFixtures();
	}
	
	@Override
	public Property getProp1() {
		return new Property("Color", colorIndex);
	}
	
	@Override
	public void setProp1(float value) {
		if(value>=0 && value<color.length){
			colorIndex = (int)value;
		}
	}

}
