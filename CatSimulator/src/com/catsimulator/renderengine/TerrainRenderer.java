package com.catsimulator.renderengine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.catsimulator.model.Model;
import com.catsimulator.shaders.TerrainShader;
import com.catsimulator.terrains.Terrain;
import com.catsimulator.texture.ModelTexture;
import com.catsimulator.tools.MathsTool;

public class TerrainRenderer 
{
	private TerrainShader terrainShader;
	
	public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix)
	{
		this.terrainShader = terrainShader;
		
		terrainShader.start();
		terrainShader.loadProjectiontionMatrix(projectionMatrix);
		terrainShader.stop();
	}
	
	public void render(List<Terrain> terrains)
	{
		for (Terrain terrain : terrains)
		{
			prepateTerrainModel(terrain);
			prepareTerrain(terrain);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexNumber(), GL11.GL_UNSIGNED_INT, 0);
			
		}
		removeTerrainTexturedModel();
	}
	
	private void prepateTerrainModel(Terrain terrain)
	{
		Model model = terrain.getModel();
		
		GL30.glBindVertexArray(model.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = terrain.getTexture();
		terrainShader.loadShineAndReflection(texture.getShine(), texture.getReflection());
	
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
	}
	
	private void removeTerrainTexturedModel()
	{
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0);
	}
	
	private void prepareTerrain(Terrain terrain)
	{
		Matrix4f transformmationMatrix = MathsTool.getTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				0, 
				0,
				0,
				1);

		terrainShader.loadTransformationMatrix(transformmationMatrix);
	}

}
