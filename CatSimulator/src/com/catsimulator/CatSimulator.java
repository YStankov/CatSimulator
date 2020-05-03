package com.catsimulator;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.catsimulator.entities.Camera;
import com.catsimulator.entities.Entity;
import com.catsimulator.model.Model;
import com.catsimulator.model.TexturedModel;
import com.catsimulator.renderengine.DisplayManager;
import com.catsimulator.renderengine.ModelLoader;
import com.catsimulator.renderengine.OBJLoader;
import com.catsimulator.renderengine.RenderEngine;
import com.catsimulator.shaders.StaticShader;
import com.catsimulator.texture.ModelTexture;

public class CatSimulator {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		ModelLoader loader = new ModelLoader();
		StaticShader shader = new StaticShader();
		RenderEngine renderEngine = new RenderEngine(shader);
	
		Model model = loader.loadModel("dragon.obj");
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested())
		{
			entity.increaseRotation(0, 1, 0);
			
			camera.move();
			
			renderEngine.prepare();
			
			shader.start();
			shader.loadViewtionMatrix(camera);
			renderEngine.render(entity, shader);
			shader.stop();
			
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
