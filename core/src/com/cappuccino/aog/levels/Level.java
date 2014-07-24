package com.cappuccino.aog.levels;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.cappuccino.aog.AOGGame;
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
	
	protected Color levelColor;
	protected boolean usePlayer;
	
	public void init(World world, boolean usePlayer){
		this.usePlayer = usePlayer;
		alexy = new Alexy(world);
		alexy.setCenter(getSpawnPoint());
		 
		AOGGame.setClearColor(
				 2.0f*0.04f*(1.0f-levelColor.r) + (float)(Math.sqrt(0.04f)*(2.0f*levelColor.r-1.0f)),
				 2.0f*0.04f*(1.0f-levelColor.g) + (float)(Math.sqrt(0.04f)*(2.0f*levelColor.g-1.0f)),
				 2.0f*0.04f*(1.0f-levelColor.b) + (float)(Math.sqrt(0.04f)*(2.0f*levelColor.b-1.0f)),
				 1f
				);
		 
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
		float bottomY = (area.y-area.height*0.3f) * Scene.WORLD_TO_BOX;
		int i = 0;
		boolean continueToRender = true;
		
		while (i<active_walls.size && continueToRender){
			Entity e = active_walls.get(i);
			 if(e.getCenter().y>bottomY){
				e.draw(batch);
			}else{
				continueToRender = false;
			}
			i++;
		}
		
	}
	
	protected void updateWalls(float delta){
		Rectangle area = Scissor.getArea();
		float bottomY = (area.y-area.height*0.3f) * Scene.WORLD_TO_BOX, topY = (area.y+area.height*1.3f) * Scene.WORLD_TO_BOX;
		int i = 0;
		boolean continueToRender = true;
		
		while(inactive_walls.size>0 && continueToRender){
			Entity e = inactive_walls.peek();
			if(e.getCenter().y<topY){
				e.active();
				active_walls.insert(0, inactive_walls.pop());
			}else{
				continueToRender = false;
			}
		}
		
		continueToRender = true;
		while (i<active_walls.size && continueToRender){
			Entity e = active_walls.get(i);
			if(e.getCenter().y>topY){
				e.disactive();
				inactive_walls.add(e);
				active_walls.removeIndex(i--);
			}else if(e.getCenter().y<bottomY){
				continueToRender = false;
			}
			i++;
		}
		
	}
	
	protected void renderEntities(SpriteBatch batch){
		Rectangle area = Scissor.getArea();
		float bottomY = (area.y-area.height*0.3f) * Scene.WORLD_TO_BOX;
		int i = 0, rendered = 0;
		boolean continueToRender = true;
		
		while (i<active_entities.size && continueToRender){
			Entity e = active_entities.get(i);
			 if(e.getCenter().y>bottomY){
				e.draw(batch);
				rendered++;
			}else{
				continueToRender = false;
			}
			i++;
		}
		
		//System.out.println("Rendered Entities " + rendered + " " + " Inactive ents " + inactive_entities.size);
		
		if(usePlayer){
			alexy.draw(batch);
		}
	}
	
	
	protected void updateEntities(float delta){
		Rectangle area = Scissor.getArea();
		float bottomY = (area.y-area.height*0.3f) * Scene.WORLD_TO_BOX, topY = (area.y+area.height*1.3f) * Scene.WORLD_TO_BOX;
		int i = 0, updated = 0;
		boolean continueToRender = true;
		
		while(inactive_entities.size>0 && continueToRender){
			Entity e = inactive_entities.peek();
			if(e.getCenter().y<topY){
				e.active();
				active_entities.insert(0, inactive_entities.pop());
			}else{
				continueToRender = false;
			}
		}
		
		continueToRender = true;
		while (i<active_entities.size && continueToRender){
			Entity e = active_entities.get(i);
			if(e.getCenter().y>topY){
				e.disactive();
				inactive_entities.add(e);
				active_entities.removeIndex(i--);
			}else if(e.getCenter().y>bottomY){
				e.update(delta);
				updated++;
			}else{
				continueToRender = false;
			}
			i++;
		}
		
		
		if(usePlayer){
			alexy.update(delta);
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
	
	public Array<Entity> getAllEntities(){
		Array<Entity> allEnts = new Array<Entity>();
		allEnts.addAll(active_entities);
		allEnts.addAll(inactive_entities);
		return allEnts;
	}
	
	public Array<Entity> getAllWalls(){
		Array<Entity> allWalls = new Array<Entity>();
		allWalls.addAll(active_walls);
		allWalls.addAll(inactive_walls);
		return allWalls;
	}
	
	public Array<Entity> getActiveEntities(){
		return active_entities;
	}
	
	public Array<Entity> getActiveWalls(){
		return active_walls;
	}
	
	public Color getColor() {
		return levelColor;
	}

	private void loadLevelFromFile(World world){
		Json j = new Json();
		FileHandle levelFile = Gdx.files.external("Desktop/levels/"+getLevelName()+".json");
		LevelModel model = j.fromJson(LevelModel.class, levelFile);
		model.loadOnLevel(world, this);
	}
	
	
	
	public void debugRender(SpriteBatch batch){
		for(int i=0; i<active_entities.size; i++){
			active_entities.get(i).draw(batch);
		}
		for(int i=0; i<active_walls.size; i++){
			active_walls.get(i).draw(batch);
		}
		alexy.draw(batch);
	}
	
	public void debugUpdate(float delta){
		for(int i=0; i<active_entities.size; i++){
			active_entities.get(i).update(delta);
		}
		alexy.update(delta);
	}
	
	
	
	
	
}
