package com.cappuccino.aog.scene.menus;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.cappuccino.aog.Assets;

public class ContextMenu extends ScrollPane{

	private static final ScrollPaneStyle scrollStyle = new ScrollPaneStyle(null, null, null, Assets.hudSkin.getDrawable("vScrollBar"), Assets.hudSkin.getDrawable("vScrollKnob"));
	
	protected ChainedContetxMenuContainer parent;
	
	protected Group group;
	
	public ContextMenu(ChainedContetxMenuContainer parent) {
		super(new Group(), scrollStyle);
		this.parent = parent;
		
		group = (Group) getWidget();
		group.setTransform(true);
		group.setSize(parent.getWidth(), parent.getHeight()*2);
		
		setScrollingDisabled(true, false);
		setVariableSizeKnobs(false);
		setSize(parent.getWidth(), parent.getHeight());
		setOrigin(getWidth()/2, getHeight());
		setTransform(true);
		
		
		parent.getStage().addActor(this);
	}
	
	public ContextMenu(Stage parent ) {
		super(new Group(), scrollStyle);
		
		group = (Group) getWidget();
		group.setTransform(true);
		group.setSize(parent.getWidth(), parent.getHeight()*2);
		
		setScrollingDisabled(true, false);
		setVariableSizeKnobs(false);
		setSize(parent.getWidth(), parent.getHeight());
		setOrigin(getWidth()/2, getHeight());
		setTransform(true);
		
		
		parent.addActor(this);
	}
	
	
}
