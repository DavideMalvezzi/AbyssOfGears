package com.cappuccino.aog.mapeditor;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.levels.Level;

public class LevelModel {
	
	private Array<EntityModel> entitiesModels = new Array<EntityModel>();
	
	public void addEntity(Entity e){
		EntityModel model = new EntityModel(e);
		entitiesModels.add(model);
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
		
		
	}
	
}
