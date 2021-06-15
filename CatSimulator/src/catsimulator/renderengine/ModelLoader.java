package catsimulator.renderengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import catsimulator.model.Model;

public class ModelLoader 
{
	List<Integer> vaoList = new ArrayList<Integer>();
	List<Integer> vboList = new ArrayList<Integer>();
	List<Integer> textureList = new ArrayList<Integer>();
	
	public Model loadToVAO(float[] points, float[] textureCoords, int[] indices, float[] normals)
	{
		int vaoId = createVAO();
		bindIndicesBuffer(indices);
		storeModelData(0, 3, points);
		storeModelData(1, 2, textureCoords);
		storeModelData(2, 3, normals);
		unbindVAO();
		
		return new Model(vaoId, indices.length);
	}
	
	public int loadTexture(String textureFilePath, String textureFormat)
	{
		Texture texture = null;
		
		try 
		{
			texture = TextureLoader.getTexture(textureFormat, new FileInputStream("resources/" + textureFilePath));
			textureList.add(texture.getTextureID());
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		textureList.add(texture.getTextureID());
		
		return texture.getTextureID();
	}
	
	
	public void cleanUp()
	{
		vaoList.stream().forEach(vao -> GL30.glDeleteVertexArrays(vao));
		vaoList.stream().forEach(vbo -> GL15.glDeleteBuffers(vbo));
		textureList.stream().forEach(texture -> GL11.glDeleteTextures(texture));
	}
	
	
	private int createVAO()
	{
		int vaoId = GL30.glGenVertexArrays();
		vaoList.add(vaoId);
		
		
		GL30.glBindVertexArray(vaoId);
		
		return vaoId;
	}
	
	
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vboList.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private void storeModelData(int attributeNumber, int coordinateSize, float[] modelData)
	{
		int vboId = GL15.glGenBuffers();
		vboList.add(vboId);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		
		FloatBuffer buffer = storeModelDataInFloatBuffer(modelData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
	private FloatBuffer storeModelDataInFloatBuffer(float[] modelData)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(modelData.length);
		buffer.put(modelData);
		buffer.flip();
		
		return buffer;
	}
}
