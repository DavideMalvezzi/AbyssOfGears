package com.cappuccino.aog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;


public class Assets {

	private static OrderedMap<String, AtlasRegion> textures;
	
	public static Texture layer0Background;
	public static Texture layer1Background;
	public static Texture layer2Background;
	
	public static ParticleEmitter fallingEffect, smokeEffect, gasEffect;
	
	public static Skin hudSkin;
	public static BitmapFont font64,font100,font200;
	
	
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
		parameter.size = 190;
		font100 = fontGenerator.generateFont(parameter);
		font100.setScale(0.5f);
		parameter.size = 230;
		parameter.characters  = "AbysofGear";
		font200 = fontGenerator.generateFont(parameter);
		font200.setScale(0.5f);
		
		fontGenerator.dispose();
		
		hudSkin = new Skin(atlas);
		textures = new OrderedMap<String, AtlasRegion>();

		Array<AtlasRegion> regions = atlas.getRegions();
		for(AtlasRegion region : regions){
			textures.put(region.name, region);
		}
		
		layer0Background = new Texture("gfx/layer0Bg.png");
		layer0Background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		layer1Background = new Texture("gfx/layer1Bg.png");
		layer2Background = new Texture("gfx/layer2Bg.png");
		
		ParticleEffect fallingParticle = new ParticleEffect();
		fallingParticle.load(Gdx.files.internal("data/FallingEffect"), Gdx.files.internal("gfx/"));
		fallingEffect = fallingParticle.getEmitters().first();
		
		ParticleEffect smokeParticle = new ParticleEffect();
		smokeParticle.load(Gdx.files.internal("data/SmokeEffect"), Gdx.files.internal("gfx/"));
		smokeEffect = smokeParticle.getEmitters().first();
		
		ParticleEffect gasParticle = new ParticleEffect();
		gasParticle.load(Gdx.files.internal("data/GasEffect"), Gdx.files.internal("gfx/"));
		gasEffect = gasParticle.getEmitters().first();
	}
	
	public static void dispose(){
		layer0Background.dispose();
		layer1Background.dispose();
		layer2Background.dispose();
		
		font64.dispose();
		font100.dispose();
		font200.dispose();
		
		hudSkin.dispose();
		textures.clear();
	}
	
	public static AtlasRegion getTexture(String name){
		return textures.get(name);
	}
	
	
}
