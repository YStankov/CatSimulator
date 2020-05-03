package com.catsimulator.texture;

public class ModelTexture 
{
	private int id;
	
	private float shine = 1;
	private float reflection = 0;
	
	public ModelTexture(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}

	public float getShine() {
		return shine;
	}

	public void setShine(float shine) {
		this.shine = shine;
	}

	public float getReflection() {
		return reflection;
	}

	public void setReflection(float reflection) {
		this.reflection = reflection;
	}
}
