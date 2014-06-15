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
import de.interoberlin.sauvignon.model.util.Matrix;
import de.interoberlin.sauvignon.model.util.Vector2;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class MonkeyPanel extends APanel
{
	Thread					thread	= null;
	SurfaceHolder			surfaceHolder;
	private static boolean	running	= false;

	private static Context	c;

	// private static Resources r;

	public MonkeyPanel(Context context)
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
		SVG svg = SvgLoader.getSVGFromAsset(c, "yay.svg");

		// Set dimensions
		Canvas canvas = surfaceHolder.lockCanvas();

		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();

		SugarMonkeyController.setCanvasHeight(canvasHeight);
		SugarMonkeyController.setCanvasWidth(canvasWidth);

		svg.setCanvasScaleMode(EScaleMode.FIT);
		//svg.scaleTo(canvasWidth, canvasHeight);

		surfaceHolder.unlockCanvasAndPost(canvas);
		
		// Rotate SVG
		Vector2 v = new Vector2(183f, 185f);//.applyCTM(svg.getCTM());
		Matrix animation = new SVGTransformRotate(v.getX(),v.getY(),0.01f).getResultingMatrix();

		// Rotate arms und eyes
		SVGGElement gArmLeft, gArmRight, gEyeLeft, gEyeRight;
		
		gArmLeft = (SVGGElement) svg.getElementById("gArmLeft");
		gArmLeft.animate( new SVGTransformRotate(279f,370-211f,-0.01f) );

		gArmRight = (SVGGElement) svg.getElementById("gArmRight");
		gArmRight.animate( new SVGTransformRotate(128f,370-205f,0.01f) );

		gEyeLeft = (SVGGElement) svg.getElementById("gEyeLeft");
		gEyeLeft.animate( new SVGTransformRotate(231f,370-282f,0.05f) );

		gEyeRight = (SVGGElement) svg.getElementById("gEyeRight");
		gEyeRight.animate( new SVGTransformRotate(179f,370-283f,-0.05f) );
		
		while (running)
		{
			if (surfaceHolder.getSurface().isValid())
			{
				// Lock canvas
				canvas = surfaceHolder.lockCanvas();

				/**
				 * Clear canvas
				 */
				canvas.drawRGB(255, 255, 255);

				//svg.setCTM(svg.getCTM().multiply(animation));
				
				gArmLeft.animateAgain();
				gArmRight.animateAgain();
				gEyeLeft.animateAgain();
				gEyeRight.animateAgain();
				
				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}

		}

	}
}