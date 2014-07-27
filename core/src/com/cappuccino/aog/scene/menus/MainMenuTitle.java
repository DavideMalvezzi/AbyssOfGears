package com.cappuccino.aog.scene.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.ActorEntities.PlayEntity;
import com.cappuccino.aog.entities.ActorEntities.TitleEntity;
import com.cappuccino.aog.entities.Chain;
import com.cappuccino.aog.entities.JointsFactory;

public class MainMenuTitle extends Actor{

	private Chain c1,c2,c3,c4;//,c5,c6;
	private TitleEntity titleEntity;
	private PlayEntity playEntity;
	private Label play;
	
	private Stage stage;
	
	public MainMenuTitle(World world, Stage stage, float traslX, float traslY) {
		this.stage = stage;
		LabelStyle titleStyle = new LabelStyle(Assets.font100, Color.WHITE);
		play = new Label("Play", titleStyle);
		
		titleEntity = new TitleEntity(world);
		titleEntity.setCenter(stage.getWidth()/2, stage.getHeight()*0.83f-traslY);
		
		playEntity = new PlayEntity(world);
		playEntity.setCenter(stage.getWidth()/2, stage.getHeight()*0.45f-traslY);
		
		c1 = new Chain(world, titleEntity.getCenter().x-titleEntity.getWidth()/2*0.8f, 1.123f*stage.getHeight()-traslY, 6, 0.5f, -90*MathUtils.degRad);
		c2 = new Chain(world, titleEntity.getCenter().x+titleEntity.getWidth()/2*0.8f, 1.123f*stage.getHeight()-traslY, 6, 0.5f, -90*MathUtils.degRad);
		c3 = new Chain(world, titleEntity.getCenter().x , 1.123f * stage.getHeight()-traslY, 6, 0.5f, -90*MathUtils.degRad);
		
		c4 = new Chain(world, titleEntity.getCenter().x, titleEntity.getCenter().y-titleEntity.getHeight()/2-traslY, 5, 0.5f, -90*MathUtils.degRad);
	//	c5 = new Chain(world, titleEntity.getCenter().x+playEntity.getWidth()/2*0.6f,titleEntity.getCenter().y-titleEntity.getHeight()/2-traslY, 5, 0.5f, -90*MathUtils.degRad);
	//	c6 = new Chain(world, titleEntity.getCenter().x-playEntity.getWidth()/2*0.6f, titleEntity.getCenter().y-titleEntity.getHeight()/2-traslY, 5, 0.5f, -90*MathUtils.degRad);
		
		c1.attachEntity(titleEntity, new Vector2(-titleEntity.getWidth()/2*0.8f, 0));
		c2.attachEntity(titleEntity, new Vector2(titleEntity.getWidth()/2*0.8f, 0));
		c3.attachEntity(titleEntity, Vector2.Zero);
		
		c4.attachEntity(playEntity, Vector2.Zero);
	//	c5.attachEntity(playEntity, new Vector2(playEntity.getWidth()*0.3f, 0));
	//	c6.attachEntity(playEntity, new Vector2(-playEntity.getWidth()*0.3f, 0));
		
		JointsFactory.createDistanceJoint(world, c4, titleEntity, Vector2.Zero, Vector2.Zero, titleEntity.getHeight()*0.55f, false);
	//	JointsFactory.createWeldJoint(world, c5, titleEntity, Vector2.Zero, new Vector2(playEntity.getWidth()*0.65f, -titleEntity.getHeight()*0.55f));
	//	JointsFactory.createWeldJoint(world, c6, titleEntity, Vector2.Zero, new Vector2(-playEntity.getWidth()*0.65f, -titleEntity.getHeight()*0.55f));
	
		stage.addActor(this);
		stage.addActor(play);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//batch.setColor(stage.getRoot().getColor()); //Risolve errore fade
		c1.draw((SpriteBatch) batch);
		c2.draw((SpriteBatch) batch);
		c4.draw((SpriteBatch) batch);
		titleEntity.draw((SpriteBatch) batch);
		playEntity.draw((SpriteBatch) batch);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		play.setPosition(playEntity.getCenter().x-play.getWidth()/2, playEntity.getCenter().y-play.getHeight());
	}
	
	public void show(){
		addAction(
				Actions.sequence(
						Actions.moveBy(0, -stage.getHeight()*Scene.BOX_TO_WORLD, 1f),
						Actions.moveBy(stage.getWidth()*Scene.BOX_TO_WORLD, 0, 1.5f),
						Actions.moveBy(0, 0)
				));
	}
	public void hide(){
		addAction(
				Actions.sequence(
						Actions.moveBy(0, stage.getHeight()*Scene.BOX_TO_WORLD, 1f),
						Actions.moveBy(-stage.getWidth()*Scene.BOX_TO_WORLD, 0, 0.5f),
						Actions.moveBy(0, 0)
				));
	}
	
	@Override
	public void moveBy(float x, float y) {
		float t = 1f/60f;
		float velX = x/t;
		float velY = y/t;
		
		super.moveBy(x, y);
		c1.setLinearVelocity(velX, velY);
		c2.setLinearVelocity(velX, velY);
		c3.setLinearVelocity(velX, velY);
		c4.setLinearVelocity(velX, velY);
	//	c5.setLinearVelocity(velX, velY);
	//	c6.setLinearVelocity(velX, velY);
	}
	
	
	public Label getPlayLabel(){
		return play;
	}
	
}
