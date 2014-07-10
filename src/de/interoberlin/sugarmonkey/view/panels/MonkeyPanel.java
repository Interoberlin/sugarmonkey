package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.elements.AGeometric;
import de.interoberlin.sauvignon.model.svg.elements.SVGGElement;
import de.interoberlin.sauvignon.model.svg.transform.SVGTransformRotate;
import de.interoberlin.sauvignon.model.svg.transform.SVGTransformTranslate;
import de.interoberlin.sauvignon.model.util.Vector2;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

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
		long millisBefore = 0;
		long millisAfter = 0;
		long millisFrame = 1000 / fps;
		
		svg = SvgLoader.getSVGFromAsset(c, "yay.svg");
		
		while (!surfaceHolder.getSurface().isValid())
			try
			{
				Thread.sleep(millisFrame);
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		// Set dimensions to fullscreen
		Canvas canvas = surfaceHolder.lockCanvas();

		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();

		SugarMonkeyController.setCanvasHeight(canvasHeight);
		SugarMonkeyController.setCanvasWidth(canvasWidth);

		surfaceHolder.unlockCanvasAndPost(canvas);

		// Set scale mode
		svg.setCanvasScaleMode(EScaleMode.FIT);
		svg.scaleTo(canvasWidth, canvasHeight);
		
		// Rotate arms und eyes
		SVGGElement gMain, gArmLeft, gArmRight, gEyeLeft, gEyeRight, gFootLeft, gFootRight;

		// Find elements to animate
		gArmLeft = (SVGGElement) svg.getElementById("gArmLeft");
		gArmRight = (SVGGElement) svg.getElementById("gArmRight");
		gEyeLeft = (SVGGElement) svg.getElementById("gEyeLeft");
		gEyeRight = (SVGGElement) svg.getElementById("gEyeRight");
		gFootLeft = (SVGGElement) svg.getElementById("gFootLeft");
		gFootRight = (SVGGElement) svg.getElementById("gFootRight");
		gMain = (SVGGElement) svg.getElementById("main");
		
		/*
		 * Define rotation centers
		 * 
		 * The coordinates refer to userspace.
		 * applyCTM transforms them to viewport coordinates. 
		 */
		Vector2 c1 = (new Vector2(275f, 177f));
		Vector2 c2 = (new Vector2(135f, 177f));
		Vector2 c3 = (new Vector2(230f, 085f));
		Vector2 c4 = (new Vector2(181f, 085f));
		Vector2 c5 = (new Vector2(150f, 260f));
		Vector2 c6 = (new Vector2(265f, 260f));
		Vector2 c7 = (new Vector2(150f, 170f));
		
		int frame = 0;
		int angle = 0;
		
		while (running)
		{
			millisBefore = System.currentTimeMillis();
			
/*			if (Math.round(frame/30) % 2 == 0)
			{
				angle += 1;
			} else {
				angle -= 1;
			}*/
			angle += 1;
			angle %= 360;
			
			frame += 1;
			frame %= 1000;

			gArmLeft.setAnimation(  new SVGTransformRotate(c1, (float) -angle) );
			gArmRight.setAnimation( new SVGTransformRotate(c2, (float) angle) );
			gFootLeft.setAnimation( new SVGTransformRotate(c5, (float) angle) );
			gFootRight.setAnimation(new SVGTransformRotate(c6, (float) -angle) );
			gEyeLeft.setAnimation(  new SVGTransformRotate(c3, (float) angle) );
			gEyeRight.setAnimation( new SVGTransformRotate(c4, (float) angle) );
			
//			gMain.setAnimation( new SVGTransformRotate(c7, angle) );
			
			if (surfaceHolder.getSurface().isValid() && svg.isChanged())
			{
				// Render SVG
				canvas = surfaceHolder.lockCanvas();

				// Clear canvas
				canvas.drawRGB(255, 255, 255);
				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);
				//svg.setChanged(false);

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
		}
	}
}
