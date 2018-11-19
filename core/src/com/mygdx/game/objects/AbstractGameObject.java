package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractGameObject {
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	public Vector2 V;
	public Vector2 terminalVelocity;
	public float friction;
	public Rectangle bounds;
	public Body body;
	
	public AbstractGameObject () 
	{
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		terminalVelocity = new Vector2(1,1);
		friction = 0.05f;
		bounds = new Rectangle();
	}
	
	protected void updateMotionX (float deltaTime) 
	{
		if(body.getLinearVelocity().x > 0)
			body.setLinearVelocity(body.getLinearVelocity().x - friction, body.getLinearVelocity().y);
		else if(body.getLinearVelocity().x < 0)
			body.setLinearVelocity(body.getLinearVelocity().x + friction, body.getLinearVelocity().y);
		
		position.set(position.x + body.getLinearVelocity().x, position.y + body.getLinearVelocity().y);
	}
	
	protected void updateMotionY (float deltaTime) 
	{
		
	}
	public void update (float deltaTime) 
	{
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		// Move to new position
		
	}
	public abstract void render (SpriteBatch batch);
}