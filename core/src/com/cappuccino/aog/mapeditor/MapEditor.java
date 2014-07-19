package com.cappuccino.aog.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.levels.Level;

public class MapEditor {
	
	private EntitiesEditingInputListener modEntities;
	private WallsEditingInputListener modWalls;
	
	private EditingInputListener currentMod;
	private String currentModName = "Entities Mod";

	public MapEditor(Level level, World world, OrthographicCamera camera) {
		modEntities = new EntitiesEditingInputListener(level, world, camera);
		modWalls = new WallsEditingInputListener(level, world, camera);
		
		currentMod = modEntities;
		Gdx.input.setInputProcessor(currentMod);
	}
	
	
	public void draw(SpriteBatch batch){
		Matrix4 tmp = batch.getProjectionMatrix().cpy();
		
		batch.setProjectionMatrix(EditingInputListener.projection);
			batch.begin();
			EditingInputListener.font.setScale(1.75f);
			EditingInputListener.font.draw(batch, currentModName, 10, Gdx.graphics.getHeight()-10);
			EditingInputListener.font.setScale(1);
			batch.end();
		batch.setProjectionMatrix(tmp);
		
		currentMod.draw(batch);
	}
	public void update(){
		currentMod.update();
		
		if(!EditingInputListener.debug){
			if(Gdx.input.isKeyPressed(Keys.J)){
				currentModName = "Entities Mod";
				currentMod = modEntities;
				Gdx.input.setInputProcessor(currentMod);
			}
			if(Gdx.input.isKeyPressed(Keys.K)){
				currentModName = "Walls Mod";
				currentMod = modWalls;
				Gdx.input.setInputProcessor(currentMod);
			}
			if(Gdx.input.isKeyPressed(Keys.L)){}
						
		}
		
	}
	
	public boolean isDebugging(){
		return EditingInputListener.debug;
	}
}
