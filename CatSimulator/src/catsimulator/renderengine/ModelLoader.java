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

/**
 * Loads the geometry data into the VAOs. Keeps track of all VAOs, VBOs and textures, so we can delete them at the game end.
 */
public class ModelLoader 
{
	List<Integer> vaoList = new ArrayList<Integer>();
	List<Integer> vboList = new ArrayList<Integer>();
	List<Integer> textureList = new ArrayList<Integer>();
	
	
	/**
     * Creates a VAO and stores:
     * the position data of the vertices into attribute 0 of the VAO;
     * the texture coordinates into attribute 1 of the VAO;
     * the normals into the attribute 2 of the VAO;
     * 
     * The indices are stored in an index buffer and bound to the VAO.
     * 
	 * @param points - the position of each vertex
	 * @param textureCoords
	 * @param indices - the indices of the model that we want to store in the VAO 
	 *                  (indicate how the vertices should be connected together to form triangles)
	 * @param normals - describes each vertex orientation
	 * @return the loaded model
	 */
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
	
	
	/**
	 * Creates a new texture and puts it in the texture list of the model.
	 * 
	 * @param textureFilePath - the path to where the texture image is being stored
	 * @param textureFormat - the format of the texture file (JPG, PNG, etc.)
	 * 
	 * @return the texture ID
	 */
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
	
	
	/**
	 * CleanUps (deletes) all the VAOs, VBOs and textures from the memory when the game is closed.
	 */
	public void cleanUp()
	{
		vaoList.stream().forEach(vao -> GL30.glDeleteVertexArrays(vao));
		vaoList.stream().forEach(vbo -> GL15.glDeleteBuffers(vbo));
		textureList.stream().forEach(texture -> GL11.glDeleteTextures(texture));
	}
	
	
	/**
	 * Creates a new VAO entry that holds geometry render data and is physically stored in memory, 
	 * so we can quickly access it for rendering.
	 * 
	 * @return the VAO ID
	 */
	private int createVAO()
	{
		int vaoId = GL30.glGenVertexArrays();
		vaoList.add(vaoId);
		
		
		GL30.glBindVertexArray(vaoId);
		
		return vaoId;
	}
	
	
	/**
	 * Unbinds a VAO after we're finished using it.
	 */
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	
	/**
	 * Creates an index buffer, binds the index buffer to the currently active VAO and fills it with the indices data.
	 * 
	 * @param indices - the indices of the model that we want to store in the VAO
	 */
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vboList.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	
	/**
	 * Creates an IntBuffer from the indices array, so we can store it in the VBO.
	 */
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	
	/**
	 * Stores the position of the vertices into attribute 0 of the VAO. 
	 * The positions must first be stored in a VBO (in the memory of the GPU, for quick access).
	 * access during rendering.
	 * 
	 * @param attributeNumber - the number of the attribute of the VAO where the data is going to be stored
	 * @param coordinateSize - the coordinate size
	 * @param modelData - the positions of the vertices to be stored in the VAO
	 */
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
	
	
	/**
	 * Creates a FloatBuffer from the model data array, so we can store it in the VBO.
	 */
	private FloatBuffer storeModelDataInFloatBuffer(float[] modelData)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(modelData.length);
		buffer.put(modelData);
		buffer.flip();
		
		return buffer;
	}
}
