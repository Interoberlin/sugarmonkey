package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.attributes.SVGTransformRotate;
import de.interoberlin.sauvignon.model.svg.attributes.SVGTransformScale;
import de.interoberlin.sauvignon.model.svg.elements.SVGGElement;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;
import de.interoberlin.sugarmonkey.view.activities.DrawingActivity;

public class MonkeyPanel extends APanel
{
	Thread					thread			= null;
	Thread					animateThread	= null;

	SurfaceHolder			surfaceHolder;
	private static boolean	running			= false;

	private static Context	c;

	private SVG				svg;

	public MonkeyPanel(Context context)
	{
		super(context);
		surfaceHolder = getHolder();

		c = (Context) SugarMonkeyController.getContext();
	}

	private void loadElements()
	{
		// Load SVG from file
		svg = SvgLoader.getSVGFromAsset(c, "yay.svg");
	}

	public void onChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}

	public void onResume()
	{
		loadElements();

		SugarMonkeyController.setFps(60);

		running = true;
		thread = new Thread(this);
		thread.start();

		animateThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int fps = SugarMonkeyController.getFps();
				long millisBefore = 0;
				long millisAfter = 0;
				long millisFrame = 1000 / fps;

				while (running)
				{
					if (millisAfter - millisBefore != 0)
					{
						SugarMonkeyController.setCurrentFps((int) (1000 / (millisAfter - millisBefore)));
						DrawingActivity.uiDraw();
					}

					millisBefore = System.currentTimeMillis();

					if (svg != null)
					{
						synchronized (svg)
						{
							// Load elements
							SVGGElement gArmLeft = ((SVGGElement) svg.getElementById("gArmLeft"));
							SVGGElement gArmRight = ((SVGGElement) svg.getElementById("gArmRight"));
							SVGGElement gBody = ((SVGGElement) svg.getElementById("gBody"));

							// Animate
							gArmLeft.animate(new SVGTransformRotate(2f, 2f, -0.1f));
							gArmRight.animate(new SVGTransformRotate(2f, 2f, 0.1f));
							gBody.animate(new SVGTransformScale(0.995f));
						}
					}

					millisAfter = System.currentTimeMillis();

					if (millisAfter - millisBefore < millisFrame)
					{
						try
						{
							Thread.sleep(millisFrame - (millisAfter - millisBefore));
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}

					millisAfter = System.currentTimeMillis();
				}
			}
		});
		animateThread.start();
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
		long millisBefore = 0;
		long millisAfter = 0;
		long millisFrame = 1000 / fps;

		// Set scale mode
		svg.setCanvasScaleMode(EScaleMode.FIT);

		while (running)
		{
			millisBefore = System.currentTimeMillis();

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
				svg.scale(canvasWidth, canvasHeight);

				// Render SVG
				synchronized (svg)
				{
					canvas = SvgRenderer.renderToCanvas(canvas, svg);
				}

				surfaceHolder.unlockCanvasAndPost(canvas);
			}

			millisAfter = System.currentTimeMillis();

			if (millisAfter - millisBefore < millisFrame)
			{
				try
				{
					Thread.sleep(millisFrame - (millisAfter - millisBefore));
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			millisAfter = System.currentTimeMillis();
		}
	}
}