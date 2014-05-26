package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.controller.renderer.SvgRenderer;
import de.interoberlin.sauvignon.model.svg.EScaleMode;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.elements.circle.SVGCircle;
import de.interoberlin.sauvignon.model.svg.elements.rect.SVGRect;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class TestPanel extends APanel
{
	Thread					thread	= null;
	SurfaceHolder			surfaceHolder;
	private static boolean	running	= false;

	private static Context	c;

	// private static Resources r;

	public TestPanel(Context context)
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

				// Scale
				svg.setCanvasScaleMode(EScaleMode.FIT);
				svg.scale(canvasWidth, canvasHeight);

				// Load elements
				SVGRect rectRed1 = (SVGRect) svg.getElementById("rectRed1");
				SVGRect rectRed2 = (SVGRect) svg.getElementById("rectRed2");
				SVGRect rectRed3 = (SVGRect) svg.getElementById("rectRed3");

				SVGRect rectGreen1 = (SVGRect) svg.getElementById("rectGreen1");
				SVGRect rectGreen2 = (SVGRect) svg.getElementById("rectGreen2");
				SVGRect rectGreen3 = (SVGRect) svg.getElementById("rectGreen3");

				SVGRect rectBlue1 = (SVGRect) svg.getElementById("rectBlue1");
				SVGRect rectBlue2 = (SVGRect) svg.getElementById("rectBlue2");
				SVGRect rectBlue3 = (SVGRect) svg.getElementById("rectBlue3");

				SVGCircle circle = (SVGCircle) svg.getElementById("circle");

				// Manipulate elements
				rectRed1.setFillR(rectRed1.getFillR() - 1);
				rectRed2.setFillG(rectRed2.getFillG() + 1);
				rectRed3.setFillB(rectRed3.getFillB() + 1);

				rectGreen1.setFillR(rectGreen1.getFillR() + 1);
				rectGreen2.setFillG(rectGreen2.getFillG() - 1);
				rectGreen3.setFillB(rectGreen3.getFillB() + 1);

				rectBlue1.setFillR(rectBlue1.getFillR() + 1);
				rectBlue2.setFillG(rectBlue2.getFillG() + 1);
				rectBlue3.setFillB(rectBlue3.getFillB() - 1);

				if (circle.getR() > 0)
				{
					circle.setR(circle.getR() - 1);
				} else
				{
					circle.setR(360);
				}

				circle.setFillA(circle.getFillA() - 1);

				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}

		}

	}
}