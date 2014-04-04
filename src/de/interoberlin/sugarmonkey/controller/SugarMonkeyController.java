package de.interoberlin.sugarmonkey.controller;

import android.app.Application;
import android.content.Context;

public class SugarMonkeyController extends Application
{
    private static Context context;
    // private static Resources resources;

    private static int     canvasHeight;
    private static int     canvasWidth;

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
}
