package com.cappuccino.aog.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cappuccino.aog.AOGGame;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.game.ParallaxLayer;
import com.cappuccino.aog.levels.MainMenuLevel;
import com.cappuccino.aog.scene.actors.ChainedContetxMenuContainer;

public class MainMenuScene extends Scene {
	
	private Stage stage;
	private MainMenuLevel level;
	private ParallaxLayer bg1,bg2;
	
	private ImageButton achive, shop, stats;
	
	private ChainedContetxMenuContainer menu;
	
	public MainMenuScene() {
		super();
		camera.position.y = SCENE_H/2*BOX_TO_WORLD;
		
		OrthographicCamera cam = new OrthographicCamera(Scene.SCENE_W, Scene.SCENE_H);
		ExtendViewport view = new ExtendViewport(Scene.SCENE_W, Scene.SCENE_H, cam);
		
		stage = new Stage(view);
		Gdx.input.setInputProcessor(stage);
		
		
		achive = new ImageButton(Assets.hudSkin.getDrawable("AchievementButton"));
		shop = new ImageButton(Assets.hudSkin.getDrawable("ShopButton"));
		stats = new ImageButton(Assets.hudSkin.getDrawable("StatsButton"));
		
		achive.setPosition(0.3041f * stage.getWidth(), 0.007f * stage.getHeight());
		shop.setPosition(0.4468f * stage.getWidth(), 0.007f * stage.getHeight());
		stats.setPosition(0.5885f * stage.getWidth(), 0.007f * stage.getHeight());
		
		
		achive.setTransform(true);
		achive.setOrigin(achive.getWidth()/2, achive.getHeight()/2);
		achive.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				menu.showMenu(achive, 0);
			}
		});
		
		shop.setTransform(true);
		shop.setOrigin(shop.getWidth()/2, shop.getHeight()/2);
		shop.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				menu.showMenu(shop, 1);
			}
		});
		
		stats.setTransform(true);
		stats.setOrigin(stats.getWidth()/2, stats.getHeight()/2);
		stats.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				menu.showMenu(stats, 2);
			}
		});
		
		LabelStyle labelStyle = new LabelStyle(Assets.font100, Color.BLACK);
		Label play = new Label("Play", labelStyle);
		play.setPosition((stage.getWidth()-play.getWidth())/2, 0.31f * stage.getHeight());
		play.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				AOGGame.changeScene(new GameScene());
			}
		});
		
		LabelStyle titleStyle = new LabelStyle(Assets.font200, Color.BLACK);
		Label titlep1 = new Label("Abyss of", titleStyle);
		titlep1.setPosition((stage.getWidth()-titlep1.getWidth())/2, 0.77f * stage.getHeight());
		Label titlep2 = new Label("Gears", titleStyle);
		titlep2.setPosition((stage.getWidth()-titlep2.getWidth())/2, 0.61f * stage.getHeight());
		
		
		menu = new ChainedContetxMenuContainer(world, stage);
		
		stage.addActor(achive);
		stage.addActor(shop);
		stage.addActor(stats);
		
		stage.addActor(play);
		stage.addActor(titlep1);
		stage.addActor(titlep2);
		
		level = new MainMenuLevel(world);
		
		bg1 = new ParallaxLayer(Assets.layer1Background, 160);
		bg2 = new ParallaxLayer(Assets.layer2Background, 160);
	}
	
	
	
	public void render(float delta) {
		beginDraw();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
				batch.draw(Assets.layer0Background, 0, -160);
				bg1.render(batch, camera);
				bg2.render(batch, camera);
				
				level.render(batch);
			batch.end();
			
			stage.draw();
			
		endDraw();
		
		camera.combined.scl(WORLD_TO_BOX);
		//box2dDebug.render(world, stage.getCamera().combined.scl(WORLD_TO_BOX));
		
		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.1f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.1f;
		
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
	}
	
	
	public void update(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		((ExtendViewport)stage.getViewport()).update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		bg1.update(delta, 0.5f);
		bg2.update(delta, 0.25f);
		level.update(delta);
		world.step(1/60f, 8, 3);
		stage.act(delta);
	}
	
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		((ExtendViewport)stage.getViewport()).update(width, height);
	}
	
	public void dispose() {
		batch.dispose();
		level.dispose();
		rayHandler.dispose();
		world.dispose();
	}

}
