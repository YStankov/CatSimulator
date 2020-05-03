package com.catsimulator.renderengine;

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
import com.catsimulator.tools.MathsTool;

public class RenderEngine 
{
	private static final float FIELD_OF_VIEW = 70;
	
	private static final float NEAR_PLANE = 0.1f;
	
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	public RenderEngine(StaticShader shader)
	{
		createProjectionMatrix();
		shader.start();
		shader.loadProjectiontionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1, 0, 0, 1);
	}
	
	
	public void render(Entity entity, StaticShader shader)
	{
		TexturedModel texturedModel = entity.getTexturedModel();
		Model model = texturedModel.getModel();
		
		GL30.glBindVertexArray(model.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		Matrix4f transformmationMatrix = MathsTool.getTransformationMatrix(entity.getPosition(), 
																		   entity.getRotX(), 
																		   entity.getRoty(),
																		   entity.getRotz(),
																		   entity.getScale());
		
		shader.loadTransformationMatrix(transformmationMatrix);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexNumber(), GL11.GL_UNSIGNED_INT, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getId());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexNumber());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void createProjectionMatrix()
	{
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FIELD_OF_VIEW / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
