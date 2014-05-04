package de.interoberlin.sugarmonkey.controller;

import de.interoberlin.sugarmonkey.view.panels.EPanel;
import android.app.Application;
import android.content.Context;

public class SugarMonkeyController extends Application
{
	private static Context	context;
	// private static Resources resources;
	private static boolean	running	= true;

	private static int		canvasHeight;
	private static int		canvasWidth;

	private static EPanel	currentPanel;

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

	public static EPanel getCurrentPanel()
	{
		return currentPanel;
	}

	public static void setCurrentPanel(EPanel currentPanel)
	{
		SugarMonkeyController.currentPanel = currentPanel;
	}
}
