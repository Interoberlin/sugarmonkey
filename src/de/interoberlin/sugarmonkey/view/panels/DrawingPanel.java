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
import de.interoberlin.sugarmonkey.model.svg.elements.AElement;
import de.interoberlin.sugarmonkey.model.svg.elements.EElement;
import de.interoberlin.sugarmonkey.model.svg.elements.G;
import de.interoberlin.sugarmonkey.model.svg.elements.Rect;

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
	// Load SVG from file
	SVG svg = getSVGFromAsset(c, "rectangle.svg");

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
		 * Clear canvas
		 */

		Paint backgroundPaint = new Paint();
		backgroundPaint.setARGB(255, 255, 255, 255);
		canvas.drawRect(0, 0, canvasWidth, canvasHeight, backgroundPaint);

		/**
		 * Actual drawing
		 */

		// Manipulate SVG
		Rect r = (Rect) svg.getElementById(EElement.RECT, "rect2985");
		r.setWidth(r.getWidth() - 1);

		// Render SVG
		canvas = renderSVG(canvas, svg);

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }

	}

    }

    public Canvas renderSVG(Canvas canvas, SVG svg)
    {
	G g = svg.getG();

	for (AElement e : g.getSubelements())
	{
	    if (e.getType() == EElement.RECT)
	    {
		Rect r = (Rect) e;
		float width = r.getWidth();
		float height = r.getHeight();
		float x = r.getX();
		float y = r.getY();

		Paint p = new Paint();
		p.setARGB(255, 150, 150, 150);

		canvas.drawRect(x, y, x + width, y + height, p);
	    }
	}

	return canvas;
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