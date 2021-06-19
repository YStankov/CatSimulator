package catsimulator.model;

import catsimulator.texture.ModelTexture;

/**
 * This class represents a model with a texture (a textured model). Keeps a model and a model texture objects.
 */
public class TexturedModel 
{
	private Model model;
	private ModelTexture modelTexture;
	
	public TexturedModel(Model model, ModelTexture modelTexture)
	{
		this.model = model;
		this.modelTexture = modelTexture;
	}

	public Model getModel() 
	{
		return model;
	}

	public ModelTexture getModelTexture() 
	{
		return modelTexture;
	}
}
