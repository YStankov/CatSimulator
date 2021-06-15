package catsimulator.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import catsimulator.model.TexturedModel;
import catsimulator.renderengine.DisplayManager;

public class Cat extends Entity
{
	private static final float MOVEMENT_SPEED = 50;
	
	private static final float TURN_SPEED = 150;
	
	private static final float GRAVITY = -30;
	
	private static final float JUMP_HEIGHT = 10;
	
	private static final float TERRAIN_UP_POSITION = 0;
	
	private float currentMovementSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currentUpSpeed = 0;

	public Cat(TexturedModel texturedModel, Vector3f position, float rotX, float roty, float rotz, float scale) 
	{
		super(texturedModel, position, rotX, roty, rotz, scale);
	}
	
	public void move()
	{
		getKeyboardInputs();
		
		increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta(), 0);
		
		float distance = currentMovementSpeed * DisplayManager.getDelta();
		float dx = (float) (Math.sin(Math.toRadians(getRoty())) * distance);
		float dz = (float) (Math.cos(Math.toRadians(getRoty())) * distance);
		
		increasePosition(dx, 0, dz);
		
		currentUpSpeed += GRAVITY * DisplayManager.getDelta();
		
		increasePosition(0, currentUpSpeed * DisplayManager.getDelta(), 0);
		
		if (getPosition().y < TERRAIN_UP_POSITION)
		{
			currentUpSpeed = 0;
			getPosition().y = TERRAIN_UP_POSITION;
		}
	}
	
	public void getKeyboardInputs()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			this.currentMovementSpeed = MOVEMENT_SPEED;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			this.currentMovementSpeed = -MOVEMENT_SPEED;
		}
		else
		{
			this.currentMovementSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			this.currentTurnSpeed = -TURN_SPEED;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			this.currentTurnSpeed = TURN_SPEED;
		}
		else
		{
			this.currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			currentUpSpeed = JUMP_HEIGHT;
		}
	}
}
