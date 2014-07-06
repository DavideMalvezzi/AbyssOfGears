package com.cappuccino.aog.scene.actors;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.cappuccino.aog.Assets;

public class AchievementContextMenu extends ContextMenu {
	
	
	public AchievementContextMenu(ChainedContetxMenuContainer parent) {
		super(parent);
		group.setSize(parent.getWidth(), parent.getHeight()*2);
		
		ImageButton achive = new ImageButton(Assets.hudSkin.getDrawable("AchievementButton"));
		achive.setPosition(100, 100);
		group.addActor(achive);
	}
	
}
