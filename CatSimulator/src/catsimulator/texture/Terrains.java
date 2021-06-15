package catsimulator.texture;

public class Terrains 
{
	private TerrainTexture background;
	private TerrainTexture red;
	private TerrainTexture green;
	private TerrainTexture blue;
	
	public Terrains(TerrainTexture background, TerrainTexture red, TerrainTexture green, TerrainTexture blue) 
	{
		this.background = background;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public TerrainTexture getBackground() {
		return background;
	}

	public void setBackground(TerrainTexture background) {
		this.background = background;
	}

	public TerrainTexture getRed() {
		return red;
	}

	public void setRed(TerrainTexture red) {
		this.red = red;
	}

	public TerrainTexture getGreen() {
		return green;
	}

	public void setGreen(TerrainTexture green) {
		this.green = green;
	}

	public TerrainTexture getBlue() {
		return blue;
	}

	public void setBlue(TerrainTexture blue) {
		this.blue = blue;
	}
	
}
