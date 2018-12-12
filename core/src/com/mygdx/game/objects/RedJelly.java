package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;

public class RedJelly extends AbstractGameObject
{
	public boolean collected;
	
	private TextureRegion reg;

	public RedJelly()
	{
		init();
	}
	
	private void init()
	{
		dimension.set(0.5f, 0.5f);
		
		reg = Assets.instance.redJelly.redJelly;
				
		//Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		if(collected) return;
		
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

}
