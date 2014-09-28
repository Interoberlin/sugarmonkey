package de.interoberlin.sugarmonkey.view.activities.examples;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import de.interoberlin.sauvignon.lib.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.lib.model.svg.SVG;
import de.interoberlin.sauvignon.lib.model.svg.elements.AGeometric;
import de.interoberlin.sauvignon.lib.model.svg.elements.path.SVGPath;
import de.interoberlin.sauvignon.lib.model.svg.elements.rect.SVGRect;
import de.interoberlin.sauvignon.lib.model.svg.transform.transform.SVGTransformTranslate;
import de.interoberlin.sauvignon.lib.model.util.SVGPaint;
import de.interoberlin.sauvignon.lib.model.util.Vector2;
import de.interoberlin.sauvignon.lib.view.DebugLine;
import de.interoberlin.sauvignon.lib.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.Simulation;

public class InteroberlinActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;

	private static SensorManager	sensorManager;
	private WindowManager			windowManager;
	private static Display			display;

	private static SVG svg;
	private static SVGPanel			panel;
	private static ImageView		ivLogo;

	private static DebugLine dlFps;
	private static DebugLine		dlData;
	private static DebugLine		dlRaw;

    private static float displacementLast;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Get activity and context
		activity = this;
		context = getApplicationContext();

		// Get instances of managers
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();

		svg = SvgLoader.getSVGFromAsset(context, "interoberlin.svg");

		panel = new SVGPanel(activity);
		panel.setSVG(svg);
		panel.setBackgroundColor(new SVGPaint(255, 255, 255, 255));

		// Add surface view
		activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// activity.addContentView(ivLogo, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Initialize
		uiInit();
	}

	public void onResume()
	{
		super.onResume();
		panel.resume();

		Simulation.getInstance(activity).start();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		panel.pause();

		Simulation.getInstance(activity).stop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

	}

	public Display getDisplay()
	{
		return display;
	}

	public SensorManager getSensorManager()
	{
		return sensorManager;
	}

	public static void uiInit()
	{
		synchronized (svg)
		{
			for (AGeometric e : svg.getAllSubElements())
			{
				if (e instanceof SVGRect && !e.getId().matches("background"))
				{
					SVGPaint p = e.getStyle().getFill();

					e.getStyle().getFill().setS(p.getS() - (svg.getMaxZindex() - e.getzIndex()) * 2);
				}
			}
		}
	}

	public static void uiUpdate()
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				synchronized (svg)
				{
                    float displacementCurrent = (float )Math.sqrt(Math.pow(Simulation.getRawX(),2) + Math.pow(Simulation.getRawY(),2));

					for (AGeometric e : svg.getAllSubElements())
					{
                        if (e instanceof SVGPath)
                        {
                            Vector2 screenCenter = new Vector2(svg.getWidth()/2, svg.getHeight()/2);
                            Vector2 elementCenter = e.getBoundingRect().getCenter();

                            Vector2 direction = elementCenter.substract(screenCenter);
                            Vector2 directionUnit = direction.normalize();

                            float shake = Math.abs(displacementLast-displacementCurrent);

                            directionUnit = directionUnit.scale((float) new Random().nextInt(4) *5);
                            directionUnit = directionUnit.scale(shake*10);

                            e.setAnimationTransform(new SVGTransformTranslate(directionUnit.getX(), directionUnit.getY()));
                        }
                    }

                    displacementLast = (float )Math.sqrt(Math.pow(Simulation.getRawX(),2) + Math.pow(Simulation.getRawY(),2));
				}
			}
		});

		t.start();
	}

	public static void uiDraw()
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{

			}
		});
	}

	public static void uiToast(final String message)
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}