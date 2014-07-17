package com.cappuccino.aog.mapeditor;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.ArrowEmitter;
import com.cappuccino.aog.entities.Chain;
import com.cappuccino.aog.entities.Entity;
import com.cappuccino.aog.entities.EntityData;
import com.cappuccino.aog.entities.GasEmitter;
import com.cappuccino.aog.entities.Gear;
import com.cappuccino.aog.entities.LaserEmitter;
import com.cappuccino.aog.entities.Pipeline;
import com.cappuccino.aog.entities.Press;
import com.cappuccino.aog.entities.SmokeEmitter;
import com.cappuccino.aog.entities.Spear;
import com.cappuccino.aog.entities.SpikedBall;
import com.cappuccino.aog.entities.ThunderEmitter;
import com.cappuccino.aog.levels.Level;

public class MapEditingInputListener extends InputAdapter{
	
	@SuppressWarnings("unchecked")
	private static Class< ? extends Entity>[] entityTypes = new Class[]{
		ArrowEmitter.class,
		Chain.class,
		GasEmitter.class,
		Gear.class,
		LaserEmitter.class,
		Pipeline.class,
		Press.class,
		SmokeEmitter.class,
		Spear.class,
		SpikedBall.class, 
		ThunderEmitter.class,
	};
	
	
	private enum EditingModes{
		ROTATE,SCALEXY,SCALEX,SCALEY,EDIT_PROP1,EDIT_PROP2,EDIT_PROP3;
	}
	
	private EditingModes currentMode = EditingModes.ROTATE;
	
	private World world;
	private OrthographicCamera camera;
	private Level level;
	private static Entity currentEntity;
	private static int currentType;
	
	private static final Matrix4 projection = new Matrix4().setToOrtho(0, Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight(), 0, 10);
	private static final BitmapFont font = new BitmapFont();
	
	private boolean debug = false;
	
	private static final QueryCallback callback = new QueryCallback() {
		public boolean reportFixture(Fixture fixture) {
			
			currentEntity = ((EntityData)fixture.getBody().getUserData()).getEntity();
			
			int i=0;
			while(i<entityTypes.length && currentEntity.getClass() != entityTypes[i])i++;
			if(i<entityTypes.length){
				currentType = i;
			}else{
				currentEntity = null;
				currentType = 0;
			}
			return false;
		}
	};
	
	
	public MapEditingInputListener(Level level, World world, OrthographicCamera camera) {
		this.level = level;
		this.world = world;
		this.camera = camera;
	}
	
	
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 mousePoint = new Vector3(screenX, screenY, 0);
		mousePoint = camera.unproject(mousePoint);
		
		if(button == Buttons.LEFT){
			if(currentEntity == null){
			world.QueryAABB(callback, 
					mousePoint.x-10*Scene.BOX_TO_WORLD, mousePoint.y-10*Scene.BOX_TO_WORLD, 
					mousePoint.x+10*Scene.BOX_TO_WORLD, mousePoint.y+10*Scene.BOX_TO_WORLD
				);
			}else{
				currentEntity = null;
			}
		}else if(button == Buttons.RIGHT){
			Level.getPlayer().setCenter(mousePoint.x*Scene.WORLD_TO_BOX, mousePoint.y*Scene.WORLD_TO_BOX);
		}else if(button == Buttons.MIDDLE){
			if(currentEntity!=null){
				currentEntity.dispose();
				level.getActiveEntities().removeValue(currentEntity, true);
				currentType = 0;
				currentEntity = null;
			}
		}
		
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 mousePoint = new Vector3(screenX, screenY, 0);
		mousePoint = camera.unproject(mousePoint);
		
		if(currentEntity!=null){
			currentEntity.setCenter(mousePoint.x*Scene.WORLD_TO_BOX, mousePoint.y*Scene.WORLD_TO_BOX);
			currentEntity.recalculate();
		}
		
		
		return super.mouseMoved(screenX, screenY);
	}
	
	
	@Override
	public boolean scrolled(int amount) {
		amount*=-1;
		if(currentEntity!=null){
			switch (currentMode) {
				case ROTATE:
					currentEntity.setAngle(currentEntity.getAngle()+5*amount*MathUtils.degRad);
					break;
				case SCALEXY:
					currentEntity.setScaleX(currentEntity.getScaleX()+0.05f*amount);
					currentEntity.setScaleY(currentEntity.getScaleY()+0.05f*amount);
					break;
				case SCALEX:
					currentEntity.setScaleX(currentEntity.getScaleX()+0.05f*amount);
					break;
				case SCALEY:
					currentEntity.setScaleY(currentEntity.getScaleY()+0.05f*amount);
					break;
				case EDIT_PROP1:
					currentEntity.setProp1(currentEntity.getProp1().value+amount);
					break;
				case EDIT_PROP2:
					currentEntity.setProp2(currentEntity.getProp2().value+amount);
					break;
				case EDIT_PROP3:
					currentEntity.setProp3(currentEntity.getProp3().value+amount);
					break;

			}
			currentEntity.recalculate();
		}
		
		
		return super.scrolled(amount);
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		Vector3 mousePoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		mousePoint = camera.unproject(mousePoint);
		
		switch (keycode) {
			case Keys.NUM_1:
				currentMode = EditingModes.ROTATE;
				break;
			case Keys.NUM_2:
				currentMode = EditingModes.SCALEXY;
				break;
			case Keys.NUM_3:
				currentMode = EditingModes.SCALEX;
				break;
			case Keys.NUM_4:
				currentMode = EditingModes.SCALEY;
				break;
			case Keys.NUM_5:
				currentMode = EditingModes.EDIT_PROP1;
				break;
			case Keys.NUM_6:
				currentMode = EditingModes.EDIT_PROP2;
				break;
			case Keys.NUM_7:
				currentMode = EditingModes.EDIT_PROP3;
				break;
				
			case Keys.NUM_0:
				Json j = new Json();
				FileHandle levelFile = Gdx.files.external("/Desktop/levels/"+level.getLevelName()+".json");
				LevelModel model = new LevelModel();
				
				if(levelFile.exists()){
					levelFile.delete();
				}
				
				for(Entity e : level.getActiveEntities()){
					model.addEntity(e);
				}
				
				levelFile.writeString(j.prettyPrint(model), true);
				
				
				break;
				
			case Keys.N:
					try {
						currentEntity =  (Entity) entityTypes[0].getConstructor(World.class).newInstance(world);
						level.getActiveEntities().add(currentEntity);
						currentType = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
				break;
				
			case Keys.PLUS:
				if(currentEntity!=null){
					currentEntity.dispose();
					level.getActiveEntities().removeValue(currentEntity, true);
					try{
						currentType = (currentType+1)%entityTypes.length;
						currentEntity = (Entity) entityTypes[currentType].getConstructor(World.class).newInstance(world);
						level.getActiveEntities().add(currentEntity);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
				
			case Keys.MINUS:
				if(currentEntity!=null){
					currentEntity.dispose();
					level.getActiveEntities().removeValue(currentEntity, true);
					try{
						currentType--;
						if(currentType<0){
							currentType = entityTypes.length-1;
						}
						currentEntity = (Entity) entityTypes[currentType].getConstructor(World.class).newInstance(world);
						level.getActiveEntities().add(currentEntity);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case Keys.ENTER:
				debug = !debug;
				break;
		}
		
		if(currentEntity!=null){
			currentEntity.setCenter(mousePoint.x*Scene.WORLD_TO_BOX, mousePoint.y*Scene.WORLD_TO_BOX);
			currentEntity.recalculate();
		}
		
		return super.keyDown(keycode);
	}
	
	
	public void render(SpriteBatch batch){
		batch.setProjectionMatrix(projection);
		batch.begin();
			if(currentEntity!=null){
				font.drawMultiLine(batch, 
						"Position:\n   " +
								"x: "+ currentEntity.getCenter().x + "\n   " +
								"y: " + currentEntity.getCenter().y + "\n" + 
						"Angle: " + currentEntity.getAngle()*MathUtils.radDeg + "\n" +
						"Scaling:\n   " +
								"x: " + currentEntity.getScaleX() + "\n   " + 
								"y: " + currentEntity.getScaleY() + "\n" +
						currentEntity.getProp1().name + ": " + currentEntity.getProp1().value + "\n" + 
						currentEntity.getProp2().name + ": " + currentEntity.getProp2().value + "\n" + 
						currentEntity.getProp3().name + ": " + currentEntity.getProp3().value + "\n" +
						currentEntity.getExternalBody1().name + "ID: " + currentEntity.getExternalBody1().value + "\n" +
						currentEntity.getExternalBody2().name + "ID: " + currentEntity.getExternalBody2().value + "\n" ,
								
						820, 220);
				
				
			}
		batch.end();
	}
	
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
	}
	
	public boolean isDebugging(){
		return debug;
	}
	

}
