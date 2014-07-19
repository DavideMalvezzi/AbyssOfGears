package com.cappuccino.aog.levels;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.cappuccino.aog.entities.Alexy;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.mapeditor.LevelModel;

public abstract class Level {

	protected static Alexy alexy;
	protected final Array<Entity> active_entities = new Array<Entity>();
	protected final Array<Entity> inactive_entities = new Array<Entity>();
	protected final Array<Entity> active_walls = new Array<Entity>();
	protected final Array<Entity> inactive_walls = new Array<Entity>();
	
	//private int entities_to_render;
		
	public void init(World world, boolean usePlayer){
		alexy = new Alexy(world);
		alexy.setCenter(getSpawnPoint());
		if(usePlayer){
			active_entities.add(alexy);
		}
		
		loadLevelFromFile(world);
	}
	
	public void reset(){
		active_entities.addAll(inactive_entities);
		inactive_entities.clear();
		active_walls.addAll(inactive_entities);
		inactive_walls.clear();
		alexy.setCenter(getSpawnPoint());
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(float delta);
	public abstract Vector2 getSpawnPoint();
	public abstract String getLevelName();
	
	protected void renderEntities(SpriteBatch batch){
		int i = 0;
		
		while (i<active_entities.size) {
			active_entities.get(i++).draw(batch);
		}
		
		i = 0;
		while (i<active_walls.size) {
			active_walls.get(i++).draw(batch);
		}
	}
	
	
	protected void updateEntities(float delta){
		int i = 0;
		while(i < active_entities.size){
			active_entities.get(i++).update(delta);
		}
		
	}
	
	
	public void dispose(){
		for(Entity e : active_entities)
			e.dispose();
		active_entities.clear();
		
		for(Entity e : inactive_entities)
			e.dispose();
		inactive_entities.clear();
		
		for(Entity e : active_walls)
			e.dispose();
		active_walls.clear();
		
		for(Entity e : inactive_walls)
			e.dispose();
		inactive_walls.clear();
	}

	public static Alexy getPlayer(){
		return alexy;
	}
	
	public Array<Entity> getActiveEntities(){
		return active_entities;
	}
	
	public Array<Entity> getActiveWalls(){
		return active_walls;
	}

	private void loadLevelFromFile(World world){
		Json j = new Json();
		FileHandle levelFile = Gdx.files.internal("data/levels/"+getLevelName()+".json");
		LevelModel model = j.fromJson(LevelModel.class, levelFile);
		model.loadOnLevel(world, this);
	}
	
	
	
	
	
	
	
	
	
}
