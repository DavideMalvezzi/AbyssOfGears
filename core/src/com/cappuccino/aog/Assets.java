package com.cappuccino.aog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;


public class Assets {

	private static OrderedMap<String, AtlasRegion> textures;
	
	
	public static Skin hudSkin;
	public static BitmapFont font64,font100;
	
	public static Texture layer0Background;
	public static Texture layer1Background;
	
	public static Animation player_WALK;
	
	public static ParticleEmitter fallingEffect, smokeEffect, gasEffect;
	
	public static void load(){
		TextureAtlas atlas = new TextureAtlas("gfx/Textures.pack");
		
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/Ornatique.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.minFilter = TextureFilter.Linear;
		parameter.magFilter = TextureFilter.Linear;
		parameter.genMipMaps = true;
		parameter.size = 128;
		font64 = fontGenerator.generateFont(parameter);
		font64.setScale(0.5f);
		parameter.size = 150;
		parameter.characters  = "Play";
		font100 = fontGenerator.generateFont(parameter);
		font100.setScale(0.5f);
		fontGenerator.dispose();
		
		hudSkin = new Skin(atlas);
		textures = new OrderedMap<String, AtlasRegion>();

		Array<AtlasRegion> regions = atlas.getRegions();
		for(AtlasRegion region : regions){
			textures.put(region.name, region);
		}
		
		layer0Background = new Texture("gfx/layer0Bg.png");
		layer0Background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		layer0Background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		layer1Background = new Texture("gfx/layer1Bg.png");
		layer1Background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		layer1Background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		ParticleEffect fallingParticle = new ParticleEffect();
		fallingParticle.load(Gdx.files.internal("data/particle/FallingEffect"), Gdx.files.internal("gfx/"));
		fallingEffect = fallingParticle.getEmitters().first();
		
		ParticleEffect smokeParticle = new ParticleEffect();
		smokeParticle.load(Gdx.files.internal("data/particle/SmokeEffect"), Gdx.files.internal("gfx/"));
		smokeEffect = smokeParticle.getEmitters().first();
		
		ParticleEffect gasParticle = new ParticleEffect();
		gasParticle.load(Gdx.files.internal("data/particle/GasEffect"), Gdx.files.internal("gfx/"));
		gasEffect = gasParticle.getEmitters().first();
		
		AtlasRegion region = getTexture("Player_Walk");
		Array<TextureRegion> keyFrames = new Array<TextureRegion>(region.split(region.getRegionWidth()/4, region.getRegionHeight())[0]);
		player_WALK = new Animation(0.1f, keyFrames, PlayMode.LOOP);
	}
	
	public static void dispose(){
		layer0Background.dispose();
		layer1Background.dispose();
		
		font64.dispose();
		font100.dispose();
		
		hudSkin.dispose();
		textures.clear();
	}
	
	public static AtlasRegion getTexture(String name){
		return textures.get(name);
	}
	
	
}
