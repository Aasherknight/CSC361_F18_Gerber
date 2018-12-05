package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.util.AudioManager;

public class SlimyCharacter extends AbstractGameObject implements ContactListener
{
	public static final String TAG = SlimyCharacter.class.getName();
	
	private final float JUMP_POWER = 10f;
	private final float MOVE_SPEED = 1.5F;
	private final float MAX_MOVE_SPEED = 6f;
	
	public enum VIEW_DIRECTION {LEFT, RIGHT}
	
	public enum JUMP_STATE {GROUNDED, FALLING, JUMPING}
	
	public Animation<TextureRegion> moving;
	public Animation<TextureRegion> redmoving;
	
	public TextureRegion regBody;
	Array<TextureRegion> assets_moving;
	Array<TextureRegion> assets_redmoving;
	Array<TextureRegion> assets_jumping;
	
	private VIEW_DIRECTION viewDirection;
	private JUMP_STATE jumpState;
	private float timeRed;

	private float stateTime;
	
	public SlimyCharacter()
	{
		init();
	}
	
	public void init() 
	{
		dimension.set(1,1);
		
		regBody = Assets.instance.slimy.slimy;
		
		assets_moving = new Array<TextureRegion>();
		assets_moving.add(Assets.instance.slimy.move01);
		assets_moving.add(Assets.instance.slimy.move02);
		
		assets_redmoving = new Array<TextureRegion>();
		assets_redmoving.add(Assets.instance.slimy.redmove01);
		assets_redmoving.add(Assets.instance.slimy.redmove02);
		
		assets_jumping = new Array<TextureRegion>();
		assets_moving.add(Assets.instance.slimy.jump01);
		assets_moving.add(Assets.instance.slimy.jump02);
		
		moving = new Animation<TextureRegion>(0.25f, assets_moving);
		moving.setPlayMode(PlayMode.LOOP);
		redmoving = new Animation<TextureRegion>(0.25f, assets_redmoving);
		redmoving.setPlayMode(PlayMode.LOOP);
		
		stateTime = 0;
		
		//center image on game object
		origin.set(dimension.x/2, dimension.y/2);
		
		//view direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		
		//jump state
		jumpState = JUMP_STATE.FALLING;
		
		timeRed = 0;
	}
	
	public void setRed() {};
	
	public boolean isRed() 
	{
		return timeRed==0 ? false : true;
	}
	
	public void jump(float deltaTime) 
	{
		body.setAwake(true);
		switch(jumpState)
		{
			case GROUNDED:
				body.setLinearVelocity(body.getLinearVelocity().x,body.getLinearVelocity().y + JUMP_POWER);
				AudioManager.instance.play(Assets.instance.sounds.jump);
				jumpState = JUMP_STATE.JUMPING;
				break;
			default:
				break;
		}
	}
	
	public void hitStatic() 
	{
		body.setLinearVelocity(0,0);
	}
	
	public void moveLeft(float deltaTime)
	{
		if(viewDirection!=VIEW_DIRECTION.LEFT)
		{
			viewDirection = VIEW_DIRECTION.LEFT;
			body.setLinearVelocity(0 - MOVE_SPEED, body.getLinearVelocity().y);
		}
		else
			body.setLinearVelocity(MathUtils.clamp(body.getLinearVelocity().x - MOVE_SPEED, -MAX_MOVE_SPEED, MAX_MOVE_SPEED), body.getLinearVelocity().y);
	}
	
	public void moveRight(float deltaTime)
	{
		if(viewDirection!=VIEW_DIRECTION.RIGHT)
		{
			viewDirection = VIEW_DIRECTION.RIGHT;
			body.setLinearVelocity(0 + MOVE_SPEED, body.getLinearVelocity().y);
		}
		else
			body.setLinearVelocity(MathUtils.clamp(body.getLinearVelocity().x + MOVE_SPEED, -MAX_MOVE_SPEED, MAX_MOVE_SPEED), body.getLinearVelocity().y);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		if(isRed())
		{
			switch(jumpState)
			{
			case JUMPING:
				regBody = Assets.instance.slimy.jump01;
				break;
			case FALLING:
				regBody = Assets.instance.slimy.jump02;
				break;
			case GROUNDED:
				if(body.getLinearVelocity().x!=0)
					regBody = redmoving.getKeyFrame(stateTime);
				else
					regBody = Assets.instance.slimy.redslimy;
				break;
			}
		}
		else
		{
			switch(jumpState)
			{
			case JUMPING:
				regBody = Assets.instance.slimy.jump01;
				break;
			case FALLING:
				regBody = Assets.instance.slimy.jump02;
				break;
			case GROUNDED:
				if(body.getLinearVelocity().x!=0)
					regBody = moving.getKeyFrame(stateTime);
				else
					regBody = Assets.instance.slimy.slimy;
				break;
			}
		}
		
		timeRed = MathUtils.clamp(timeRed - deltaTime, 0, 999);
		stateTime += deltaTime;
	}
	
	@Override
	public void updateMotionX(float deltaTime)
	{
		position.x = body.getPosition().x;
	}
	
	@Override
	public void updateMotionY(float deltaTime)
	{
		position.y = body.getPosition().y;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		
		reg = regBody;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
			dimension.x, dimension.y, scale.x, scale.y, rotation,
			reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
			viewDirection == VIEW_DIRECTION.LEFT, false);
	}
	
	@Override
	public void beginContact(Contact contact) 
	{
		Fixture fa = contact.getFixtureA(); //the object being hit
		Fixture fb = contact.getFixtureB(); //should be the object colliding into b1
		
		if(fb.getBody() == body)
		{
			jumpState = JUMP_STATE.GROUNDED;
			body.setLinearVelocity(body.getLinearVelocity().x,0);
		}
	}

	@Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fb.getBody() == body && body.getLinearVelocity().y == 0)
		{
			jumpState = JUMP_STATE.FALLING;
				
		}

    }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}
