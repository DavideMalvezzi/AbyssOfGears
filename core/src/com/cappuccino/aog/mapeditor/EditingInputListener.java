package com.cappuccino.aog.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.Wall;
import com.cappuccino.aog.levels.Level;

public class EditingInputListener extends InputAdapter {

	protected World world;
	protected OrthographicCamera camera;
	protected Level level;
	
	
	public static final Matrix4 projection = new Matrix4().setToOrtho(0, Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight(), 0, 10);
	public static final BitmapFont font = new BitmapFont();
	
	protected final LevelModel levelModel = new LevelModel();
	public static boolean debug = false;
	
	//Entities modding
	protected enum EntityEditingModes{
		ROTATE,SCALEXY,SCALEX,SCALEY,EDIT_PROP1,EDIT_PROP2,EDIT_PROP3;
	}
	
	
	public void draw(SpriteBatch batch){}
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
	}
	
	
	protected void save() {
		Json j = new Json();
		FileHandle levelFile = Gdx.files.external("/Desktop/levels/"+level.getLevelName()+".json");
		
		levelModel.clear();
		
		if(levelFile.exists()){
			levelFile.delete();
		}
		
		for(Entity e : level.getActiveEntities()){
			levelModel.addEntity(e);
		}
		
		for(Entity e : level.getActiveWalls()){
			levelModel.addWall((Wall) e);
		}
		
		levelFile.writeString(j.prettyPrint(levelModel), true);
	}
	
	
}
