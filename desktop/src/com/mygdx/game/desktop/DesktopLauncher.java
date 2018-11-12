package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.mygdx.game.SlimyMain;

public class DesktopLauncher 
{
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = true;
	
	public static void main (String[] arg) 
	{
		if(rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "../core/assets/art","../core/assets","slimy.atlas");
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Slimy";
		config.width = 640;
		config.height = 400;
		
		new LwjglApplication(new SlimyMain(), config);
		
	}
}
