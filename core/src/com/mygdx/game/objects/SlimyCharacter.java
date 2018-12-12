package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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

public class SlimyCharacter extends AbstractGameObject
{
	public static final String TAG = SlimyCharacter.class.getName();
	
	private final float JUMP_POWER = 10f;
	private final float MOVE_SPEED = 1.5F;
	private final float MAX_MOVE_SPEED = 6f;
	
	public enum VIEW_DIRECTION {LEFT, RIGHT}
	
	public enum JUMP_STATE {GROUNDED, FALLING, JUMPING}
	
	private Animation<TextureRegion> moving;
	private Animation<TextureRegion> redmoving;

	private ParticleEffect slimeParticle;
	
	public TextureRegion regBody;
	Array<TextureRegion> assets_moving;
	Array<TextureRegion> assets_redmoving;
	
	private VIEW_DIRECTION viewDirection;
	private JUMP_STATE jumpState;
	private float timeRed;

	private float timeFalling;
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
		
		moving = new Animation<TextureRegion>(0.25f, assets_moving);
		moving.setPlayMode(PlayMode.LOOP);
		redmoving = new Animation<TextureRegion>(0.25f, assets_redmoving);
		redmoving.setPlayMode(PlayMode.LOOP);
		
		stateTime = 0;
		timeFalling = 0;
		
		slimeParticle = new ParticleEffect();
		
		slimeParticle.load(new FileHandle("../core/assets/particles/dust.pfx"),
				new FileHandle("../core/assets/particles"));
		
		//center image on game object
		origin.set(dimension.x/2, dimension.y/2);
		
		//view direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		
		//jump state
		jumpState = JUMP_STATE.FALLING;
		
		timeRed = 0;
	}
	
	public void setRed()
	{
		timeRed += 5;
	}
	
	public boolean isRed() 
	{
		return timeRed==0 ? false : true;
	}
	
	public void jump(float deltaTime) 
	{
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
				if(timeFalling > 0.5)
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
				if(timeFalling > 0.5)
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
		
		if(jumpState == JUMP_STATE.FALLING)
			timeFalling += deltaTime;
		
		slimeParticle.update(deltaTime);
		
		timeRed = MathUtils.clamp(timeRed - deltaTime, 0, 999);
		stateTime += deltaTime;
	}
	
	@Override
	public void updateMotionX(float deltaTime)
	{
		if(jumpState == JUMP_STATE.GROUNDED && body.getLinearVelocity().x != 0)
		{
			slimeParticle.setPosition(position.x + dimension.x / 2, position.y + dimension.y /2);
			slimeParticle.start();
		}
		else
			slimeParticle.allowCompletion();

		position.x = body.getPosition().x;
	}
	
	@Override
	public void updateMotionY(float deltaTime)
	{
		if(body.getLinearVelocity().y < -0.5)
			jumpState = JUMP_STATE.FALLING;
		position.y = body.getPosition().y;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;

		slimeParticle.draw(batch);
		
		reg = regBody;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
			dimension.x, dimension.y, scale.x, scale.y, rotation,
			reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
			viewDirection == VIEW_DIRECTION.LEFT, false);
	}
	
	public void setGrounded()
	{
		jumpState = JUMP_STATE.GROUNDED;
			body.setLinearVelocity(body.getLinearVelocity().x,0);
	}
}
