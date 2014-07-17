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
		alexy.setCenter(getSpawnPoint());
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(float delta);
	public abstract Vector2 getSpawnPoint();
	public abstract String getLevelName();
	
	protected void renderEntities(SpriteBatch batch){
		int i = 0;
		
		//alexy.draw(batch);
		while (i<active_entities.size) {
			active_entities.get(i++).draw(batch);
		}
		
		
	}
	protected void updateEntities(float delta){
		//boolean stopUpdate = false;
		//Rectangle view = Scissor.getArea();
		//Entity e;
		//entities_to_render = 0;
		int i = 0;
		//alexy.update(delta);
		while(i < active_entities.size){
			active_entities.get(i++).update(delta);
			/*
			e = active_entities.get(entities_to_render);
			if(e.getPosition().y>(view.y+view.height)*GameScene.WORLD_TO_BOX){
				inactive_entities.add(active_entities.removeIndex(entities_to_render));
			}else if(e.getPosition().y+e.getHeight()<view.y*GameScene.WORLD_TO_BOX){
				stopUpdate = true;
			}else{
				entities_to_render++;
				e.update(delta);
			}*/
			
		}
		
		
	}
	
	protected void sortEntities(){
		Entity e1,e2;
		int i, j, temp;
		for(i=0; i<active_entities.size-1; i++){
			temp = i;
			for(j=i+1; j<active_entities.size; j++){
				e1 = active_entities.get(temp);
				e2 = e1 = active_entities.get(j);
				if(e2.getPosition().y + e2.getHeight()>e1.getPosition().y + e1.getHeight()){
					temp = j;
				}
			}
			active_entities.swap(i, temp);
		}
	}
	
	public static Alexy getPlayer(){
		return alexy;
	}
	
	public Array<Entity> getActiveEntities(){
		return active_entities;
	}

	public void dispose(){
		for(Entity e : active_entities)
			e.dispose();
		active_entities.clear();
		for(Entity e : inactive_entities)
			e.dispose();
		inactive_entities.clear();
	}

	
	
	
	
	private void loadLevelFromFile(World world){
		Json j = new Json();
		FileHandle levelFile = Gdx.files.internal("data/levels/"+getLevelName()+".json");
		LevelModel model = j.fromJson(LevelModel.class, levelFile);
		model.loadOnLevel(world, this);
		
	}
	
	
	
	
	
	
	
	
	
}
