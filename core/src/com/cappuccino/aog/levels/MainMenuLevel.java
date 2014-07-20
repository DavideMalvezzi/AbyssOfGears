package com.cappuccino.aog.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.entities.Press;

public class MainMenuLevel extends Level {

	private Press p,p2;
	
	
	public MainMenuLevel(World world) {
		init(world, false);
		
		p = new Press(world, 26, 250, 75, 300, 30, 0, 0.65f);
		p2 = new Press(world, 668, 250, 75, 300, 30, 180*MathUtils.degRad, 0.65f);
		openPress();
		disactivePress();
		/*
		SmokeEmitter smoke1 = new SmokeEmitter(world, 20, 420, 20*MathUtils.degRad);
		SmokeEmitter smoke2 = new SmokeEmitter(world, 610, 420, 160*MathUtils.degRad);
		
		Gear gear = new Gear(world, 5, 0, 30, 2, 0.85f);
		Gear gear2 = new Gear(world, 7, 615, 550, 1, 0.75f);
		Gear gear3 = new Gear(world, 6, 630, 75, -1.3f, 0.55f);
		
		active_entities.add(smoke1);
		active_entities.add(smoke2);
		
		active_entities.add(gear);
		active_entities.add(gear2);
		active_entities.add(gear3);
		*/
		
		active_entities.add(p);
		active_entities.add(p2);
		
	}
	
	public void activePress(){
		p.active();
		p2.active();
	}
	
	public void disactivePress(){
		p.disactive();
		p2.disactive();
	}
	
	public void openPress(){
		p.open();
		p2.open();
	}
	
	public boolean pressFinish(){
		return p.isClosed() && p2.isClosed();
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		renderEntities(batch);
		
	}

	@Override
	public void update(float delta) {
		updateEntities(delta);
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
