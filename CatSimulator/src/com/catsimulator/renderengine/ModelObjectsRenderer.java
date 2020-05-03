package com.catsimulator.renderengine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.catsimulator.entities.Entity;
import com.catsimulator.model.Model;
import com.catsimulator.model.TexturedModel;
import com.catsimulator.shaders.StaticShader;
import com.catsimulator.texture.ModelTexture;
import com.catsimulator.tools.MathsTool;

public class ModelObjectsRenderer 
{	
	private StaticShader shader = new StaticShader();
	
	public ModelObjectsRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		
		shader.start();
		shader.loadProjectiontionMatrix(projectionMatrix);
		shader.stop();
	}
	
	
	public void render(Map<TexturedModel, List<Entity>> entities)
	{
		for (TexturedModel model : entities.keySet())
		{
			prepateTexturedModel(model);
			
			List<Entity> entityInstances = entities.get(model);
			entityInstances.forEach(e -> {
				prepareEntity(e);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexNumber(), GL11.GL_UNSIGNED_INT, 0);
			});
			
			removeTexturedModel();
		}
	}
	
	private void prepateTexturedModel(TexturedModel texturedModel)
	{
		Model model = texturedModel.getModel();
		
		GL30.glBindVertexArray(model.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = texturedModel.getModelTexture();
		shader.loadShineAndReflection(texture.getShine(), texture.getReflection());
	
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getId());
	}
	
	private void removeTexturedModel()
	{
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0);
	}
	
	private void prepareEntity(Entity entity)
	{
		Matrix4f transformmationMatrix = MathsTool.getTransformationMatrix(entity.getPosition(), 
				   entity.getRotX(), 
				   entity.getRoty(),
				   entity.getRotz(),
				   entity.getScale());

		shader.loadTransformationMatrix(transformmationMatrix);
	}
}
