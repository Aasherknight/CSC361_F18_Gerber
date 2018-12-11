package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class HighScore
{
	public static final String TAG = HighScore.class.getName();
	
	//Singleton of game preferences
	public static final HighScore instance = new HighScore();
	
	//Set up the various settings players can alter
	public int scores[] = new int[10];
	
	private Preferences prefs;
	
	//Initialization of the singleton
	private HighScore()
	{
		prefs = Gdx.app.getPreferences(Constants.HIGHSCORES);
	}
	
	public void load()
	{
		for(int i = 0; i < scores.length; i++)
			scores[i] = prefs.getInteger("Score" + i);
	}
	
	public void save (int score)
	{
		load();
		for(int i = 0; i < scores.length; i++)
		{
			if(score >= scores[i])
			{
				prefs.putInteger("Score" + i, score);
				score = scores[i];
			}
		}
		prefs.flush();	
	}
}
