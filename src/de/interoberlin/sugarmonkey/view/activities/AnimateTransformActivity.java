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
import de.interoberlin.sauvignon.model.util.Vector2;
import de.interoberlin.sauvignon.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class AnimateTransformActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;
	// private static SugarMonkeyController controller;

	private static SensorManager	sensorManager;
	private WindowManager			windowManager;
	private static Display			display;

	private static SVGPanel			panel;
	private static LinearLayout		lnr;
	private static TextView			tvLabel;
	private static TextView			tvValue;

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

		panel = new SVGPanel(activity);
		panel.setSVG(SvgLoader.getSVGFromAsset(context, "animateTransform.svg"));

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

		lnr = new LinearLayout(activity);
		tvLabel = new TextView(activity);
		tvValue = new TextView(activity);
		lnr.addView(tvLabel, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
		lnr.addView(tvValue, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
		activity.addContentView(lnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// Get controller
		// controller = (SugarMonkeyController) getApplicationContext();
	}

	public void onResume()
	{
		super.onResume();
		panel.resume();
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
	}

	public Display getDisplay()
	{
		return display;
	}

	public SensorManager getSensorManager()
	{
		return sensorManager;
	}

	public static void uiDraw()
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				tvLabel.setText(R.string.fps);
				tvValue.setText(String.valueOf(SugarMonkeyController.getCurrentFps()));
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