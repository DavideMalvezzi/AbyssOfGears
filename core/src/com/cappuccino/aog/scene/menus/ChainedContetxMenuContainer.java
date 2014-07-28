package com.cappuccino.aog.scene.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.entities.ActorEntities.ContetxMenuEntity;
import com.cappuccino.aog.entities.Chain;

public class ChainedContetxMenuContainer extends Actor {

	private Chain c1, c2, c3;
	private ContetxMenuEntity menuBg;
	
	private ImageButton back;
	private Stage stage;
	private ContextMenu contextMenu;
	
	public ChainedContetxMenuContainer(World world, Stage stage, float traslX, float traslY) {
		 this.stage = stage;
		 menuBg = new ContetxMenuEntity(world);
		 menuBg.setCenter(stage.getWidth()*0.5f, 1.65f*stage.getHeight()-traslY);
		 
		 c1 = new Chain(world, menuBg.getCenter().x-menuBg.getWidth()/2*0.85f , 2*stage.getHeight()-traslY, 7, 0.5f, -90*MathUtils.degRad);
		 c2 = new Chain(world, menuBg.getCenter().x+menuBg.getWidth()/2*0.85f , 2*stage.getHeight()-traslY, 7, 0.5f, -90*MathUtils.degRad);
		 c3 = new Chain(world, menuBg.getCenter().x , 2*stage.getHeight()-traslY, 7, 0.5f, -90*MathUtils.degRad);
		
		 c1.attachEntity(menuBg, new Vector2(-menuBg.getWidth()/2*0.85f, 0));
		 c2.attachEntity(menuBg, new Vector2(menuBg.getWidth()/2*0.85f, 0));
		 c3.attachEntity(menuBg, new Vector2());
		 
		 back = new ImageButton(Assets.hudSkin.getDrawable("BackButton"));
		 back.setPosition(0.01f * stage.getWidth()-traslX, stage.getHeight()-back.getHeight()-traslY);
		 back.setOrigin(back.getWidth()/2, back.getHeight()/2);
		 back.setTransform(true);
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		c1.draw((SpriteBatch) batch);
		c2.draw((SpriteBatch) batch);
		menuBg.draw((SpriteBatch) batch);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		contextMenu.setPosition(menuBg.getX(), menuBg.getY());
		contextMenu.setRotation(menuBg.getAngle()*MathUtils.radDeg);
	}
	
	public void showMenu(Actor sender, int contexMenuType){
		sender.addAction(Actions.repeat(5, Actions.rotateBy(360, 0.2f)));
		stage.addActor(this);
		contextMenu = new AchievementContextMenu(this);
		
		addAction(
				Actions.sequence(
						Actions.moveBy(stage.getWidth()*Scene.BOX_TO_WORLD, 0, 1f), 
						Actions.moveBy(-stage.getWidth()*Scene.BOX_TO_WORLD, 0, 0.5f), 
						Actions.moveBy(0, -stage.getHeight()*Scene.BOX_TO_WORLD, 1f), 
						Actions.moveBy(0, 0),	//stop
						Actions.run(new Runnable() {
							public void run() {
								stage.addActor(back);
								back.addAction(Actions.fadeIn(0.5f));
							}
						})
				));
	}
	
	public void hideMenu(){
		addAction(
				Actions.sequence(
						Actions.moveBy(0, stage.getHeight()*Scene.BOX_TO_WORLD, 1), 
						Actions.moveBy(0, 0),
						Actions.run(new Runnable() {
							public void run() {
								back.setDisabled(false);
								contextMenu.remove();
								remove();
							}
						})
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
		menuBg.setLinearVelocity(velX, velY);
	}
	
	public ImageButton getBackButton(){
		return back;
	}
	
	
	//Per dimensione contexMenu
	@Override
	public float getWidth() {
		return menuBg.getWidth();
	}
	
	@Override
	public float getHeight() {
		return menuBg.getHeight();
	}
	
}
