package catsimulator.renderengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import catsimulator.entities.Camera;
import catsimulator.entities.Entity;
import catsimulator.entities.Light;
import catsimulator.model.TexturedModel;
import catsimulator.shaders.StaticShader;
import catsimulator.shaders.TerrainShader;
import catsimulator.terrains.Terrain;

public class RenderEngine 
{
	private static final float FIELD_OF_VIEW = 70;
	
	private static final float NEAR_PLANE = 0.1f;
	
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	private ModelObjectsRenderer modelObjectRenderer = null;
	private TerrainRenderer terrainRenderer = null;
	
	private StaticShader shader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public RenderEngine()
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		createProjectionMatrix();
		
		modelObjectRenderer = new ModelObjectsRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public void render(Camera camera, Light light)
	{
		prepare();
		
		shader.start();
		shader.loadLight(light);
		shader.loadViewtionMatrix(camera);
		modelObjectRenderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(light);
		terrainShader.loadViewtionMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void addTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity)
	{
		TexturedModel texturedModel = entity.getTexturedModel();
		List<Entity> entityInstances = entities.get(texturedModel);
		
		if (entityInstances != null)
		{
			entityInstances.add(entity);
		}
		else
		{
			List<Entity> newInstances = new ArrayList<Entity>();
			newInstances.add(entity);
			entities.put(texturedModel, newInstances);
		}
	}
	
	public void clean()
	{
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	
	private void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.74902f, 0.847059f, 0.847059f, 1);
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
