package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	public AssetManager assetManager;

	public AssetSlimy slimy;
	public AssetImp imp;
	public AssetJelly jelly;
	public AssetDividingJelly dividing_jelly;
	public AssetRedJelly redJelly;
	public AssetCave cave;
	public BitmapFont defaultNormal;
	public BitmapFont menuNormal;
	public AssetButton button;
	
	public AssetSounds sounds;
	public AssetMusic music;
	
	private Assets() {}
	
	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//set assent manager error handler
		assetManager.setErrorListener(this);
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		//loading sound and music
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("music/caves_of_echoes.mp3",Music.class);
		
		//start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames());
		
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		
		TextureAtlas atlas = new TextureAtlas(Constants.TEXTURE_ATLAS_OBJECTS);
		
		//enable texture filtering for pixel smoothing
		for(Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		slimy = new AssetSlimy(atlas);
		imp = new AssetImp(atlas);
		jelly = new AssetJelly(atlas);
		redJelly = new AssetRedJelly(atlas);
		dividing_jelly = new AssetDividingJelly(atlas);
		cave = new AssetCave(atlas);
		defaultNormal = new BitmapFont(new FileHandle("../core/assets/arial-15.fnt"), true);
		defaultNormal.getData().setScale(1.0f);
		defaultNormal.getRegion().getTexture().setFilter(
				 TextureFilter.Linear, TextureFilter.Linear);
		menuNormal = new BitmapFont(new FileHandle("../core/assets/arial-15.fnt"), false);
		menuNormal.getData().setScale(1.0f);
		menuNormal.getRegion().getTexture().setFilter(
				 TextureFilter.Linear, TextureFilter.Linear);
		button = new AssetButton(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}
	
	public void error(String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}
	
	@Override
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}
	
	@Override
	public void dispose()
	{
		assetManager.dispose();
	}

	public class AssetSlimy
	{
		public final AtlasRegion slimy;
		public final AtlasRegion redslimy;
		public final AtlasRegion move01;
		public final AtlasRegion move02;
		public final AtlasRegion redmove01;
		public final AtlasRegion redmove02;
		public final AtlasRegion jump01;
		public final AtlasRegion jump02;
		
		public AssetSlimy(TextureAtlas atlas)
		{
			slimy = atlas.findRegion("slimy");
			redslimy = atlas.findRegion("redSlimy");
			move01 = atlas.findRegion("move1");
			move02 = atlas.findRegion("move2");
			redmove01 = atlas.findRegion("Redmove1");
			redmove02 = atlas.findRegion("Redmove2");
			jump01 = atlas.findRegion("jump3");
			jump02 = atlas.findRegion("jump2");
		}
	}
	
	public class AssetImp
	{
		public final AtlasRegion imp;
		
		public AssetImp(TextureAtlas atlas)
		{
			imp = atlas.findRegion("imp_stand");
		}
	}
	
	public class AssetJelly
	{
		public final AtlasRegion jelly;
		
		public AssetJelly(TextureAtlas atlas)
		{
			jelly = atlas.findRegion("jelly");
		}
	}
	
	public class AssetRedJelly
	{
		public final AtlasRegion redJelly;
		
		public AssetRedJelly(TextureAtlas atlas)
		{
			redJelly = atlas.findRegion("redJelly");
		}
	}
	
	public class AssetDividingJelly
	{
		public final AtlasRegion dividing_jelly;
		
		public AssetDividingJelly(TextureAtlas atlas)
		{
			dividing_jelly = atlas.findRegion("dividingjelly");
		}
	}
	
	public class AssetCave
	{
		public final AtlasRegion backdrop;
		public final AtlasRegion largeRocks;
		public final AtlasRegion smallRocks;
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetCave(TextureAtlas atlas)
		{
			edge = atlas.findRegion("CaveFloorEdge");
			middle = atlas.findRegion("CaveFloor");
			backdrop = atlas.findRegion("CaveBackdrop");
			largeRocks = atlas.findRegion("CaveRocks1");
			smallRocks = atlas.findRegion("CaveRocks2");
		}
	}
	
	public class AssetButton
	{
		public final AtlasRegion play;
		
		public AssetButton(TextureAtlas atlas)
		{
			play = atlas.findRegion("tempPlayButton");
		}
	}
	
	public class AssetSounds
	{
		public final Sound jump;
		
		public AssetSounds (AssetManager am) 
		 {
			 jump = am.get("sounds/jump.wav", Sound.class);
		 }
	}
	
	public class AssetMusic
	{
		public final Music song01;
		
		public AssetMusic (AssetManager am)
		{
			song01 = am.get("music/caves_of_echoes.mp3",Music.class);
		}
	}
}
