package com.mygdx.game.objects;

import java.awt.desktop.SystemEventListener;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;

public class SlimyCharacter extends AbstractGameObject implements ContactListener
{
	public static final String TAG = SlimyCharacter.class.getName();
	
	private final float JUMP_POWER = 0.75f;
	private final float GRAVITY = 0.05f;
	private final float MOVE_SPEED = .15F;
	private final float MAX_MOVE_SPEED = .5f;
	
	public enum VIEW_DIRECTION {LEFT, RIGHT}
	
	public enum JUMP_STATE {GROUNDED, FALLING, JUMPING}
	
	public TextureRegion regBody;
	
	private VIEW_DIRECTION viewDirection;
	private JUMP_STATE jumpState;
	private float timeRed;
	
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
		
		timeRed = MathUtils.clamp(timeRed - deltaTime, 0, 999);
	}
	
	@Override
	public void updateMotionX(float deltaTime)
	{
		body.setAwake(true);
		switch(viewDirection)
		{
		case LEFT:
			body.setLinearVelocity(MathUtils.clamp(body.getLinearVelocity().x + friction, -MAX_MOVE_SPEED, 0), body.getLinearVelocity().y);
			break;
		case RIGHT:
			body.setLinearVelocity(MathUtils.clamp(body.getLinearVelocity().x - friction, 0, MAX_MOVE_SPEED), body.getLinearVelocity().y);
			break;
		}
		
		position.x += body.getLinearVelocity().x;
		body.setTransform(position.x, position.y, 0);
	}
	
	@Override
	public void updateMotionY(float deltaTime)
	{
		body.setAwake(true);
		if(body.getLinearVelocity().y == 0)
			jumpState = JUMP_STATE.GROUNDED;
		if(jumpState!=JUMP_STATE.GROUNDED)
		{
			body.setLinearVelocity(body.getLinearVelocity().x,MathUtils.clamp(body.getLinearVelocity().y-GRAVITY, -1, 3));
			position.y += body.getLinearVelocity().y;
		}
		if(body.getLinearVelocity().y<0)
			jumpState = JUMP_STATE.FALLING;
		
		body.setTransform(position.x, position.y, 0);
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
	
	@Override
	public void beginContact(Contact contact) 
	{
		Fixture fa = contact.getFixtureA(); //the object being hit
		Fixture fb = contact.getFixtureB(); //should be the object colliding into b1
		
		System.out.println(fa.getBody().getType()+" has hit "+ fb.getBody().getType());		
	}

	@Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        System.out.println(fixtureA.toString() + " and " + fixtureB.toString());
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
