package catsimulator.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderController 
{
	private int controllerId;
	private int vertexShaderId;
	private int fragmentShaderId;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderController(String vertexFile,String fragmentFile)
	{
		vertexShaderId = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderId = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		
		controllerId = GL20.glCreateProgram();
		GL20.glAttachShader(controllerId, vertexShaderId);
		GL20.glAttachShader(controllerId, fragmentShaderId);
		bindAttributes();
		GL20.glLinkProgram(controllerId);
		GL20.glValidateProgram(controllerId);
		
		getAllUniformLocaltions();
	}
	
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value)
	{
		GL20.glUniform1i(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadBoolean(int location, boolean value)
	{
		float floatValue = value ? 1 : 0;
		
		GL20.glUniform1f(location, floatValue);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	protected int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(controllerId, uniformName);
	}
	
	protected abstract void getAllUniformLocaltions();
	
	public void start()
	{
		GL20.glUseProgram(controllerId);
	}
	
	
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	
	public void cleanUp()
	{
		stop();
		
		GL20.glDetachShader(controllerId, vertexShaderId);
		GL20.glDetachShader(controllerId, fragmentShaderId);
		
		GL20.glDeleteShader(vertexShaderId);
		GL20.glDeleteShader(fragmentShaderId);
		
		GL20.glDeleteProgram(controllerId);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName)
	{
		GL20.glBindAttribLocation(controllerId, attribute, variableName);
	}
	
	private static int loadShader(String file, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}
		catch(IOException e)
		{
			System.out.println("Failed to read shader file: " + file);
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderId = GL20.glCreateShader(type);
		
		GL20.glShaderSource(shaderId, shaderSource);
		GL20.glCompileShader(shaderId);
		
		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.out.println(GL20.glGetShaderInfoLog(shaderId, 512));
			System.out.println("Couldn't compile shader.");
			System.exit(-1);
		}
		
		return shaderId;
	}
	
}
