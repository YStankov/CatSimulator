package catsimulator.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Container class that keeps the values for the light source position and colour
 */
public class Light {
	// A 3D vector that contains the light source position coordinates
	private Vector3f position;

	// A 3D vector that contains the light source colour
	private Vector3f colour;

	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
}
