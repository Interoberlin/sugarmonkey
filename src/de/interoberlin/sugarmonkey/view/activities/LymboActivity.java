package de.interoberlin.sugarmonkey.view.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.model.smil.EAttributeName;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.elements.AGeometric;
import de.interoberlin.sauvignon.model.svg.elements.rect.SVGRect;
import de.interoberlin.sauvignon.model.svg.transform.set.SetOperator;
import de.interoberlin.sauvignon.model.util.Vector2;
import de.interoberlin.sauvignon.view.DebugLine;
import de.interoberlin.sauvignon.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.Simulation;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class LymboActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;
	// private static SugarMonkeyController controller;

	private static SensorManager	sensorManager;
	private WindowManager			windowManager;
	private static Display			display;

	private static SVGPanel			panel;
	private static SVG				svg;

	private static LinearLayout		lnr;
	private static DebugLine		dlFps;
	private static DebugLine		dlData;
	private static DebugLine		dlRaw;

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

		svg = SvgLoader.getSVGFromAsset(context, "lymbo.svg");

		panel = new SVGPanel(activity);
		panel.setSVG(svg);

		// Add surface view
		activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		panel.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// Read values
				float x = event.getX();
				float y = event.getY();

				// Inform panel
				panel.setTouch(new Vector2(x, y));

				return true;
			}
		});

		// Add linear layout
		lnr = new LinearLayout(activity);
		activity.addContentView(lnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// Get controller
		// controller = (SugarMonkeyController) getApplicationContext();
	}

	public void onResume()
	{
		super.onResume();
		panel.resume();

		draw();

		Simulation.getInstance(activity).start();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		panel.pause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Simulation.getInstance(activity).stop();
	}

	public Display getDisplay()
	{
		return display;
	}

	public SensorManager getSensorManager()
	{
		return sensorManager;
	}

	public static void draw()
	{
		if (lnr != null)
		{
			lnr.removeAllViews();

			// Add debug lines
			dlFps = new DebugLine(activity, "FPS", String.valueOf(SugarMonkeyController.getFps()), String.valueOf(SugarMonkeyController.getCurrentFps()));
			dlData = new DebugLine(activity, "Data", String.valueOf(Simulation.getDataX()), String.valueOf(Simulation.getDataY()));
			;
			dlRaw = new DebugLine(activity, "Raw", String.valueOf(Simulation.getRawX()), String.valueOf(Simulation.getRawY()));
			;

			lnr.setOrientation(1);
			lnr.addView(dlFps, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(dlData, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(dlRaw, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	public static void uiUpdate()
	{
		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				for (AGeometric e : svg.getAllSubElements())
				{
					synchronized (svg)
					{
						if (e instanceof SVGRect)// && !
													// e.getId().matches(".*parallax.*"))
						{
							float x = ((SVGRect) e).getX() + Simulation.getRawX() * (e.getzIndex() - svg.getMaxZindex() / 2) * -5;
							float y = ((SVGRect) e).getY() + Simulation.getRawY() * (e.getzIndex() - svg.getMaxZindex() / 2) * -5;

							e.getAnimationSets().clear();
							e.addAnimationSet(new SetOperator(EAttributeName.X, x));
							e.addAnimationSet(new SetOperator(EAttributeName.Y, y));
						}
					}
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
				draw();
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