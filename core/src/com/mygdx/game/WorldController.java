package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Ground;
import com.mygdx.game.objects.SlimyCharacter;
import com.mygdx.game.objects.SlimyCharacter.JUMP_STATE;
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
	
	//rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
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
		cameraHelper.setTarget(level.slimy);
	}
	
	/**
	 * Updates based on how much time has passed since the last update.
	 * @param deltaTime How much time has passed
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		//updateTestObjects(deltaTime);
		handleInputGame(deltaTime);
		level.update(deltaTime);
		checkCollision();
		cameraHelper.update(deltaTime);
	}
	
	/**
	 * Called with Update.
	 * Handles the input related the bunny head's movement
	 * @param deltaTime
	 */
	private void handleInputGame(float deltaTime)
	{
		if (cameraHelper.hasTarget(level.slimy))
		{
			//Player Movement
			if (Gdx.input.isKeyPressed(Keys.A))
				level.slimy.velocity.x -= level.slimy.terminalVelocity.x;
			else if (Gdx.input.isKeyPressed(Keys.D))
				level.slimy.velocity.x += level.slimy.terminalVelocity.x;
			
			//slimy jump
			if (Gdx.input.isKeyPressed(Keys.W))
				level.slimy.jump(true);
			else
				level.slimy.jump(false);
		}
	}
	

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

	private void checkCollision()
	{
		r1.set(level.slimy.position.x, level.slimy.position.y,level.slimy.bounds.width, level.slimy.bounds.height);
		
		//testing for collisions with the ground
		for(Ground ground: level.ground)
		{
			r2.set(ground.position.x,ground.position.y, ground.bounds.width,ground.bounds.height);
			if(r1.overlaps(r2))
				onCollisionSlimyWithGround(ground);
		}
	}
	
	private void onCollisionSlimyWithGround(Ground ground)
	{
		SlimyCharacter slimy = level.slimy;
		
		float heightDifference = Math.abs(slimy.position.y - (ground.position.y + ground.bounds.height));
		
		if(heightDifference> 0.15)
		{
			if(slimy.position.x > (ground.position.x + ground.bounds.width/2.0f))
				slimy.position.x = ground.position.x + ground.bounds.width;
			else
				slimy.position.x = ground.position.x - ground.bounds.width;
		}
		
		switch(slimy.jumpState)
		{
		case FALLING:
			slimy.position.y = ground.position.y + slimy.bounds.height + slimy.origin.y;
			slimy.jumpState = JUMP_STATE.GROUNDED;
			break;
		default:
			break;
		}
	}
}
