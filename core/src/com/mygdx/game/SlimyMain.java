package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.screens.MenuScreen;

public class SlimyMain extends Game implements ApplicationListener
{
	private static final String TAG = SlimyMain.class.getName();
	
	@Override
	public void create()
	{
		//Debug mode
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//load assets
		Assets.instance.init(new AssetManager());
		
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}

//	@Override
//	public void resize(int width, int height)
//	{
//		worldRenderer.resize(width, height);
//	}
//
//	@Override
//	public void render()
//	{
//		//if the game is paused, don't update the game world
//		if(!paused)
//			//Update the game world by the time that has passed since the last rendered frame
//			worldController.update(Gdx.graphics.getDeltaTime());
//		//Sets the clear screen color to cornflower blue
//		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
//		//clear the screen
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		
//		worldRenderer.render();
//	}
//
//	@Override
//	public void pause()
//	{
//		paused = true;
//	}
//
//	@Override
//	public void resume()
//	{
//		Assets.instance.init(new AssetManager());
//		paused = false;
//	}
//
//	@Override
//	public void dispose()
//	{
//		worldRenderer.dispose();
//		Assets.instance.dispose();
//	}
			
}
