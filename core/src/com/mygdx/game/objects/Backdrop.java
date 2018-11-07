package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;

public class Backdrop extends AbstractGameObject
{
	private TextureRegion backdrop;
	private float length;

	public Backdrop(float length)
	{
		this.length = length;
		init();
	}
	
	private void init()
	{
		dimension.set(length*10,3);
		
		backdrop = Assets.instance.cave.backdrop;
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = backdrop;
		batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, 
				origin.x, origin.y, dimension.x, dimension.y, scale.x, 
				scale.y, rotation, reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

}
