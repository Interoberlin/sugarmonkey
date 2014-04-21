package de.interoberlin.sugarmonkey.view.panels;

import java.util.List;

import de.interoberlin.sauvignon.model.svg.elements.AElement;
import de.interoberlin.sauvignon.model.svg.elements.Circle;
import de.interoberlin.sauvignon.model.svg.elements.EElement;
import de.interoberlin.sauvignon.model.svg.elements.Path;
import de.interoberlin.sauvignon.model.svg.elements.Rect;
import de.interoberlin.sauvignon.model.util.Vector2;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class DrawingPanel extends SurfaceView implements Runnable
{
	Thread					thread	= null;
	SurfaceHolder			surfaceHolder;
	private static boolean	running	= false;

	private static Context	c;

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

	public boolean isRunning()
	{
		return running;
	}

	@Override
	public void run()
	{
		// Load SVG from file
		SVG svg = SvgLoader.getSVGFromAsset(c, "rectangle.svg");

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

				canvas.drawRGB(255, 255, 255);

				/**
				 * Actual drawing
				 */

				// Manipulate SVG
				Rect rectRed1 = (Rect) svg.getElementById(EElement.RECT, "rectRed1");
				Rect rectRed2 = (Rect) svg.getElementById(EElement.RECT, "rectRed2");
				Rect rectRed3 = (Rect) svg.getElementById(EElement.RECT, "rectRed3");

				Rect rectGreen1 = (Rect) svg.getElementById(EElement.RECT, "rectGreen1");
				Rect rectGreen2 = (Rect) svg.getElementById(EElement.RECT, "rectGreen2");
				Rect rectGreen3 = (Rect) svg.getElementById(EElement.RECT, "rectGreen3");

				Rect rectBlue1 = (Rect) svg.getElementById(EElement.RECT, "rectBlue1");
				Rect rectBlue2 = (Rect) svg.getElementById(EElement.RECT, "rectBlue2");
				Rect rectBlue3 = (Rect) svg.getElementById(EElement.RECT, "rectBlue3");

				Rect rectGrey = (Rect) svg.getElementById(EElement.RECT, "rectGrey");

				rectRed1.setFillR(rectRed1.getFillR() + 1);
				rectRed2.setFillG(rectRed2.getFillG() + 1);
				rectRed3.setFillB(rectRed3.getFillB() + 1);

				rectGreen1.setFillR(rectGreen1.getFillR() + 1);
				rectGreen2.setFillG(rectGreen2.getFillG() + 1);
				rectGreen3.setFillB(rectGreen3.getFillB() + 1);

				rectBlue1.setFillR(rectBlue1.getFillR() + 1);
				rectBlue2.setFillG(rectBlue2.getFillG() + 1);
				rectBlue3.setFillB(rectBlue3.getFillB() + 1);

				if (rectGrey.getWidth() > 0)
				{
					rectGrey.setWidth(rectGrey.getWidth() - 1);
				} else
				{
					rectGrey.setWidth(720);
				}

				rectGrey.setFillR(rectGrey.getFillR() + 1);
				rectGrey.setFillG(rectGrey.getFillG() + 1);
				rectGrey.setFillB(rectGrey.getFillB() + 1);

				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}

		}

	}
}