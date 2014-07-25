package com.cappuccino.aog.levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.entities.Press;

public class MainMenuLevel extends Level {

	private Press p,p2;
	
	
	public MainMenuLevel(World world) {
		levelColor = new Color(0xff9600ff);
		init(world, false);
		
		p = new Press(world, 26, 250, 75, 300, 30, 0, 0.65f);
		p2 = new Press(world, 668, 250, 75, 300, 30, 180*MathUtils.degRad, 0.65f);
		openPress();
		disactivePress();
		
		active_entities.add(p);
		active_entities.add(p2);
		
	}
	
	public void activePress(){
		p.active();
		p2.active();
		p.setVelocity(30);
		p2.setVelocity(30);
	}
	
	public void disactivePress(){
		p.disactive();
		p2.disactive();
		p.setVelocity(0);
		p2.setVelocity(0);
	}
	
	public void openPress(){
		p.open();
		p2.open();
		p.setVelocity(0);
		p2.setVelocity(0);
	}
	
	public boolean pressFinish(){
		return p.isClosed() && p2.isClosed();
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		renderEntities(batch);
		renderWalls(batch);
	}

	@Override
	public void update(float delta) {
		updateEntities(delta);
		updateWalls(delta);
	}

	@Override
	public Vector2 getSpawnPoint() {
		return new Vector2(0,-200);
	}

	@Override
	public String getLevelName() {
		return "MainMenuLevel";
	}

}
