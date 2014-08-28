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
import android.widget.TextView;
import android.widget.Toast;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.util.Vector2;
import de.interoberlin.sauvignon.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.Simulation;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class SugarMonkeyActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;
	// private static SugarMonkeyController controller;

	private static SensorManager	sensorManager;
	private WindowManager			windowManager;
	private static Display			display;

	private static SVGPanel			panel;

	private static LinearLayout		lnr;

	private static LinearLayout		zeroLnr;
	private static TextView			zeroTvLabel;
	private static TextView			zeroTvValue;

	private static LinearLayout		oneLnr;
	private static TextView			oneTvFirst;
	private static TextView			oneTvSecond;
	private static TextView			oneTvThird;

	private static LinearLayout		threeLnr;
	private static TextView			threeTvFirst;
	private static TextView			threeTvSecond;
	private static TextView			threeTvThird;

	private static LinearLayout		fourLnr;
	private static TextView			fourTvFirst;
	private static TextView			fourTvSecond;
	private static TextView			fourTvThird;

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

		SVG svg = SvgLoader.getSVGFromAsset(context, "yay.svg");

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

				// Vibrate
				// ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);

				// Inform panel
				panel.setTouch(new Vector2(x, y));

				return true;
			}
		});

		// Get controller
		// controller = (SugarMonkeyController) getApplicationContext();
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

			// Add text views
			lnr = new LinearLayout(activity);
			zeroTvLabel = new TextView(activity);
			zeroTvValue = new TextView(activity);
			zeroTvLabel.setText(R.string.fps);
			zeroTvValue.setText(String.valueOf(SugarMonkeyController.getCurrentFps()));
			lnr.addView(zeroTvLabel, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			lnr.addView(zeroTvValue, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			// Add text views
			oneLnr = new LinearLayout(activity);
			oneTvFirst = new TextView(activity);
			oneTvSecond = new TextView(activity);
			oneTvThird = new TextView(activity);
			oneTvFirst.setText("Data");
			oneTvSecond.setText(String.valueOf(Simulation.getDataX()));
			oneTvThird.setText(String.valueOf(Simulation.getDataY()));
			oneLnr.addView(oneTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			oneLnr.addView(oneTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			oneLnr.addView(oneTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			// Add text views
			threeLnr = new LinearLayout(activity);
			threeTvFirst = new TextView(activity);
			threeTvSecond = new TextView(activity);
			threeTvThird = new TextView(activity);
			threeTvFirst.setText("Raw");
			threeTvSecond.setText(String.valueOf(Simulation.getRawX()));
			threeTvThird.setText(String.valueOf(Simulation.getRawY()));
			threeLnr.addView(threeTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			threeLnr.addView(threeTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			threeLnr.addView(threeTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			// Add text views
			fourLnr = new LinearLayout(activity);
			fourTvFirst = new TextView(activity);
			fourTvSecond = new TextView(activity);
			fourTvThird = new TextView(activity);
			fourTvFirst.setText("Values");
			fourTvSecond.setText(String.valueOf(Simulation.getX()));
			fourTvThird.setText(String.valueOf(Simulation.getY()));
			fourLnr.addView(fourTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			fourLnr.addView(fourTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			fourLnr.addView(fourTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			lnr.setOrientation(1);
			lnr.addView(zeroLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(oneLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(threeLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(fourLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			activity.addContentView(lnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
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