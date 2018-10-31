package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Controls how the world updates
 * @author AaronGerber
 *
 */

public class WorldController
{
	private static final String TAG = WorldController.class.getName();
	
	public Sprite[] testSprites;
	public int selectedSprite;
	
	public WorldController()
	{
		init();
	}
	
	private void init()
	{
		initTestObjects();
	}
	
	/**
	 * Updates based on how much time has passed since the last update.
	 * @param deltaTime How much time has passed
	 */
	public void update(float deltaTime)
	{
		//TODO
	}
	
	private void initTestObjects()
	{
		//array of 5 sprites
		testSprites = new Sprite[5];
		
		//select the first sprite
		selectedSprite = 0;
	}
	
	private void updateTestObjects(float deltaTime)
	{
		float rotation = testSprites[selectedSprite].getRotation();
		
		rotation += 90*deltaTime;
		rotation %= 360;
		
		testSprites[selectedSprite].setRotation(rotation);
	}
}
