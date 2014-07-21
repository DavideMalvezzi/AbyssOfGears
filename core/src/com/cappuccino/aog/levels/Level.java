package com.cappuccino.aog.levels;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.Scissor;
import com.cappuccino.aog.entities.Alexy;
import com.cappuccino.aog.entities.Alexy.Status;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.mapeditor.LevelModel;

public abstract class Level {

	protected static Alexy alexy;
	protected final Array<Entity> active_entities = new Array<Entity>(true, 32);
	protected final Array<Entity> inactive_entities = new Array<Entity>(true, 32);
	protected final Array<Entity> active_walls = new Array<Entity>(true, 32);
	protected final Array<Entity> inactive_walls = new Array<Entity>(true, 32);
	
	public void init(World world, boolean usePlayer){
		alexy = new Alexy(world);
		alexy.setCenter(getSpawnPoint());
		if(usePlayer){
			active_entities.add(alexy);
		}
		
		loadLevelFromFile(world);
	}
	
	public void reset(){
		while(inactive_entities.size>0){
			Entity e = inactive_entities.pop();
			e.active();
			active_entities.insert(0, e);
		}
		
		while(inactive_walls.size>0){
			Entity e = inactive_walls.pop();
			e.active();
			active_walls.insert(0, e);
		}
		
		alexy.setCenter(getSpawnPoint());
		alexy.setLinearVelocity(0, 0);
		alexy.setState(Status.UMBRELLA_OPEN);
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(float delta);
	public abstract Vector2 getSpawnPoint();
	public abstract String getLevelName();
	
	
	protected void renderWalls(SpriteBatch batch){
		Rectangle area = Scissor.getArea();
		int i = 0, rendererd = 0;
		boolean continueToRender = true;
		
		while (i<active_walls.size && continueToRender){
			Entity e = active_walls.get(i);
			if(e.getCenter().y*Scene.BOX_TO_WORLD>area.y+area.height){
				e.disactive();
				inactive_walls.add(e);
				active_walls.removeIndex(i--);
			}else if(e.getCenter().y*Scene.BOX_TO_WORLD>area.y){
				e.draw(batch);
				rendererd++;
			}else{
				continueToRender = false;
			}
			i++;
		}
		
		//System.out.println(active_walls.size + " " + inactive_walls.size + "  " + rendererd + "  " + i);
	}
	
	protected void renderEntities(SpriteBatch batch){
		int i = 0;
		while (i<active_entities.size) {
			active_entities.get(i++).draw(batch);
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
