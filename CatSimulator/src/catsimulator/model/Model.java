package catsimulator.model;

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
