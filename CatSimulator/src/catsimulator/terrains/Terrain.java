package catsimulator.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import catsimulator.model.Model;
import catsimulator.renderengine.ModelLoader;
import catsimulator.texture.TerrainTexture;
import catsimulator.texture.Terrains;

public class Terrain 
{
	private static final float SIZE = 2048;
	private static final float MAX_HEIGHT = 50;
	private static final float MAX_COLOUR = 256 * 256 * 256;
	
	private float x;
	private float z;
	
	private Model model;
	private Terrains terrains;
	private TerrainTexture blendMap;
	
	public Terrain(int gridX, 
				   int gridZ, 
				   ModelLoader loader, 
				   Terrains terrains, 
				   TerrainTexture blendMap,
				   String heightMap)
	{
		this.terrains = terrains;
		this.blendMap = blendMap;
		
		this.x = -SIZE + gridX * SIZE;
		this.z = -SIZE + gridZ * SIZE;
		
		this.model = generateTerrain(loader, heightMap);
	}
	
	public Terrains getTerrains() {
		return terrains;
	}

	public void setTerrains(Terrains terrains) {
		this.terrains = terrains;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	public void setBlendMap(TerrainTexture blendMap) {
		this.blendMap = blendMap;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	private Model generateTerrain(ModelLoader loader, String heightMap)
	{
		BufferedImage heightMapImage = null;
		
		try {
			heightMapImage = ImageIO.read(new File("resources/" + heightMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int vertexCount = heightMapImage != null ? heightMapImage.getHeight() : 256;
		
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
		int vertexPointer = 0;
		for(int i=0;i<vertexCount;i++){
			for(int j=0;j<vertexCount;j++){
				vertices[vertexPointer*3] = (float)j/((float)vertexCount - 1) * SIZE;
				vertices[vertexPointer*3+1] = getTerrainHeight(j, i, heightMapImage);
				vertices[vertexPointer*3+2] = (float)i/((float)vertexCount - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)vertexCount - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<vertexCount-1;gz++){
			for(int gx=0;gx<vertexCount-1;gx++){
				int topLeft = (gz*vertexCount)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*vertexCount)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, indices, normals);
	}
	
	private float getTerrainHeight(int x, int y, BufferedImage image)
	{
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight())
		{
			return 0;
		}
		
		float currentHeight = image.getRGB(x, y);
		currentHeight += MAX_COLOUR/2f;
		currentHeight /= MAX_COLOUR/2f;
		currentHeight *= MAX_HEIGHT;
		
		return currentHeight;
	}
	
}
