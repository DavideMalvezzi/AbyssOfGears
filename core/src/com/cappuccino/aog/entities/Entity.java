package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.mapeditor.EntityModel.Property;
import com.cappuccino.aog.scene.GameScene;

public class Entity implements Disposable {

	public static final short PLAYER = 0x0002;
	public static final short WALL = 0x0004;
	public static final short ENTITY = 0x0008;
	public static final short LIGHT = 0x0010;
	public static final short BULLET = 0x0020;
	
	public static final short PLAYER_MASK = PLAYER | WALL | ENTITY | BULLET;
	public static final short WALL_MASK = PLAYER | LIGHT | BULLET;
	public static final short ENTITY_MASK = PLAYER | LIGHT | BULLET;
	public static final short LIGHT_MASK = LIGHT | ENTITY | WALL | BULLET | PLAYER;
	public static final short BULLET_MASK = LIGHT | ENTITY | WALL | PLAYER;
	
	protected static final BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("data/Bodies"));
	
	protected AtlasRegion texture;
	protected Body body;
	protected Vector2 origin,trasl;
	protected float scaleX,scaleY;
	protected boolean flipX,flipY;
	
	public Entity(String name) {
		texture = Assets.getTexture(name);
		scaleX = scaleY = 1;
		origin = new Vector2();
		trasl = new Vector2();
	}
	
	public Entity(String name, World world) {
		this(name);
	}
	
	
	protected void initBody(World world, BodyType type){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 0);
		bodyDef.type = type;
		bodyDef.allowSleep = true;
		
		body = world.createBody(bodyDef);
		body.setUserData(new EntityData(texture.name, this));
	}
	
	protected void initFixtures(){}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture.getTexture(),
				getX()+trasl.x,getY()+trasl.y,
				getOrigin().x, getOrigin().y,
				getWidth(), getHeight(), 
				1, 1, MathUtils.radiansToDegrees*getAngle(), 
				texture.getRegionX(), texture.getRegionY(), 
				(int)texture.getRegionWidth(), (int)texture.getRegionHeight(), 
				flipX, flipY
			);
	}

	
	public void update(float delta){}
	public void onCollide(Fixture sender, Fixture collided, Contact contact){}
	public void active(){}
	public void disactive(){}
	
	public void dispose(){
		if(body!=null){
			World world = body.getWorld();
			world.destroyBody(body);
		}
	} 

	
	//Texture properties
	public void setScaleX(float scaleX){
		this.scaleX = scaleX;
	}
	
	public void setScaleY(float scaleY){
		this.scaleY = scaleY;
	}
	
	public float getScaleX(){
		return scaleX;
	}
	
	public float getScaleY(){
		return scaleY;
	}
	
	public void setFlip(boolean flipX, boolean flipY){
		this.flipX = flipX;
		this.flipY = flipY;
	}
	
	public float getHeight(){
		return texture.getRegionHeight()*scaleY;
	}
	
	public float getWidth(){
		return texture.getRegionWidth()*scaleX;
	}
	
	public float getRealHeight(){
		return texture.getRegionHeight();
	}
	
	public float getRealWidth(){
		return texture.getRegionWidth();
	}
	
	public void setTraslX(float traslX){
		trasl.x = traslX;
	}
	
	public void setTraslY(float traslY){
		trasl.y = traslY;
	}
	
	//Properties
	public void recalculate(){}
	
	public void setProp1(float value){}
	public void setProp2(float value){}
	public void setProp3(float value){}
	public void setExternalBody1(Entity value){}
	public void setExternalBody2(Entity value){}
	
	public Property getProp1(){return new Property();}
	public Property getProp2(){return new Property();}
	public Property getProp3(){return new Property();}
	public Property getExternalBody1(){return new Property("null",-1);}
	public Property getExternalBody2(){return new Property("null",-1);}

	//Body properties
	public Body getBody(){
		return body;
	}
	
	public float getX(){
		return getPosition().x;
	}
	
	public float getY(){
		return getPosition().y;
	}
	
	public float getAngle(){
		return body.getAngle();
	}
	
	public void setAngle(float angle){
		body.setTransform(getCenter().scl(GameScene.BOX_TO_WORLD), angle);
	}
	
	public Vector2 getPosition(){
		return getCenter().sub(getOrigin());
	}
	
	public Vector2 getCenter(){
		return body.getPosition().scl(GameScene.WORLD_TO_BOX);
	}
	
	public Vector2 getOrigin(){
		return origin.cpy().scl(GameScene.WORLD_TO_BOX);
	}
	
	public void setPosition(Vector2 position){
		body.setTransform(position.cpy().scl(GameScene.BOX_TO_WORLD), getAngle());
	}
	
	public void setPosition(float x, float y){
		float xx = x*GameScene.BOX_TO_WORLD + origin.x;
		float yy = y*GameScene.BOX_TO_WORLD + origin.y;
		body.setTransform(xx, yy, getAngle());
	}
	
	public void setCenter(Vector2 position){
		body.setTransform(position.cpy().scl(GameScene.BOX_TO_WORLD), getAngle());
		
	}
	
	public void setCenter(float x, float y){
		float xx = x*GameScene.BOX_TO_WORLD;
		float yy = y*GameScene.BOX_TO_WORLD;
		body.setTransform(xx, yy, getAngle());
	}
	
	public void setOrigin(Vector2 origin){
		this.origin.set(origin);
	}
	
	public float getMass(){
		return body.getMass();
	}
	
	public void setMass(float mass){
		MassData data = body.getMassData();
		data.mass = mass;
		body.setMassData(data);
	}
	
	public Vector2 getLinearVelocity(){
		return body.getLinearVelocity();
	}
	
	public void setLinearVelocity(float velX, float velY){
		body.setLinearVelocity(velX, velY);
	}
	
	public void setAngularVelocity(float omega){
		body.setAngularVelocity(omega);
	}
	
	public float getAngularVelocity(){
		return body.getAngularVelocity();
	}
	
	public Filter getFilter(){
		return body.getFixtureList().first().getFilterData();
	}
	
	public void setFilter(Filter filter){
		Array<Fixture> fixtures = body.getFixtureList();
		for(int i=0; i<fixtures.size; i++){
			fixtures.get(i).setFilterData(filter);
		}
	}
	
	public void reloadFixtures(){
		Array<Fixture> fs = body.getFixtureList();
		for(Fixture f : fs){
			body.destroyFixture(f);
		}
		fs.clear();
		
		initFixtures();
	}
}
