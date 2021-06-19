package catsimulator.renderengine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Responsible for setting-up and maintaining the game display.
 */
public class DisplayManager 
{
	private static final String DISPLAY_TITLE= "CAT SIMULATOR";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS = 60;
	
	private static long lastFrameTime;
	private static float delta;
	
	
	/**
	 * Creates a display window on which the game is going to be rendered. 
	 * The dimensions are set-up by using the display mode.
	 */
	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3,2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(DISPLAY_TITLE);
			Display.setInitialBackground(0, 20, 0);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		lastFrameTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	
	/**
	 * Updates the display at the end of each frame.
	 */
	public static void updateDisplay()
	{	
		Display.sync(FPS);
		Display.update();
		
		long currentFrameTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		
		lastFrameTime = currentFrameTime;
	}
	
	
	/**
	 * Closes the window of the game at the game end.
	 */
	public static void closeDisplay()
	{
		Display.destroy();
	}
	
	
	/**
	 * Gets the display delta.
	 */
	public static float getDelta()
	{
		return delta;
	}
}
