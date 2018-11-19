package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;

public class SlimyCharacter extends AbstractGameObject
{
	public static final String TAG = SlimyCharacter.class.getName();
	
	private final float JUMP_POWER = .75f;
	private final float GRAVITY = 0.05f;
	
	public enum VIEW_DIRECTION {LEFT, RIGHT}
	
	public enum JUMP_STATE {GROUNDED, FALLING, JUMPING}
	
	public TextureRegion regBody;
	
	public VIEW_DIRECTION viewDirection;
	public JUMP_STATE jumpState;
	public float currentJump;
	public float timeRed;
	
	public SlimyCharacter()
	{
		init();
	}
	
	public void init() 
	{
		dimension.set(1,1);
		
		regBody = Assets.instance.slimy.slimy;
		
		//center image on game object
		origin.set(dimension.x/2, dimension.y/2);
				
		//set physics
		terminalVelocity.set(3.0f,4.0f);
		friction = 12;
		
		//view direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		
		//jump state
		jumpState = JUMP_STATE.FALLING;
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
					body.applyForceToCenter(0, currentJump, true);
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
		if(regBody !=null)
			viewDirection = VIEW_DIRECTION.LEFT;
		else
			viewDirection = VIEW_DIRECTION.RIGHT;
		
		timeRed = MathUtils.clamp(timeRed - deltaTime, 0, 999);
	}
	
	@Override
	public void updateMotionY(float deltaTime)
	{
		if(currentJump == 0)
			jumpState = JUMP_STATE.GROUNDED;
		if(jumpState!=JUMP_STATE.GROUNDED)
		{
			body.applyForceToCenter(0, -GRAVITY, true);
			currentJump = (float) MathUtils.clamp(currentJump-GRAVITY, -1, 1);
			
		}
		
		if(currentJump<0)
			jumpState = JUMP_STATE.FALLING;
		position.set(position.x + body.getLinearVelocity().x, position.y + body.getLinearVelocity().y);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		
		if(isRed())
			regBody = Assets.instance.slimy.redSlimy;
		else
			regBody = Assets.instance.slimy.slimy;
		
		reg = regBody;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
			dimension.x, dimension.y, scale.x, scale.y, rotation,
			reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
			viewDirection == VIEW_DIRECTION.LEFT, false);
	}

}
