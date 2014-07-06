package com.cappuccino.aog.scene.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.cappuccino.aog.Assets;

public class ContextMenu extends Actor{

	protected ChainedContetxMenuContainer parent;
	
	protected ScrollPane scroll;
	protected Group group;
	
	public ContextMenu(ChainedContetxMenuContainer parent) {
		this.parent = parent;
		
		group = new Group();
		group.setTransform(true);
		
		ScrollPaneStyle scrollStyle = new ScrollPaneStyle();
		scrollStyle.vScroll = Assets.hudSkin.getDrawable("vScrollBar");
		scrollStyle.vScrollKnob = Assets.hudSkin.getDrawable("vScrollKnob");
		
		scroll = new ScrollPane(group, scrollStyle);
		scroll.setScrollingDisabled(true, false);
		scroll.setVariableSizeKnobs(false);
		scroll.setSize(parent.getWidth(), parent.getHeight());
		scroll.setOrigin(scroll.getWidth()/2, scroll.getHeight());
		scroll.setPosition(0, -100);
		scroll.setTransform(true);
		
		parent.getStage().addActor(scroll);
	}
	
	
	@Override
	public void setPosition(float x, float y) {
		scroll.setPosition(x, y);
	}
	
	@Override
	public float getX() {
		return scroll.getX();
	}
	
	@Override
	public float getY() {
		return scroll.getY();
	}
	
	@Override
	public void setRotation(float degrees) {
		scroll.setRotation(degrees);
	}
	
	@Override
	public boolean remove() {
		return scroll.remove();
	}
	
}
