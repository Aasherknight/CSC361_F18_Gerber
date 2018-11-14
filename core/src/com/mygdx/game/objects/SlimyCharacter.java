package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Assets;

public class SlimyCharacter extends AbstractGameObject
{
	public static final String TAG = SlimyCharacter.class.getName();
	
	private final int JUMP_POWER = 9;
	private final double GRAVITY = 0.25;
	
	public enum VIEW_DIRECTION {LEFT, RIGHT}
	
	public enum JUMP_STATE {GROUNDED, FALLING, JUMPING}
	
	private TextureRegion body;
	
	public VIEW_DIRECTION viewDirection;
	public JUMP_STATE jumpState;
	public double currentJump;
	public float timeRed;
	
	public SlimyCharacter()
	{
		init();
	}
	
	public void init() 
	{
		dimension.set(1,1);
		
		body = Assets.instance.slimy.slimy;
		
		//center image on game object
		origin.set(dimension.x/2, dimension.y/2);
		
		//bounding box for collision
		bounds.set(0,0,dimension.x,dimension.y);
		
		//set physics
		terminalVelocity.set(3.0f,4.0f);
		friction.set(12.0f,0.0f);
		acceleration.set(0.0f, -25.0f);
		
		//view direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		
		//jump state
		jumpState = JUMP_STATE.GROUNDED;
		currentJump = 0;
		
		timeRed = 0;
	}
	
	public void jump(boolean jumpKeyPressed) 
	{
		switch(jumpState)
		{
			case GROUNDED:
				if(jumpKeyPressed)
				{
					currentJump = JUMP_POWER;
					jumpState = JUMP_STATE.JUMPING;
				}
				break;
			default:
				break;
		}
	}
	
	public void setRed() {};
	
	public boolean isRed() 
	{
		return timeRed==0 ? false : true;
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		if(velocity.x < 0)
			viewDirection = VIEW_DIRECTION.LEFT;
		else
			viewDirection = VIEW_DIRECTION.RIGHT;
		
		timeRed = MathUtils.clamp(timeRed - deltaTime, 0, 999);
	}
	
	@Override
	public void updateMotionY(float deltaTime)
	{
		
		if(jumpState!=JUMP_STATE.GROUNDED)
		{
			currentJump = MathUtils.clamp(currentJump-GRAVITY, -9, 9);
			
			velocity.y = (float) currentJump;
		}
		
		if(currentJump<0)
			jumpState = JUMP_STATE.FALLING;
		
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		
		if(isRed())
			body = Assets.instance.slimy.redSlimy;
		else
			body = Assets.instance.slimy.slimy;
		
		reg = body;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
			dimension.x, dimension.y, scale.x, scale.y, rotation,
			reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
			viewDirection == VIEW_DIRECTION.LEFT, false);
	}

}
