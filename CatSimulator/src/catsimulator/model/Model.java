package catsimulator.model;

/**
 * Represents a 3D model loaded in memory. Contains the ID of the VAO that contains the model data and the number of vertices.
 */
public class Model 
{
	private int vaoId;
	private int vertexNumber;
	
	public Model(int vaoId, int vertexNumber)
	{
		this.vaoId =vaoId;
		this.vertexNumber = vertexNumber;
	}

	public int getVertexNumber() 
	{
		return vertexNumber;
	}

	public int getVaoId() 
	{
		return vaoId;
	}
}
