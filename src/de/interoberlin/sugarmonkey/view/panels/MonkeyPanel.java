package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.attributes.SVGTransform;
import de.interoberlin.sauvignon.model.svg.attributes.SVGTransformRotate;
import de.interoberlin.sauvignon.model.svg.attributes.SVGTransformScale;
import de.interoberlin.sauvignon.model.svg.attributes.SVGTransformTranslate;
import de.interoberlin.sauvignon.model.svg.elements.SVGGElement;
import de.interoberlin.sauvignon.model.util.Matrix;
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

				((SVGGElement) svg.getElementById("gArmLeft"))
					.animate( new SVGTransformRotate(2f,2f,-0.1f) );

				((SVGGElement) svg.getElementById("gArmRight"))
					.animate( new SVGTransformRotate(2f,2f,0.1f) );

				((SVGGElement) svg.getElementById("gBody"))
					.animate( new SVGTransformScale(0.995f) );
				
				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}

		}

	}
}