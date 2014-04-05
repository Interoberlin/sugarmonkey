package de.interoberlin.sugarmonkey.view.panels;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;
import de.interoberlin.sugarmonkey.controller.parser.SvgHandler;
import de.interoberlin.sugarmonkey.model.svg.SVG;

public class DrawingPanel extends SurfaceView implements Runnable
{
    Thread		 thread  = null;
    SurfaceHolder	  surfaceHolder;
    private static boolean running = false;

    private static Context c;

    // private static Resources r;

    public DrawingPanel(Context context)
    {
	super(context);
	surfaceHolder = getHolder();

	c = (Context) SugarMonkeyController.getContext();
	// r = c.getResources();
    }

    public void onChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
    }

    public void onResume()
    {
	running = true;
	thread = new Thread(this);
	thread.start();
    }

    public void onPause()
    {
	boolean retry = true;
	running = false;

	while (retry)
	{
	    try
	    {
		thread.join();
		retry = false;
	    } catch (InterruptedException e)
	    {
		e.printStackTrace();
	    }
	}
    }

    public static boolean isRunning()
    {
	return running;
    }

    @Override
    public void run()
    {
	while (running)
	{
	    if (surfaceHolder.getSurface().isValid())
	    {
		// Lock canvas
		Canvas canvas = surfaceHolder.lockCanvas();

		// Set dimensions
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();

		SugarMonkeyController.setCanvasHeight(canvasHeight);
		SugarMonkeyController.setCanvasWidth(canvasWidth);

		/**
		 * Actual drawing
		 */

		Paint green = new Paint();
		green.setARGB(255, 50, 200, 50);

		canvas.drawRect(50, 50, canvasWidth - 50, canvasHeight - 50, green);

		SVG svg = getSVGFromAsset(c, "rectangle.svg");

		// s1.parseFromFile("rect.svg");
		// s1.renderToCanvas(canvas);

		// for (Element e : s1.getElementsByZIndex())
		// {
		// e.render();
		// }

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }

	}

    }

    private SVG getSVGFromAsset(Context c, String svgPath)
    {
	try
	{
	    return SvgHandler.getSVGFromAsset(c.getAssets(), svgPath);
	} catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (XmlPullParserException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }
}