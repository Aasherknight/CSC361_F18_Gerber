package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;

public class DividingJelly extends AbstractGameObject
{
	public TextureRegion reg;

	public DividingJelly()
	{
		init();
	}
	
	private void init()
	{
		dimension.set(0.5f, 0.5f);
		
		reg = Assets.instance.dividing_jelly.dividing_jelly;
				
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
