package com.cappuccino.aog.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
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

public class EditingModeInterface {
	
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
	
	
	private enum EntityEditingModes{
		ROTATE,SCALEXY,SCALEX,SCALEY,EDIT_PROP1,EDIT_PROP2,EDIT_PROP3;
	}
	
	private EntityEditingModes entityEditingCurrentMode = EntityEditingModes.ROTATE;
	
	private World world;
	private OrthographicCamera camera;
	private Level level;
	private static Entity currentEntity;
	private static int currentType;
	
	private static final Matrix4 projection = new Matrix4().setToOrtho(0, Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight(), 0, 10);
	private static final BitmapFont font = new BitmapFont();
	
	private final LevelModel levelModel = new LevelModel();
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
			}
			return false;
		}
	};
	
	
	

	public void onSxClick(Vector3 mousePoint){}
	public void onDxClick(Vector3 mousePoint){}
	public void onMidClick(Vector3 mousePoint){}
	public void onScroll(int amount){}
	public void onMouseMoved(int x, int y){}
	public void onKeyPress(int key){}
	public void draw(SpriteBatch batch){}
	
}
