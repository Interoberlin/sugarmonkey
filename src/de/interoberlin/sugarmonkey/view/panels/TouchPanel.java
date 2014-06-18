package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.elements.circle.SVGCircle;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class TouchPanel extends APanel
{
	Thread					thread	= null;
	SurfaceHolder			surfaceHolder;
	private static boolean	running	= false;

	private static Context	c;

	// private static Resources r;

	public TouchPanel(Context context)
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
		SVG svg = SvgLoader.getSVGFromAsset(c, "dot.svg");

		// Load elements
		SVGCircle cBlue = (SVGCircle) svg.getElementById("dot");

		// Clone elements
//		SVGCircle cRed = null;
//		cRed = new SVGCircle(cBlue, "newCircle");
//		svg.addSubelement(cRed);

//		Paint red = new Paint();
//		red.setARGB(255, 255, 0, 0);
//		cRed.setFill(red);

		Paint blue = new Paint();
		blue.setARGB(255, 0, 0, 255);
		cBlue.getStyle().setFill(blue);

		// Add other elements
//		SVGPath pBlue = new SVGPath();
//		pBlue.setFill(blue);
//		pBlue.setStroke(blue);
//		pBlue.addAbsoluteMoveTo(new Vector2(cBlue.getCx(), cBlue.getCy()));
//		svg.addSubelement(pBlue);

//		SVGPath pRed = new SVGPath();
//		pRed.setFill(red);
//		pRed.setStroke(red);
//		pRed.addAbsoluteMoveTo(new Vector2(cRed.getCx(), cRed.getCy()));
//		svg.addSubelement(pRed);

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
				svg.scaleTo(canvasWidth, canvasHeight);

				// Manipulate elements
				if (super.getTouch() != null)
				{
					cBlue.setCx(super.getTouch().getX());
					cBlue.setCy(super.getTouch().getY());
				}

//				cRed.setCx(SugarMonkeyController.getAccelerometerX() / 10 * canvasWidth);
//				cRed.setCy(SugarMonkeyController.getAccelerometerY() / 10 * canvasHeight);
//
//				pBlue.addAbsoluteLineTo(new Vector2(cBlue.getCx(), cBlue.getCy()));
//				pRed.addAbsoluteLineTo(new Vector2(cRed.getCx(), cRed.getCy()));

				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}

	}
}