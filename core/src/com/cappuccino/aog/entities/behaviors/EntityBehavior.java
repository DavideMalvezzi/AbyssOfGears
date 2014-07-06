package com.cappuccino.aog.entities.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cappuccino.aog.entities.Entity;

public interface EntityBehavior {

	public void initEntity(Entity entity);
	public void update(Entity entity, float dt);
	public void render(SpriteBatch batch);
	public void dispose();
	
}
