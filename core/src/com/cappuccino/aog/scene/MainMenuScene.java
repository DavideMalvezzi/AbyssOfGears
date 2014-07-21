package com.cappuccino.aog.scene;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cappuccino.aog.AOGGame;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;
import com.cappuccino.aog.Scissor;
import com.cappuccino.aog.game.VParallaxLayer;
import com.cappuccino.aog.levels.MainMenuLevel;
import com.cappuccino.aog.mapeditor.MapEditor;
import com.cappuccino.aog.scene.menus.ChainedContetxMenuContainer;

public class MainMenuScene extends Scene {
	
	private Stage stage;
	private MainMenuLevel level;
	private VParallaxLayer bg0,bg1,bg2;
	
	private ChainedContetxMenuContainer contextMenu;
	
	private MapEditor editor;
	
	public MainMenuScene() {
		super();
		OrthographicCamera cam = new OrthographicCamera();
		ExtendViewport view = new ExtendViewport(SCENE_W, SCENE_H, cam);
		stage = new Stage(view);
		
		cam.zoom = SCENE_H/view.getWorldHeight();
		cam.position.set(view.getWorldWidth()/2, view.getWorldHeight()/2, 0);
		
		float traslX = (view.getWorldWidth()*cam.zoom-SCENE_W)/2;
		float traslY = (view.getWorldHeight()-SCENE_H)/2;
		
		Gdx.input.setInputProcessor(stage);
		
		//Main Menù
		final ImageButton achive = new ImageButton(Assets.hudSkin.getDrawable("AchievementButton"));
		final ImageButton shop = new ImageButton(Assets.hudSkin.getDrawable("ShopButton"));
		final ImageButton stats = new ImageButton(Assets.hudSkin.getDrawable("StatsButton"));
		
		
		shop.setPosition((stage.getWidth()-shop.getWidth())/2, 5+traslY );
		achive.setPosition(shop.getX()*1.3f, 5+traslY);
		stats.setPosition(shop.getX()*0.7f, 5+traslY);
		
		
		achive.setTransform(true);
		achive.setOrigin(achive.getWidth()/2, achive.getHeight()/2);
		achive.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				contextMenu.showMenu(achive, 0);
			}
		});
		
		shop.setTransform(true);
		shop.setOrigin(shop.getWidth()/2, shop.getHeight()/2);
		shop.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				contextMenu.showMenu(shop, 1);
			}
		});
		
		stats.setTransform(true);
		stats.setOrigin(stats.getWidth()/2, stats.getHeight()/2);
		stats.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				contextMenu.showMenu(stats, 2);
			}
		});
		
		LabelStyle playStyle = new LabelStyle(Assets.font100, Color.BLACK);
		final Label play = new Label("Play", playStyle);
		play.setPosition((stage.getWidth()-play.getWidth())/2, 200+traslY);//0.27f * view.getWorldHeight());
		play.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				AOGGame.changeScene(new GameScene());
				/*
				level.activePress();
				play.addAction(Actions.forever(Actions.run(new Runnable() {
					boolean isStarted = false;
					public void run() {
						if(level.pressFinish() && !isStarted){
							isStarted = true;
							level.disactivePress();
							stage.addAction(
									Actions.sequence(
											Actions.fadeOut(0.5f),
											Actions.delay(0.3f),
											Actions.moveBy(0, SCENE_H*2, 1.5f),
											Actions.fadeIn(0.5f)
									));
						}
					}
				})));
				*/
				
			}
		});
		
		LabelStyle titleStyle = new LabelStyle(Assets.font200, Color.BLACK);
		Label titlep1 = new Label("Abyss of", titleStyle);
		titlep1.setPosition((stage.getWidth()-titlep1.getWidth())/2, 555+traslY);
		Label titlep2 = new Label("Gears", titleStyle);
		titlep2.setPosition((stage.getWidth()-titlep2.getWidth())/2, 440+traslY);
		
		LabelStyle infoStyle = new LabelStyle(Assets.font64, Color.WHITE);
		Label info = new Label("?", infoStyle);
		info.setPosition(0.95f*stage.getWidth()+traslX, traslY);
		info.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				
			}
		});
		
		stage.addActor(achive);
		stage.addActor(shop);
		stage.addActor(stats);
		stage.addActor(info);
		
		stage.addActor(play);
		stage.addActor(titlep1);
		stage.addActor(titlep2);
		
		//Menù play
		final ImageButton back = new ImageButton(Assets.hudSkin.getDrawable("BackButton"));
		back.setPosition(5-traslX, -815+traslY);
		back.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(!back.isDisabled()){
					level.openPress();
					stage.addAction(
						Actions.sequence(
								Actions.fadeOut(0.5f),
								Actions.delay(0.3f),
								Actions.moveBy(0, -SCENE_H*2, 1.5f),
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
		
		contextMenu = new ChainedContetxMenuContainer(world, stage, traslX, traslY);
		level = new MainMenuLevel(world);
		
		bg0 = new VParallaxLayer(Assets.layer0Background);
		bg1 = new VParallaxLayer(Assets.layer1Background);
		bg2 = new VParallaxLayer(Assets.layer2Background);
		
		//editor = new MapEditor(level, world, camera);
		
	}
	
	
	
	public void render(float delta) {
		beginClip();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
				bg0.render(batch, camera);
				bg1.render(batch, camera);
				bg2.render(batch, camera);
				
				level.render(batch);
			batch.end();
			
			stage.draw();
		endClip();
		
		//rayHandler.setCombinedMatrix(camera.combined.scl(WORLD_TO_BOX));
		//rayHandler.render();
		
		//box2dDebug.render(world, camera.combined.scl(WORLD_TO_BOX));
		//box2dDebug.render(world, stage.getCamera().combined.scl(WORLD_TO_BOX));
		
		
		
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
		
		//editor.draw(batch);
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
			bg1.update(delta, -speedY*2);
			bg2.update(delta, -speedY);
		}
		
		
		//editor.update();
	}
	
	
	public void dispose() {
		stage.dispose();
		batch.dispose();
		level.dispose();
		rayHandler.dispose();
		world.dispose();
	}

}
