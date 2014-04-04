package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class DrawingPanel extends SurfaceView implements Runnable
{
    Thread		 thread  = null;
    SurfaceHolder	  surfaceHolder;
    private static boolean running = false;

    // private static Context c;

    // private static Resources r;

    public DrawingPanel(Context context)
    {
	super(context);
	surfaceHolder = getHolder();

	// c = (Context) SVGDemoController.getContext();
	// r = c.getResou<rces();
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

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	}
    }
}