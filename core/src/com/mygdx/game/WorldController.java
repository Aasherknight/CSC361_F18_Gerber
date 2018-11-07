package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.util.CameraHelper;
import com.mygdx.game.util.Constants;

/**
 * Controls how the world updates
 * @author AaronGerber
 *
 */

public class WorldController extends InputAdapter
{
	private static final String TAG = WorldController.class.getName();
	
//	public Sprite[] testSprites;
//	public int selectedSprite;
	
	public CameraHelper cameraHelper;
	
	public Level level;
	public int lives;
	public int score;
	
	public WorldController()
	{
		init();
	}
	
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		//initTestObjects();
		lives = Constants.LIVES_START;
		initLevel();
	}
	
	public void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
	}
	
	/**
	 * Updates based on how much time has passed since the last update.
	 * @param deltaTime How much time has passed
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		//updateTestObjects(deltaTime);
		cameraHelper.update(deltaTime);
	}
	
//	private void initTestObjects()
//	{
//		//array of 5 sprites
//		testSprites = new Sprite[5];
//		//list of texture regions
//		Array<TextureRegion> regions = new Array<TextureRegion>();
//		regions.add(Assets.instance.slimy.slimy);
//		regions.add(Assets.instance.imp.imp);
//		regions.add(Assets.instance.jelly.jelly);
//		regions.add(Assets.instance.dividing_jelly.dividing_jelly);
//		
//		//create new sprites using the random texture region
//		for(int i = 0; i < testSprites.length; i++)
//		{
//			Sprite spr = new Sprite(regions.random());
//			spr.setSize(1, 1);
//			//set origin for sprites center
//			spr.setOrigin(spr.getWidth()/2.0f, spr.getHeight()/2.0f);
//			//calculate random position for sprite
//			float ranX = MathUtils.random(-2.0f, 2.0f);
//			float ranY = MathUtils.random(-2.0f, 2.0f);
//			spr.setPosition(ranX, ranY);
//			
//			//put the sprite we made into array
//			testSprites[i] = spr;
//		}
//		//select the first sprite
//		selectedSprite = 0;
//	}
//	
//	private void updateTestObjects(float deltaTime)
//	{
//		float rotation = testSprites[selectedSprite].getRotation();
//		
//		rotation += 90*deltaTime;
//		rotation %= 360;
//		
//		testSprites[selectedSprite].setRotation(rotation);
//	}

	private void handleDebugInput(float deltaTime)
	{
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
//		float sprMoveSpeed = 5*deltaTime;
//		
//		if (Gdx.input.isKeyPressed(Keys.A))
//			moveSelectedSprite(-sprMoveSpeed, 0);
//		if (Gdx.input.isKeyPressed(Keys.D))
//			moveSelectedSprite(sprMoveSpeed, 0);
//		if (Gdx.input.isKeyPressed(Keys.W))
//			moveSelectedSprite(0, sprMoveSpeed);
//		if (Gdx.input.isKeyPressed(Keys.S))
//			moveSelectedSprite(0, -sprMoveSpeed);
		
		// Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP))
			moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
			cameraHelper.setPosition(0,0);
		
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
	}
	
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,y);
	}
	
//	private void moveSelectedSprite(float x, float y)
//	{
//		testSprites[selectedSprite].translate(x, y);
//	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		//reset game world
		if(keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		
//		//select next sprite
//		else if(keycode == Keys.SPACE)
//		{
//			selectedSprite = (selectedSprite+1)%testSprites.length;
//			if(cameraHelper.hasTarget())
//				cameraHelper.setTarget(testSprites[selectedSprite]);
//			Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
//		}
		
//		else if (keycode == Keys.ENTER)
//		{
//			cameraHelper.setTarget((cameraHelper.hasTarget() ? null : testSprites[selectedSprite]));
//			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
//		}
		return false;
	}
}
