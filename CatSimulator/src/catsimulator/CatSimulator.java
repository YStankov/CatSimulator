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

// The main game class
public class CatSimulator {

	public static void main(String[] args) 
	{
		DisplayManager.createDisplay();
		ModelLoader loader = new ModelLoader();
		
		// Create the different terrain textures
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass4.jpg", "JPG"));
		TerrainTexture redTexture = new TerrainTexture(loader.loadTexture("brownstone.jpg", "JPG"));
		TerrainTexture blueTexture = new TerrainTexture(loader.loadTexture("roadstone.jpg", "JPG"));
		TerrainTexture greenTexture = new TerrainTexture(loader.loadTexture("flowers.jpg", "JPG"));
		
		// Create the terrains object that contains all different terrain textures
		Terrains terrains = new Terrains(backgroundTexture, 
										 redTexture, 
										 greenTexture, 
										 blueTexture);
		// Create the blend map terrain texture object (contains black/red/blue/green colours). The blend map determines which terrain should be shown in a given position.
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap.png", "PNG"));
		
		// Create two terrains - actually the game terrain is split in two parts: one start from the left side of the hero and the other on the right side.
		Terrain terrain = new Terrain(0, 0, loader, terrains, blendMap, "height_map.png");
		Terrain terrain1 = new Terrain(1, 0, loader, terrains, blendMap, "height_map.png");
		
		// Create the different tree models
		Model treeModel = OBJLoader.loadObjModel("tree", loader);
		TexturedModel classicTreeTexturedModel = new TexturedModel(treeModel, 
																   new ModelTexture(loader.loadTexture("classictree.png", "PNG")));
		TexturedModel pinkTreeTexturedModel = new TexturedModel(treeModel, 
				   											    new ModelTexture(loader.loadTexture("pinktree.png", "PNG")));
		TexturedModel yellowTreeTexturedModel = new TexturedModel(treeModel, 
				   												  new ModelTexture(loader.loadTexture("yellowtree.png", "PNG")));
		
		// Create the light source
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		RenderEngine renderer = new RenderEngine();
		
		// ---- Determines the locations on the map, where the trees are going to be rendered. ---
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
	    // ---
		
		// Load the cat model
		Model model = OBJLoader.loadObjModel("catobj", loader);
		
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("cat_diff.tga", "TGA")));
		
		// Create the cat textured model and give it a start location
		Cat cat = new Cat(texturedModel, new Vector3f(50, 0, -500), 0, 180, 0, 6f);
		
		Camera camera = new Camera(cat);
		
		// Start the main game loop
		while(!Display.isCloseRequested())
		{
			// calculates the camera position
			camera.move();
			
			// calculates the cat position and orientation
			cat.move();
			
			renderer.processEntity(cat);
			
			// render the terrain
			renderer.addTerrain(terrain);
			renderer.addTerrain(terrain1);

			// Render the trees - since we have tree different type of trees, they'll be rendered on a "random"
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
			// ----
			
			renderer.render(camera, light);
			DisplayManager.updateDisplay();
		}
		
		// on game exit, clean all rendered entities
		renderer.clean();
		loader.cleanUp();
		
		// close the display
		DisplayManager.closeDisplay();
	}
}
