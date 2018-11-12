package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.objects.AbstractGameObject;
import com.mygdx.game.objects.Backdrop;
import com.mygdx.game.objects.Ground;


public class Level 
{
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE
	{
		EMPTY(0,0,0), 					//Black
		JELLY(0,255,0), 				//Green
		PLAYER_SPAWNPOINT(255,255,255),	//White
		IMP_SPAWNS(255,0,255),			//Purple
		GOAL(255,255,0),				//Yellow
		REDJELLY(255,0,0),				//Red
		GROUND(70,70,70);				//Grey
		
		private int color;
		
		private BLOCK_TYPE(int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		public boolean sameColor(int color)	
		{
			return this.color == color;
		}
		
		public int getColor()
		{
			return color;
		}
	}
	
	public Array<Ground> ground;
	
	public Backdrop backdrop;
	
	public Level (String filename)
	{
		init(filename);
	}
	
	public void update(float deltaTime)
	{
		
	}
	
	private void init(String filename)
	{
		ground = new Array<Ground>();
		
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		
		//scan pixels form top-left to bottom-right
		int lastPixel = -1;
		
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
		{
			for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				//preparing a super so that the children can be used as if they were the super
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				
				//height grows from bottom to top
				float baseHeight = pixmap.getHeight()-pixelY;
				
				//get color of current pixels as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				
				//find matching color value to id the block type
				//create the listed object if there is a match
				
				//empty space
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					
				}
				//rock
				else if(BLOCK_TYPE.GROUND.sameColor(currentPixel))
				{
					//making the edge of a rock
					if(lastPixel != currentPixel)
					{
						obj = new Ground();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						ground.add((Ground)obj);
					}
					else
					{
						ground.get(ground.size-1).increaseLength(1);
					}
				}
				else if(BLOCK_TYPE.JELLY.sameColor(currentPixel))
				{
				}
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
				}
				else if(BLOCK_TYPE.IMP_SPAWNS.sameColor(currentPixel))
				{
				}
				else if(BLOCK_TYPE.GOAL.sameColor(currentPixel))
				{
				}
				else if(BLOCK_TYPE.REDJELLY.sameColor(currentPixel))
				{
				}
				else
				{
					int r = 0xff & (currentPixel >>> 24); 	//Red color channel
					int g = 0xff & (currentPixel >>> 16);	//Green color channel
					int b = 0xff & (currentPixel >>> 8);	//Blue color channel
					int a = 0xff & currentPixel;			//alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		
		//decoration
		backdrop = new Backdrop(pixmap.getWidth(),pixmap.getHeight());
		
		//free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '" + filename + "' loaded");
	}
	
	public void render(SpriteBatch batch)
	{		
		//Draw the cave backdrop
		backdrop.render(batch);

		//Draw rocks
		for(Ground floor : ground)
			floor.render(batch);	

	}
	
}
