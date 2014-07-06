package com.cappuccino.aog.scene;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cappuccino.aog.AOGGame;
import com.cappuccino.aog.Assets;
import com.cappuccino.aog.Scene;

public class GameSceneHud extends Stage{
	
	private static CheckBox useUmbrella;
	private TextButton resume, restart, exit;
	private ImageButton pause;
	
	private boolean isPaused;
	
	
	public GameSceneHud(){
		Gdx.input.setInputProcessor(this);
		
		OrthographicCamera cam = new OrthographicCamera(Scene.SCENE_W, Scene.SCENE_H);
		cam.position.set(Scene.SCENE_W/2, Scene.SCENE_H/2, 0);
		ExtendViewport view = new ExtendViewport(Scene.SCENE_W, Scene.SCENE_H, cam);
		view.setWorldSize(Scene.SCENE_W, Scene.SCENE_H);
		setViewport(view);
		cam.update();
		
		//Hud
		CheckBoxStyle checkStyle = new CheckBoxStyle();
		checkStyle.checkboxOff = Assets.hudSkin.getDrawable("umbrellaOff");
		checkStyle.checkboxOn = Assets.hudSkin.getDrawable("umbrellaOn");
		checkStyle.font = Assets.font64;
		useUmbrella = new CheckBox("", checkStyle);
		useUmbrella.setPosition(48, 10);
		
		ImageButtonStyle buttonStyle = new ImageButtonStyle();
		buttonStyle.imageUp = Assets.hudSkin.getDrawable("pause");
		buttonStyle.imageDown = Assets.hudSkin.getDrawable("pause");
		pause = new ImageButton(buttonStyle);
		pause.setPosition(848, 570);
		
		pause.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				showPauseMenu();
			}
		});
		
		addActor(useUmbrella);
		addActor(pause);
	
		
		//Menu
		TextButtonStyle textStyle = new TextButtonStyle();
		textStyle.font = Assets.font64;
		textStyle.fontColor = Color.BLACK;
		textStyle.up = Assets.hudSkin.getDrawable("tempButton");
		
		resume =  new TextButton(" Resume ", textStyle);
		resume.setPosition(-100, -100);
		resume.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				disposePauseMenu();
			}
		});
		restart =  new TextButton(" Restart ", textStyle);
		restart.setPosition(-100, -100);
		exit =  new TextButton(" Exit ", textStyle);
		exit.setPosition(-100, -100);
		exit.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				AOGGame.changeScene(new MainMenuScene());
			}
		});
		
		addActor(resume);
		addActor(restart);
		addActor(exit);
		
		
	}
	
	
	
	@Override
	public void act(float delta) {
		((ExtendViewport)getViewport()).update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		super.act(delta);
	}
	
	public void showPauseMenu(){
		isPaused = true;
		
		resume.addAction(Actions.moveTo(
				(getWidth()-resume.getWidth())/2, 
				getHeight()*3/4f,
				1f, Interpolation.sine
				));
		restart.addAction(Actions.moveTo(
				(getWidth()-restart.getWidth())/2, 
				getHeight()*2/4f,
				1f, Interpolation.sine
				));
		exit.addAction(Actions.moveTo(
				(getWidth()-exit.getWidth())/2, 
				getHeight()*1/4f,
				1f, Interpolation.sine
				));
		
	}
	
	public void disposePauseMenu(){
		
		resume.addAction(Actions.moveTo(
				-100, -100,
				1f, Interpolation.sine
				));
		restart.addAction(Actions.moveTo(
				-100, -100,
				1f, Interpolation.sine
				));
		exit.addAction(Actions.moveTo(
				-100, -100,
				1f, Interpolation.sine
				));
		isPaused = false;
	}
	
	public void showWinMenu(){
		isPaused = true;
	}
	
	public void disposeWinMenu(){
		isPaused = false;
	}
	
	public void showGameOverMenu(){
		//isPaused = true;
	}
	
	public void disposeGameOverMenu(){
		isPaused = false;
	}
	
	public boolean isPaused(){
		return isPaused;
	}
	
	
	public static boolean useUmbrellaIsChecked(){
		return useUmbrella.isChecked();
	}
	
	
}
