package catsimulator;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import catsimulator.entities.Camera;
import catsimulator.entities.Cat;
import catsimulator.entities.Entity;
import catsimulator.entities.Light;
import catsimulator.model.Model;
import catsimulator.model.TexturedModel;
import catsimulator.renderengine.DisplayManager;
import catsimulator.renderengine.ModelLoader;
import catsimulator.renderengine.OBJLoader;
import catsimulator.renderengine.RenderEngine;
import catsimulator.terrains.Terrain;
import catsimulator.texture.ModelTexture;
import catsimulator.texture.TerrainTexture;
import catsimulator.texture.Terrains;

public class CatSimulator {

	public static void main(String[] args) 
	{
		DisplayManager.createDisplay();
		ModelLoader loader = new ModelLoader();
	
//		Model model = OBJLoader.loadObjModel("catobj", loader);
		
//		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("cat_diff.tga", "TGA")));
//		ModelTexture texture = texturedModel.getModelTexture();
//		texture.setShine(4);
//		texture.setReflection(1);
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass4.jpg", "JPG"));
		TerrainTexture redTexture = new TerrainTexture(loader.loadTexture("brownstone.jpg", "JPG"));
		TerrainTexture blueTexture = new TerrainTexture(loader.loadTexture("roadstone.jpg", "JPG"));
		TerrainTexture greenTexture = new TerrainTexture(loader.loadTexture("flowers.jpg", "JPG"));
		
		Terrains terrains = new Terrains(backgroundTexture, 
										 redTexture, 
										 greenTexture, 
										 blueTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap.png", "PNG"));
		
		Terrain terrain = new Terrain(0, 0, loader, terrains, blendMap, "height_map.png");
		Terrain terrain1 = new Terrain(1, 0, loader, terrains, blendMap, "height_map.png");
		
		Model treeModel = OBJLoader.loadObjModel("tree", loader);
		TexturedModel classicTreeTexturedModel = new TexturedModel(treeModel, 
																   new ModelTexture(loader.loadTexture("classictree.png", "PNG")));
		TexturedModel pinkTreeTexturedModel = new TexturedModel(treeModel, 
				   											    new ModelTexture(loader.loadTexture("pinktree.png", "PNG")));
		TexturedModel yellowTreeTexturedModel = new TexturedModel(treeModel, 
				   												  new ModelTexture(loader.loadTexture("yellowtree.png", "PNG")));
		
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		RenderEngine renderer = new RenderEngine();
		
		float[] treesX = new float [4096];
		float[] treesZ = new float [4096];
		float[] treeSize = new float [4096];
		
		int min = -2048;
		int maxZ = 0;
		int maxX = 2048;
		
		float minSize = 0.6f;
		float maxSize = 1.0f;
	
		for (int i = 0; i < 1024; i++)
		{
			float x = (float) ((Math.random() * ((maxX - min) + 1)) + min);
			treesX[i] = x;
			float z = (float) ((Math.random() * ((maxZ - min) + 1)) + min);
			treesZ[i] = z;
			
			float size = (float) ((Math.random() * ((maxSize - minSize) + 1)) + minSize);
			treeSize[i] = size;
			
		}
				
		Model model = OBJLoader.loadObjModel("catobj", loader);
		
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("cat_diff.tga", "TGA")));
		Cat cat = new Cat(texturedModel, new Vector3f(50, 0, -500), 0, 180, 0, 6f);
		
		Camera camera = new Camera(cat);
		
		while(!Display.isCloseRequested())
		{			
			camera.move();
			
			cat.move();
			
			renderer.processEntity(cat);
			
			renderer.addTerrain(terrain);
			renderer.addTerrain(terrain1);

			for (int i = 0; i < treesX.length; i++)
			{
				if (i <= 300)
				{
					Entity entity = new Entity(classicTreeTexturedModel, new Vector3f(treesX[i], 0 , treesZ[i]), 0, 0, 0, treeSize[i]);
					renderer.processEntity(entity);
				}
				else if (i <= 600)
				{
					Entity entity = new Entity(yellowTreeTexturedModel, new Vector3f(treesX[i], 0 , treesZ[i]), 0, 0, 0, treeSize[i]);
					renderer.processEntity(entity);
				}
				else
				{
					Entity entity = new Entity(pinkTreeTexturedModel, new Vector3f(treesX[i], 0 , treesZ[i]), 0, 0, 0, treeSize[i]);
					renderer.processEntity(entity);
				}
			}
			
			renderer.render(camera, light);
			DisplayManager.updateDisplay();
		}
		
		renderer.clean();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
