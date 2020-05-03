package com.catsimulator.renderengine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager 
{
	private static final String DISPLAY_TITLE= "CAT SIMULATOR";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS = 60;
	
	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3,2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(DISPLAY_TITLE);
			Display.setInitialBackground(1, 1, 1);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void updateDisplay()
	{	
		Display.sync(FPS);
		Display.update();
	}
	
	public static void closeDisplay()
	{
		Display.destroy();
	}
}
