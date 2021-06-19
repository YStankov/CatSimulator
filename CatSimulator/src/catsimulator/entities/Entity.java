package catsimulator.entities;

import org.lwjgl.util.vector.Vector3f;

import catsimulator.model.TexturedModel;

/**
 * Container class that keeps the texture, position, scale and rotation values for an entity.
 */
public class Entity 
{
	private TexturedModel texturedModel;
	
	private Vector3f position;
	
	private float rotX;
	private float rotY;
	private float rotZ;
	
	private float scale;

	public Entity(TexturedModel texturedModel, Vector3f position, float rotX, float roty, float rotz, float scale) 
	{
		this.texturedModel = texturedModel;
		this.position = position;
		this.rotX = rotX;
		this.rotY = roty;
		this.rotZ = rotz;
		this.scale = scale;
	}

	public TexturedModel getTexturedModel() {
		return texturedModel;
	}

	public void setTexturedModel(TexturedModel texturedModel) {
		this.texturedModel = texturedModel;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRoty() {
		return rotY;
	}

	public void setRoty(float roty) {
		this.rotY = roty;
	}

	public float getRotz() {
		return rotZ;
	}

	public void setRotz(float rotz) {
		this.rotZ = rotz;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void increasePosition(float ix, float iy, float iz)
	{
		position.x += ix;
		position.y += iy;
		position.z += iz;
	}
	
	public void increaseRotation(float ix, float iy, float iz)
	{
		rotX += ix;
		rotY += iy;
		rotZ += iz;
	}
}
