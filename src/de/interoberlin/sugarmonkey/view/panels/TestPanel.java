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
		SVG svg = SvgLoader.getSVGFromAsset(c, "rectangle.svg");

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
				rectRed1.getStyle().setFillR(rectRed1.getStyle().getFillR() - 1);
				rectRed2.getStyle().setFillG(rectRed2.getStyle().getFillG() + 1);
				rectRed3.getStyle().setFillB(rectRed3.getStyle().getFillB() + 1);

				rectGreen1.getStyle().setFillR(rectGreen1.getStyle().getFillR() + 1);
				rectGreen2.getStyle().setFillG(rectGreen2.getStyle().getFillG() - 1);
				rectGreen3.getStyle().setFillB(rectGreen3.getStyle().getFillB() + 1);

				rectBlue1.getStyle().setFillR(rectBlue1.getStyle().getFillR() + 1);
				rectBlue2.getStyle().setFillG(rectBlue2.getStyle().getFillG() + 1);
				rectBlue3.getStyle().setFillB(rectBlue3.getStyle().getFillB() - 1);

				if (circle.getRadius() > 0)
				{
					circle.setRadius(circle.getRadius() - 1);
				} else
				{
					circle.setRadius(360);
				}

				circle.getStyle().setFillA(circle.getStyle().getFillA() - 1);

				// Render SVG
				canvas = SvgRenderer.renderToCanvas(canvas, svg);

				surfaceHolder.unlockCanvasAndPost(canvas);
			}

		}

	}
}