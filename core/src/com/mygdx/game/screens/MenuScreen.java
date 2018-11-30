package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Assets;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.UIHelper;

public class MenuScreen extends AbstractGameScreen
{

	private static final String TAG = MenuScreen.class.getName();
	
	private Stage stage;
	
	//menu
	private Image imgBackground;
	private Image imgJelly;
	private Image imgSlimy;
	private Button btnMenuPlay;
	
	//debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	public MenuScreen (Game game)
	{
		super(game);
	}
	
	//rebuilds all the layers for the final scene of the menu screen
	private void rebuildStage()
	{	
		//build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerControls = buildControlsLayer();
		
		//assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerControls);
	}
	
	/**
	 * Builds the Table for the background layer of the menu
	 * @return Background Layer Table
	 */
	private Table buildBackgroundLayer()
	{
		Table layer = new Table();
		// + Background
		imgBackground = new Image(Assets.instance.cave.backdrop);
		layer.add(imgBackground);
		return layer;
	}
	
	/**
	 * Builds the Table for the objects layer of the menu
	 * @return Objects Layer Table
	 */
	private Table buildObjectsLayer()
	{
		Table layer = new Table();
		// + Coins
		imgJelly = new Image(Assets.instance.jelly.jelly);
		layer.addActor(imgJelly);
		imgJelly.setPosition(135, 80);
		// + Bunny
		imgSlimy = new Image(Assets.instance.slimy.slimy);
		layer.addActor(imgSlimy);
		imgSlimy.setPosition(355, 40);
		return layer;
	}

	/**
	 * Builds the Table for the controls layer of the menu
	 * @return Controls Layer Table
	 */
	private Table buildControlsLayer()
	{
		Table layer = new Table();
		UIHelper.addTexture("tempPlayButton", new Texture("tempPlayButton.png"));
		
		/**
		 * Aaron Gerber - pg 247-248 changes
		 */
		//Set the layer to the bottom right
		layer.right().bottom();
		// add play button
		btnMenuPlay = UIHelper.constructButton("tempPlayButton", "play");
		layer.add(btnMenuPlay);
		//give the play button something to do
		btnMenuPlay.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{onPlayClicked();}
			});
		
		//show debug layer if we are on debug
		if(debugEnabled) 
			layer.debug();
		/**
		 * End of changes from pg 247-248
		 */
		
		return layer;
	}

	/**
	 * Draws the menu screen and debug lines if debug is activated
	 * @param deltaTime
	 */
	@Override
	public void render(float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(debugEnabled)
		{
			debugRebuildStage -= deltaTime;
			if(debugRebuildStage <= 0)
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		stage.act(deltaTime);
		stage.draw();
		stage.setDebugAll(debugEnabled); //needs to be variable so debug variable can be used
	}
	
	/**
	 * Updates the size of the game window
	 * @param width
	 * @param heigth
	 */
	@Override public void resize(int width, int heigth)
	{
		stage.getViewport().update(width, heigth, true);
	}
	
	/**
	 * Initializes the stage size and rebuilds the stage
	 */
	@Override public void show()
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}
	
	/**
	 * Disposes of the stage and skin
	 */
	@Override public void hide()
	{
		stage.dispose();
	}
	@Override public void pause() {}
	
	/**
	 * Aaron Gerber
	 * play the game!
	 */
	private void onPlayClicked()
	{
		game.setScreen(new GameScreen(game));
	}
}
