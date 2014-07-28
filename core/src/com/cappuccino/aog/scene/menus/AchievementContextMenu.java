package com.cappuccino.aog.scene.menus;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.cappuccino.aog.Assets;

public class AchievementContextMenu extends ContextMenu {
	
	public AchievementContextMenu(ChainedContetxMenuContainer parent) {
		super(parent);
		
		ImageButton achive = new ImageButton(Assets.hudSkin.getDrawable("AchievementButton"));
		achive.setPosition(100, 100);
		group.addActor(achive);
		
	}
	
	@Override
	public void act(float delta) {

		super.act(delta);
	}
	
	
	
}
