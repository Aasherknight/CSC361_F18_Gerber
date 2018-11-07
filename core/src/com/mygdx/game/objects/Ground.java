package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;

public class Ground extends AbstractGameObject
{

	private TextureRegion edge;
	private TextureRegion middle;
	
	private int length;
	
	public Ground()
	{
		init();
	}
	
	private void init()
	{
		dimension.set(1,1.5f);
		
		edge = Assets.instance.cave.edge;
		middle = Assets.instance.cave.middle;
		
		//set the inital length to 1
		setLength(1);
	}
	
	public void setLength(int length)
	{
		this.length = length;
	}
	
	public void increaseLength(int increment)
	{
		setLength(length+increment);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		
		float relX = 0;
		float relY = 0;
		
		//Draw left edge
		reg = edge;
		relX -= dimension.x / 4;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, 
				origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, 
				scale.y, rotation, reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
		//Draw middle
		relX = 0;
		reg = middle;
		for (int i = 0; i < length; i++)
		{
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), 
					reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
		
		//Draw right edge
		reg = edge;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8,
				origin.y, dimension.x /4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), 
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), true, false);
	}

}
