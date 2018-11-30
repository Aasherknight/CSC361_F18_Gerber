package com.mygdx.game.util;

/**
 * This Class is meant to house any universal constants that the game might need.
 * @author Aaron Gerber
 */
public class Constants
{
	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	
	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;
	
	//GUI width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	
	//GUI height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	public static final String TEXTURE_ATLAS_OBJECTS = "slimy.atlas";//"slimy.atlas";
	
	//location for level 1's image
	public static final String LEVEL_01 = "level01.png";
	
	//slimes only have one life
	public static final int LIVES_START = 1;
}
