package com.catsimulator.shaders;

import org.lwjgl.util.vector.Matrix4f;

import com.catsimulator.entities.Camera;
import com.catsimulator.tools.MathsTool;

public class StaticShader extends ShaderController 
{
	private static final String VERTEX_FILE_PATH = "src/com/catsimulator/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE_PATH = "src/com/catsimulator/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public StaticShader()
	{
		super(VERTEX_FILE_PATH, FRAGMENT_FILE_PATH);
	}
	
	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocaltions() 
	{
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) 
	{
		loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectiontionMatrix(Matrix4f matrix) 
	{
		loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewtionMatrix(Camera camera) 
	{
		Matrix4f matrix = MathsTool.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, matrix);
	}
}
