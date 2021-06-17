package catsimulator.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0, 20, 0);
	private float pitch = 10;
	private float yaw;
	private float roll;
	
	private float cameraToPlayerDistance = 50;
	private float cameraToPlayerAngle = 0;
	private Cat cat;
	
	public Camera(Cat cat)
	{
		this.cat = cat;
	}

	public void move() {
		calculateCameraToPlayerDistance();
		calculateCameraToPlayerAngle();
		calculatePitchLevel();
		
		float horizontal = calculateHorizontalDistance();
		float vertical = calculateVerticalDistance();
		
		calculateCameraPosition(horizontal, vertical);
		
		yaw = 180 - (cat.getRoty() + cameraToPlayerAngle);
	}

	public Vector3f getPosition() 
	{
		return position;
	}

	public float getPitch() 
	{
		return pitch;
	}

	public float getYaw() 
	{
		return yaw;
	}

	public float getRoll() 
	{
		return roll;
	}
	
	private void calculateCameraToPlayerDistance()
	{
		cameraToPlayerDistance = 0.1f * Mouse.getDWheel();
	}
	
	private void calculateCameraToPlayerAngle()
	{
		if (Mouse.isButtonDown(0))
		{
			cameraToPlayerAngle -= 0.2f * Mouse.getDX();
		}
	}
	
	private void calculatePitchLevel()
	{
		float pitchChange = Mouse.getDY() * 0.1f;
		pitch -= pitchChange;
		if(pitch < 10)
			pitch = 10;
		else if(pitch > 90)
			pitch = 90;
	}
	
	private float calculateHorizontalDistance()
	{
		float hD = (float) (cameraToPlayerDistance * Math.cos(Math.toRadians(pitch)));
		return hD < 20 ? 20 : hD;
	}
	
	private float calculateVerticalDistance()
	{
		float vD = (float) (cameraToPlayerDistance * Math.sin(Math.toRadians(pitch)));
		return vD < 10 ? 10: vD;
	}
	
	private void calculateCameraPosition(float horizontal, float vertical)
	{
		position.y = cat.getPosition().y + vertical;
		
		float t = cat.getRoty() + cameraToPlayerAngle;
		float offsetX = (float) (horizontal * Math.sin(Math.toRadians(t)));
		float offsetZ = (float) (horizontal * Math.cos(Math.toRadians(t)));
		
		position.x = cat.getPosition().x - offsetX;
		position.z = cat.getPosition().z - offsetZ;
	}
}
