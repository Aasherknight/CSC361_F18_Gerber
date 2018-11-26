package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.Ground;
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
	
	public World b2world;
	
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
		initPhysics();
		b2world.setContactListener(level.slimy);
	}
	
	public void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.slimy);
	}
	
	public void initPhysics()
	{
		if(b2world != null)
			b2world.dispose();
		b2world = new World(new Vector2(0, -12f), true);
		
		BodyDef bodyDef = null;
		Vector2 origin = new Vector2();
		//Rocks
		for(Ground ground : level.ground)
		{
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(ground.position);
			bodyDef.fixedRotation = true;
			Body body = b2world.createBody(bodyDef);
			ground.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = ground.bounds.width / 2.0f;
			origin.y = ground.bounds.height / 2.0f;
			polygonShape.setAsBox(ground.bounds.width / 2.0f, ground.bounds.height / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 1;
			body.createFixture(fixtureDef);
			body.setAwake(true);
			polygonShape.dispose();
		}
		//slimy
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(level.slimy.position);
		bodyDef.fixedRotation = true;
		
		level.slimy.body = b2world.createBody(bodyDef);	

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(level.slimy.dimension.x/3.0f,level.slimy.dimension.x/3.0f, level.slimy.origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 1;
		fixtureDef.restitution = 0.1f;
		fixtureDef.friction = 1f;
		level.slimy.body.createFixture(fixtureDef);
		polygonShape.dispose();
		level.slimy.body.setAwake(true);
		
	}
	
	/**
	 * Updates based on how much time has passed since the last update.
	 * @param deltaTime How much time has passed
	 */
	public void update(float deltaTime)
	{
		b2world.step(deltaTime, 8, 3);
		handleDebugInput(deltaTime);
		//updateTestObjects(deltaTime);
		handleInputGame(deltaTime);
		level.update(deltaTime);
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
				level.slimy.moveLeft(deltaTime);
			if (Gdx.input.isKeyPressed(Keys.D))
				level.slimy.moveRight(deltaTime);
			
			//slimy jump
			if (Gdx.input.isKeyPressed(Keys.W))
				level.slimy.jump(deltaTime);
		}
	}
	

	private void handleDebugInput(float deltaTime)
	{
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
}
