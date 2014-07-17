package com.cappuccino.aog.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.levels.Level;
import com.cappuccino.aog.mapeditor.EntityModel;
import com.cappuccino.aog.scene.GameScene;

public class SmokeEmitter extends Entity {

	private static final float smokeRadius = 250;
	private static final float maxForce = 500;
	private static int emittersNumber = 0;
	
	private Vector2 emissionPoint;
	
	private final RayCastCallback callback = new RayCastCallback() {
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			String fixName = (String)fixture.getUserData();
			Alexy alexy = Level.getPlayer();
			float dstPercent;
			
			if(fixName.contains("Player")){
				dstPercent = 1-(alexy.getCenter().dst(emissionPoint)/smokeRadius);
				alexy.getBody().applyForceToCenter(maxForce*MathUtils.cos(getAngle())*dstPercent, maxForce*MathUtils.sin(getAngle())*dstPercent, true);
				return 0;
			}else if(fixName.contains("Umbrella")){
				dstPercent = 1-(alexy.umbrella.getCenter().dst(emissionPoint)/smokeRadius);
				alexy.umbrella.getBody().applyForceToCenter(maxForce*MathUtils.cos(getAngle())*dstPercent, maxForce*MathUtils.sin(getAngle())*dstPercent, true);
				return 0;
			}
			return 1;
		}
	};
	
	public SmokeEmitter(World world) {
		this(world, 0, 0, 0);
	}
	
	public SmokeEmitter(World world, EntityModel model) {
		this(world, model.position.x, model.position.y, model.angle);
	}
	
	
	
	public SmokeEmitter(World world, float x, float y, float angle) {
		super("SmokeEmitter", world);
		initBody(world, BodyType.StaticBody);
		initFixtures();
		
		setAngle(angle);
		setCenter(x,y);
		
		emissionPoint = new Vector2(getCenter().add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle())));
		emittersNumber++;
	}
	
	
	@Override
	public void initFixtures() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = ENTITY;
		fd.filter.maskBits = ENTITY_MASK;
		
		bodyLoader.attachFixture(body, "SmokeEmitter", fd, getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD);
		
		origin.set(bodyLoader.getOrigin("SmokeEmitter", getRealWidth()*scaleX*GameScene.BOX_TO_WORLD, getRealWidth()*scaleY*GameScene.BOX_TO_WORLD));
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		Assets.smokeEffect.setPosition(emissionPoint.x, emissionPoint.y);
		
		Assets.smokeEffect.getAngle().setLow(getAngle()*MathUtils.radDeg-45, getAngle()*MathUtils.radDeg+45);
		Assets.smokeEffect.getAngle().setHigh(getAngle()*MathUtils.radDeg);
		
		Assets.smokeEffect.update(Gdx.graphics.getDeltaTime()/emittersNumber);
		Assets.smokeEffect.draw(batch);
		
	}
	
	
	@Override
	public void update(float delta) {
		Vector2 start = emissionPoint.cpy().scl(GameScene.BOX_TO_WORLD);
		Vector2 end = emissionPoint.cpy().add(smokeRadius*MathUtils.cos(getAngle()), smokeRadius*MathUtils.sin(getAngle())).scl(GameScene.BOX_TO_WORLD);
		
		body.getWorld().rayCast(callback, start, end);
		
	}
	
	@Override
	public void recalculate(){
		emissionPoint = new Vector2(getCenter().add(getWidth()*MathUtils.cos(getAngle()), getWidth()*MathUtils.sin(getAngle())));
	}
	
	@Override
	public void active() {
		emittersNumber++;
	}
	
	@Override
	public void disactive() {
		emittersNumber--;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		emittersNumber--;
	}
	
	
	
	
	
	

	
	
}
