package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;

	public AssetSlimy slimy;
	public AssetImp imp;
	public AssetJelly jelly;
	public AssetDividingJelly dividing_jelly;
	
	private Assets() {}
	
	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//set assent manager error handler
		assetManager.setErrorListener(this);
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
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
		dividing_jelly = new AssetDividingJelly(atlas);
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
		
		public AssetSlimy(TextureAtlas atlas)
		{
			slimy = atlas.findRegion("slimy");
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
	
	public class AssetDividingJelly
	{
		public final AtlasRegion dividing_jelly;
		
		public AssetDividingJelly(TextureAtlas atlas)
		{
			dividing_jelly = atlas.findRegion("dividingjelly");
		}
	}
}
