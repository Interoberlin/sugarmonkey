package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class DebugPanel extends APanel
{
	Thread					thread	= null;
	SurfaceHolder			surfaceHolder;
	private static boolean	running	= false;

	private static Context	c;

	// private static Resources r;

	public DebugPanel(Context context)
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
		SugarMonkeyController.setFps(30);
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
		int fps = SugarMonkeyController.getFps();
//		long millisBefore = 0;
//		long millisAfter = 0;
		long millisFrame = 1000 / fps;

		// Load SVG from file
		SVG svg = SvgLoader.getSVGFromAsset(c, "debug.svg");

		while (!surfaceHolder.getSurface().isValid())
		{
			try
			{
				Thread.sleep(millisFrame);
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// Set dimensions to fullscreen
		Canvas c = surfaceHolder.lockCanvas();

		int canvasWidth = c.getWidth();
		int canvasHeight = c.getHeight();

		SugarMonkeyController.setCanvasHeight(canvasHeight);
		SugarMonkeyController.setCanvasWidth(canvasWidth);

		surfaceHolder.unlockCanvasAndPost(c);

		// Set scale mode
		svg.setCanvasScaleMode(EScaleMode.FIT);
		svg.scaleTo(canvasWidth, canvasHeight);

		while (running)
		{
			if (surfaceHolder.getSurface().isValid())
			{
				// Lock canvas
				Canvas canvas = surfaceHolder.lockCanvas();

				/**
				 * Clear canvas
				 */

				canvas.drawRGB(255, 255, 255);

				/**
				 * Actual drawing
				 */

				// Load elements
				// SVGGElement gArmLeft = (SVGGElement)
				// svg.getElementById("gArmLeft");

				// Manipulate elements

				// Render SVG
				 canvas = SvgRenderer.renderRasterToCanvas(canvas, svg);
				canvas = SvgRenderer.renderToCanvas(canvas, svg);
				canvas = SvgRenderer.renderBoundingRectsToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
}