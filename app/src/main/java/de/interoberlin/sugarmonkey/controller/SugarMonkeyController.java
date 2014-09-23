package de.interoberlin.sugarmonkey.controller;

import android.app.Application;
import android.content.Context;

import de.interoberlin.sugarmonkey.controller.accelerometer.AcceleratorListener;

public class SugarMonkeyController extends Application
{
	private static Context	context;
	// private static Resources resources;
	private static boolean	running	= true;

	private static int		canvasHeight;
	private static int		canvasWidth;

	private static int		fps;
	private static int		currentFps;

	private static float	offsetX	= 0F;
	private static float	offsetY	= 0F;

	@Override
	public void onCreate()
	{
		super.onCreate();
		context = this;
		// resources = this.getResources();
	}

	public static Context getContext()
	{
		return context;
	}

	public static int getCanvasHeight()
	{
		return canvasHeight;
	}

	public static void setCanvasHeight(int canvasHeight)
	{
		SugarMonkeyController.canvasHeight = canvasHeight;
	}

	public static int getCanvasWidth()
	{
		return canvasWidth;
	}

	public static void setCanvasWidth(int canvasWidth)
	{
		SugarMonkeyController.canvasWidth = canvasWidth;
	}

	public static void start()
	{
		running = true;
	}

	public static void stop()
	{
		running = false;
	}

	public static boolean isRunning()
	{
		return running;
	}

	public static float getAccelerometerX()
	{
		return AcceleratorListener.getDataX();
	}

	public static float getAccelerometerY()
	{
		return AcceleratorListener.getDataY();
	}

	public static int getFps()
	{
		return fps;
	}

	public static void setFps(int fps)
	{
		SugarMonkeyController.fps = fps;
	}

	public static int getCurrentFps()
	{
		return currentFps;
	}

	public static void setCurrentFps(int currentFps)
	{
		SugarMonkeyController.currentFps = currentFps;
	}

	public static float getOffsetX()
	{
		return offsetX;
	}

	public static void setOffsetX(float offsetX)
	{
		SugarMonkeyController.offsetX = offsetX;
	}

	public static float getOffsetY()
	{
		return offsetY;
	}

	public static void setOffsetY(float offsetY)
	{
		SugarMonkeyController.offsetY = offsetY;
	}
}