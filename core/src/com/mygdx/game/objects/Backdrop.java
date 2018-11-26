package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;

public class Backdrop extends AbstractGameObject
{
	private TextureRegion backdrop;
	private float length;
	private float height;

	public Backdrop(float length, float height)
	{
		this.length = length;
		this.height = height;
		
		init();
	}
	
	private void init()
	{
		dimension.set(length,height);
		
		backdrop = Assets.instance.cave.backdrop;
		
		origin.x = -dimension.x/2;
		origin.y = -dimension.y/2;
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = backdrop;
		batch.draw(reg.getTexture(), origin.x, origin.y, 
				origin.x, origin.y, dimension.x, dimension.y, scale.x, 
				scale.y, rotation, reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

}
