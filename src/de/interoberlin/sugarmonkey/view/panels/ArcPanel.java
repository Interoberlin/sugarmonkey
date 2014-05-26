package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class ArcPanel extends APanel
{
	Thread					thread	= null;
	SurfaceHolder			surfaceHolder;
	private static boolean	running	= false;

	private static Context	c;

	// private static Resources r;

	public ArcPanel(Context context)
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
		SVG svg = SvgLoader.getSVGFromAsset(c, "arcs.svg");

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

				// Scale
				svg.setCanvasScaleMode(EScaleMode.FIT);
				svg.scale(canvasWidth, canvasHeight);

				// Load elements

				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}

		}

	}
}