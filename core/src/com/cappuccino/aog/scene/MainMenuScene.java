package com.cappuccino.aog.scene;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cappuccino.aog.AOGGame;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.Scissor;
import com.cappuccino.aog.ShaderLibrary;
import com.cappuccino.aog.game.VParallaxLayer;
import com.cappuccino.aog.levels.MainMenuLevel;
import com.cappuccino.aog.mapeditor.MapEditor;
import com.cappuccino.aog.scene.menus.ChainedContetxMenuContainer;
import com.cappuccino.aog.scene.menus.MainMenuTitle;

public class MainMenuScene extends Scene {
	
	private Stage stage;
	private MainMenuLevel level;
	private VParallaxLayer bg0,bg1;
	
	private MainMenuTitle menuTitle;
	private ChainedContetxMenuContainer contextMenu;
	
	private MapEditor editor;
	
	public MainMenuScene() {
		super();
		OrthographicCamera cam = new OrthographicCamera();
		ExtendViewport view = new ExtendViewport(SCENE_W, SCENE_H, cam);
		stage = new Stage(view);
		stage.getBatch().setShader(ShaderLibrary.softLight);
		Gdx.input.setInputProcessor(stage);
		
		cam.zoom = SCENE_H/view.getWorldHeight();
		cam.position.set(view.getWorldWidth()/2, view.getWorldHeight()/2, 0);
		
		float traslX = (view.getWorldWidth()*cam.zoom-SCENE_W)/2;
		float traslY = (view.getWorldHeight()-SCENE_H)/2;
		
		level = new MainMenuLevel(world);
		
		menuTitle = new MainMenuTitle(world, stage, traslX, traslY);
		final Label play = menuTitle.getPlayLabel();
		play.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				//AOGGame.changeScene(new GameScene());
				if(play.getActions().size<=0){
					play.addAction(Actions.forever(Actions.run(new Runnable() {
						boolean isStarted = false;
						public void run() {
							if(!isStarted){
								isStarted = true;
								stage.addAction(
									Actions.sequence(
											Actions.fadeOut(0.5f),
											Actions.delay(0.3f),
											Actions.moveBy(0, SCENE_H*2, 1.2f),
											Actions.fadeIn(0.5f),
											Actions.run(new Runnable() {
												public void run() {
													play.getActions().clear();													
												}
											})
										));
							}
						}
					})));
				}
				
			}
		});
		
		contextMenu = new ChainedContetxMenuContainer(world, stage, traslX, traslY);
		loadMainMenu(traslX, traslY);
		
		//Menù play
		loadSelectLevelMenu(traslX, traslY);
		
	
		bg0 = new VParallaxLayer(Assets.layer0Background);
		bg1 = new VParallaxLayer(Assets.layer1Background);
		
		//editor = new MapEditor(level, world, camera);
		
	}
	
	
	
	private void loadSelectLevelMenu(float traslX, float traslY) {
		final ImageButton back = new ImageButton(Assets.hudSkin.getDrawable("BackButton"));
		back.setPosition(5-traslX, -825+traslY);
		back.setOrigin(back.getWidth()/2, back.getHeight()/2);
		back.setTransform(true); 
		back.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(!back.isDisabled()){
					back.addAction(Actions.repeat(5, Actions.rotateBy(360, 0.2f)));
					stage.addAction(
						Actions.sequence(
								Actions.fadeOut(0.5f),
								Actions.delay(0.3f),
								Actions.moveBy(0, -SCENE_H*2, 1.2f),
								Actions.fadeIn(0.5f),
								Actions.run(new Runnable() {
									public void run() {
										back.setDisabled(false);
									}
								})
						));
				}
				back.setDisabled(true);
				
			}
		});
		
		stage.addActor(back);
		
	}

	private void loadMainMenu(float traslX, float traslY) {
		//Main Menù
		final Button achive = new Button(Assets.hudSkin.getDrawable("AchievementButton"));
		final Button shop = new Button(Assets.hudSkin.getDrawable("ShopButton"));
		final Button stats = new Button(Assets.hudSkin.getDrawable("StatsButton"));
		final Button info = new Button(Assets.hudSkin.getDrawable("InfoButton"));
		final ClickListener onClick = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Button sender = (Button) event.getTarget();
				if(!sender.isDisabled()){
					menuTitle.hide();
					contextMenu.showMenu(sender, (Integer)sender.getUserObject());
					
					disableAndFadeOut(stats);
					disableAndFadeOut(achive);
					disableAndFadeOut(shop);
					disableAndFadeOut(info);
				}
			}
		};
				
		shop.setPosition((stage.getWidth()-shop.getWidth())/2, 5+traslY);
		shop.setTransform(true);
		shop.setOrigin(shop.getWidth()/2, shop.getHeight()/2);
		shop.addListener(onClick);
		shop.setUserObject(1);
		
		achive.setPosition(shop.getX()*0.7f, 5+traslY);
		achive.setTransform(true);
		achive.setOrigin(achive.getWidth()/2, achive.getHeight()/2);
		achive.addListener(onClick);
		achive.setUserObject(0);
		
		stats.setPosition(shop.getX()*1.3f, 5+traslY);
		stats.setTransform(true);
		stats.setOrigin(stats.getWidth()/2, stats.getHeight()/2);
		stats.addListener(onClick);	
		stats.setUserObject(2);
	
		info.setPosition(0.93f*stage.getWidth()+traslX, traslY);
		info.setTransform(true);
		info.setOrigin(info.getWidth()/2, info.getHeight()/2);
		info.addListener(onClick);
		info.setUserObject(3);
		
		stage.addActor(achive);
		stage.addActor(shop);
		stage.addActor(stats);
		stage.addActor(info);
		
		
		final ImageButton contextBackButton = contextMenu.getBackButton();
		contextBackButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(!contextBackButton.isDisabled()){
					contextMenu.hideMenu();
					menuTitle.show();
					contextBackButton.addAction(Actions.sequence(
							Actions.repeat(5, Actions.rotateBy(360, 0.2f)),
							Actions.delay(1f),
							Actions.run(new Runnable() {
								public void run() {
									enableAndFadeIn(stats);
									enableAndFadeIn(achive);
									enableAndFadeIn(shop);
									enableAndFadeIn(info);
									contextBackButton.remove();
								}
							})
					));
					contextBackButton.setDisabled(true);
				}
			}
		 });
	}

	private void disableAndFadeOut(Button a){
		a.setDisabled(true);
		a.addAction(Actions.fadeOut(0.5f));
	}

	private void enableAndFadeIn(Button a){
		a.setDisabled(false);
		a.addAction(Actions.fadeIn(0.5f));
	}


	public void render(float delta) {
		beginClip();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
				ShaderLibrary.setSoftLightParams(level.getColor());
				bg0.render(batch, camera);
				bg1.render(batch, camera);
				level.render(batch);
				
			batch.end();
			
			ShaderLibrary.softLight.begin();
			ShaderLibrary.setSoftLightParams(level.getColor());
			stage.draw();
			
			
			
		endClip();
		
		//rayHandler.setCombinedMatrix(camera.combined.scl(WORLD_TO_BOX));
		//rayHandler.render();
		
		//box2dDebug.render(world, camera.combined.scl(WORLD_TO_BOX));
		box2dDebug.render(world, stage.getCamera().combined.scl(WORLD_TO_BOX));
		
		
		
		if(Gdx.input.isKeyPressed(Keys.SPACE))camera.zoom+=0.01f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))camera.zoom-=0.01f;
		if(Gdx.input.isKeyPressed(Keys.SPACE))((OrthographicCamera)stage.getCamera()).zoom+=0.01f;
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))((OrthographicCamera)stage.getCamera()).zoom-=0.01f;
		
		/*
		if(Gdx.input.isKeyPressed(Keys.A))camera.position.x-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.D))camera.position.x+=0.2f;
		if(Gdx.input.isKeyPressed(Keys.S))camera.position.y-=0.2f;
		if(Gdx.input.isKeyPressed(Keys.W))camera.position.y+=0.2f;
		*/
		
		
		if(Gdx.input.isKeyPressed(Keys.A))stage.getCamera().position.x-=4f;
		if(Gdx.input.isKeyPressed(Keys.D))stage.getCamera().position.x+=4f;
		if(Gdx.input.isKeyPressed(Keys.S))stage.getCamera().position.y-=4f;
		if(Gdx.input.isKeyPressed(Keys.W))stage.getCamera().position.y+=4f;
		
		if(editor!=null){
			editor.draw(batch);
		}
	}
	
	
	public void update(float delta) {
		camera.position.y = (-stage.getRoot().getY() * BOX_TO_WORLD + camera.viewportHeight/2)*camera.zoom;
		camera.update();
		
		level.update(delta);
		world.step(1f/60f, 8, 3);
		stage.act(delta);
		
		//currentPos-lastPos
		float speedY = (camera.position.y-camera.viewportHeight/2)-Scissor.getArea().y;
		
		if(speedY!=0){
			bg0.update(delta, -speedY);
			bg1.update(delta, -speedY*2);
		}
		
		
		if(editor!=null){
			editor.update();
		}
	}
	
	
	public void dispose() {
		stage.dispose();
		batch.dispose();
		level.dispose();
		rayHandler.dispose();
		world.dispose();
	}

}
