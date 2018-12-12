package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;

public class Imp extends AbstractGameObject
{
	public TextureRegion reg;
	
	public Imp()
	{
		init();
	}
	
	private void init()
	{
		dimension.set(1f, 1f);
		
		reg = Assets.instance.imp.imp;
				
		//Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

}
