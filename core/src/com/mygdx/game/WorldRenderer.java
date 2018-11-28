package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.util.Constants;

/**
 * Displays the game world to the player
 * @author Aaron Gerber
 *
 */

public class WorldRenderer implements Disposable
{
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	private Box2DDebugRenderer b2debugRenderer;
	
	public WorldRenderer(WorldController worldController)
	{
		this.worldController = worldController;
		init();
	}
	
	private void init()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	public void render()
	{
		//renderTestObjects();
		renderWorld(batch);
		renderGUI(batch);
	}
	
	public void resize(int width, int height)
	{
		camera.viewportWidth = Constants.VIEWPORT_HEIGHT/height * width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_WIDTH / (float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth/2, cameraGUI.viewportHeight/2, 0);
		cameraGUI.update();				
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}
	
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		if(DEBUG_DRAW_BOX2D_WORLD)
			b2debugRenderer.render(worldController.b2world, camera.combined);
	}
	
	private void renderGUI(SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		
		renderGUIJelly(batch);
		
		batch.end();
	}
	
	private void renderGUIJelly(SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.jelly.jelly, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.defaultNormal.draw(batch, "" + worldController.score, x+75, y+37);
	}
	
//	private void renderTestObjects()
//	{
//		worldController.cameraHelper.applyTo(camera);
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		for(Sprite sprite: worldController.testSprites)
//			sprite.draw(batch);
//		batch.end();
//	}
}
