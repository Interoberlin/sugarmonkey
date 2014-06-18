package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.attributes.SVGTransformRotate;
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

	private SVG svg = new SVG();

	
	public MonkeyPanel(Context context)
	{
		super(context);
		surfaceHolder = getHolder();

		c = (Context) SugarMonkeyController.getContext();
	}

	public void onChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}

	public void onResume()
	{
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
				
				// Rotate arms und eyes
				SVGGElement gArmLeft, gArmRight, gEyeLeft, gEyeRight;

				// wait for svg
				if (svg == null)
					while (running)
					{
						try
						{
							Thread.sleep(millisFrame);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				
				if (svg != null)
				{
					synchronized (svg)
					{
						try
						{
							gArmLeft = (SVGGElement) svg.getElementById("gArmLeft");
							gArmRight = (SVGGElement) svg.getElementById("gArmRight");
							gEyeLeft = (SVGGElement) svg.getElementById("gEyeLeft");
							gEyeRight = (SVGGElement) svg.getElementById("gEyeRight");
						}
						catch (NullPointerException e)
						{
							// Error, unable to animate
							running = false;
							return;
						}
						finally
						{
							// Find elements to animate
							
							gArmLeft = (SVGGElement) svg.getElementById("gArmLeft");
							gArmRight = (SVGGElement) svg.getElementById("gArmRight");
							gEyeLeft = (SVGGElement) svg.getElementById("gEyeLeft");
							gEyeRight = (SVGGElement) svg.getElementById("gEyeRight");
							
							// Define animations
							
							gArmLeft.animate( new SVGTransformRotate(279f,370-211f,-0.01f) );
							gArmRight.animate( new SVGTransformRotate(128f,370-205f,0.01f) );
							gEyeLeft.animate( new SVGTransformRotate(231f,370-282f,0.05f) );
							gEyeRight.animate( new SVGTransformRotate(179f,370-283f,-0.05f) );
							
							// Execute animations
							
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
										gArmLeft.animateAgain();
										gArmRight.animateAgain();
										gEyeLeft.animateAgain();
										gEyeRight.animateAgain();
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
							} // while running
						} // elements found
					} // synchronized svg
				} // svg != null
			} // run()
		}); // animateThread
		
		animateThread.start();
	}

	public void onPause()
	{
		running = false;

		boolean retry = true;
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
		
		synchronized (svg)
		{
			svg = SvgLoader.getSVGFromAsset(c, "yay.svg");
		}
		
		while (!surfaceHolder.getSurface().isValid())
		{
			try
			{
				Thread.sleep(millisFrame);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
			
		// Set dimensions to fullscreen
		Canvas canvas = surfaceHolder.lockCanvas();

		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();

		SugarMonkeyController.setCanvasHeight(canvasHeight);
		SugarMonkeyController.setCanvasWidth(canvasWidth);

		synchronized (svg)
		{
			// Set scale mode
			svg.setCanvasScaleMode(EScaleMode.FIT);
			svg.scaleTo(canvasWidth, canvasHeight);
		}

		surfaceHolder.unlockCanvasAndPost(canvas);
		
		while (running)
		{
			if (surfaceHolder.getSurface().isValid())
			{
				millisBefore = System.currentTimeMillis();

				canvas = surfaceHolder.lockCanvas();

				// Clear canvas
				canvas.drawRGB(255, 255, 255);

				// Render SVG
				synchronized (svg)
				{
					canvas = SvgRenderer.renderToCanvas(canvas, svg);
				}

				surfaceHolder.unlockCanvasAndPost(canvas);

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
			} else {
				try
				{
					Thread.sleep(millisFrame);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
