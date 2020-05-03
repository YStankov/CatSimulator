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
import com.catsimulator.renderengine.ModelObjectsRenderer;
import com.catsimulator.renderengine.RenderEngine;
import com.catsimulator.shaders.StaticShader;
import com.catsimulator.terrains.Terrain;
import com.catsimulator.texture.ModelTexture;

public class CatSimulator {

	public static void main(String[] args) 
	{
		DisplayManager.createDisplay();
		ModelLoader loader = new ModelLoader();
	
		Model model = OBJLoader.loadObjModel("catobj", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("cat_diff.tga", "TGA")));
		
		ModelTexture texture = texturedModel.getModelTexture();
		texture.setShine(4);
		texture.setReflection(1);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		Terrain terrain1 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		Terrain terrain2 = new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		Terrain terrain3 = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		Terrain terrain4 = new Terrain(0, 1, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		Terrain terrain5 = new Terrain(1, 1, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		Terrain terrain6 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass.jpg", "JPG")));
		
		Camera camera = new Camera();
		
		RenderEngine renderer = new RenderEngine();
		while(!Display.isCloseRequested())
		{
			entity.increaseRotation(0, 1, 0);
			
			camera.move();
			
			renderer.addTerrain(terrain);
			renderer.addTerrain(terrain1);
			renderer.addTerrain(terrain2);
			renderer.addTerrain(terrain3);
			renderer.addTerrain(terrain4);
			renderer.addTerrain(terrain5);
			renderer.addTerrain(terrain6);
			renderer.processEntity(entity);
			renderer.render(camera, light);
			DisplayManager.updateDisplay();
		}
		
		renderer.clean();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
