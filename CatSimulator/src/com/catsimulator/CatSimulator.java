package com.catsimulator;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.catsimulator.entities.Camera;
import com.catsimulator.entities.Entity;
import com.catsimulator.entities.Light;
import com.catsimulator.model.Model;
import com.catsimulator.model.TexturedModel;
import com.catsimulator.renderengine.DisplayManager;
import com.catsimulator.renderengine.ModelLoader;
import com.catsimulator.renderengine.OBJLoader;
import com.catsimulator.renderengine.RenderEngine;
import com.catsimulator.terrains.Terrain;
import com.catsimulator.texture.ModelTexture;

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
		
		Model model = OBJLoader.loadObjModel("tree", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree.png", "PNG")));
		
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		Terrain terrain1 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		
		Camera camera = new Camera();
		
		RenderEngine renderer = new RenderEngine();
		
		float[] treesX = new float [4096];
		float[] treesZ = new float [4096];
		float[] treeSize = new float [4096];
		
		int min = -2048;
		int maxZ = 0;
		int maxX = 2048;
		
		int minSize = 1;
		int maxSize = 3;
	
		for (int i = 0; i < 4096; i++)
		{
			float x = (float) ((Math.random() * ((maxX - min) + 1)) + min);
			treesX[i] = x;
			float z = (float) ((Math.random() * ((maxZ - min) + 1)) + min);
			treesZ[i] = z;
			
			float size = (float) ((Math.random() * ((maxSize - minSize) + 1)) + minSize);
			treeSize[i] = size;
			
		}
		
		while(!Display.isCloseRequested())
		{
			//entity.increaseRotation(0, 1, 0);
			
			camera.move();
			
			renderer.addTerrain(terrain);
			renderer.addTerrain(terrain1);

			for (int i = 0; i < treesX.length; i++)
			{	
				Entity entity = new Entity(texturedModel, new Vector3f(treesX[i], 0 , treesZ[i]), 0, 0, 0, treeSize[i]);
				renderer.processEntity(entity);
			}
			renderer.render(camera, light);
			DisplayManager.updateDisplay();
		}
		
		renderer.clean();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
