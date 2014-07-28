package com.cappuccino.aog.mapeditor;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.Wall;
import com.cappuccino.aog.levels.Level;

public class LevelModel {
	
	private Array<EntityModel> entitiesModels = new Array<EntityModel>();
	private Array<EntityModel> wallsModels = new Array<EntityModel>();
	
	public void addEntity(Entity e){
		entitiesModels.add(e.getModel());
	}
	
	public void addWall(Wall e){
		wallsModels.add(e.getModel());
	}
	

	public void clear(){
		entitiesModels.clear();
		wallsModels.clear();
	}
	
	
	public void loadOnLevel(World world, Level level) {
		Array<Entity> entities = level.getActiveEntities();
		for (EntityModel model : entitiesModels) {
			try {
				Class<?> modelClass =  Class.forName(model.type);
				Constructor<?> entityConstructor = modelClass.getConstructor(World.class, EntityModel.class);
				entities.add((Entity) entityConstructor.newInstance(world, model));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Array<Entity> wall = level.getActiveWalls();
		for (EntityModel model : wallsModels) {
			try {
				Class<?> modelClass =  Class.forName(model.type);
				Constructor<?> entityConstructor = modelClass.getConstructor(World.class, EntityModel.class);
				wall.add((Entity) entityConstructor.newInstance(world, model));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
}
