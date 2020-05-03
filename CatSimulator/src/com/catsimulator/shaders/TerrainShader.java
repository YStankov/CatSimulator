package com.catsimulator.shaders;

import org.lwjgl.util.vector.Matrix4f;

import com.catsimulator.entities.Camera;
import com.catsimulator.entities.Light;
import com.catsimulator.tools.MathsTool;

public class TerrainShader extends ShaderController
{
	private static final String VERTEX_FILE_PATH = "src/com/catsimulator/shaders/vertexShaderTerrain.txt";
	private static final String FRAGMENT_FILE_PATH = "src/com/catsimulator/shaders/fragmentShaderTerrain.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	private int location_lightColour;
	private int location_lightPosition;
	
	private int location_shine;
	private int location_reflection;
	
	public TerrainShader()
	{
		super(VERTEX_FILE_PATH, FRAGMENT_FILE_PATH);
	}
	
	@Override
	protected void bindAttributes()
	{
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
		bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocaltions() 
	{
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		
		location_lightColour = getUniformLocation("lightColour");
		location_lightPosition = getUniformLocation("lightPosition");
		
		location_shine = getUniformLocation("shine");
		location_reflection = getUniformLocation("reflection");
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
	
	public void loadLight(Light light) 
	{
		loadVector(location_lightColour, light.getColour());
		loadVector(location_lightPosition, light.getPosition());
	}
	
	public void loadShineAndReflection(float shine, float reflection)
	{
		loadFloat(location_shine, shine);
		loadFloat(location_reflection, reflection);
	}
}
