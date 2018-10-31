package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class SlimyMain implements ApplicationListener
{
	private static final String TAG = SlimyMain.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private boolean paused;
	
	@Override
	public void create()
	{
		//Debug mode
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Initialize the controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		
		//game world is active on start
		paused = false;
	}

	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}

	@Override
	public void render()
	{
		//if the game is paused, don't update the game world
		if(!paused)
			//Update the game world by the time that has passed since the last rendered frame
			worldController.update(Gdx.graphics.getDeltaTime());
		//Sets the clear screen color to cornflower blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		//clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		worldRenderer.render();
	}

	@Override
	public void pause()
	{
		paused = true;
	}

	@Override
	public void resume()
	{
		paused = false;
	}

	@Override
	public void dispose()
	{
		worldRenderer.dispose();
	}
			
}
