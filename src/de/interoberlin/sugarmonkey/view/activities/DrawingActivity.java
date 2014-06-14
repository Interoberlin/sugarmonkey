package de.interoberlin.sugarmonkey.view.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.interoberlin.sauvignon.model.util.Vector2;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;
import de.interoberlin.sugarmonkey.controller.accelerometer.AcceleratorListener;
import de.interoberlin.sugarmonkey.view.panels.APanel;
import de.interoberlin.sugarmonkey.view.panels.ArcPanel;
import de.interoberlin.sugarmonkey.view.panels.LymboPanel;
import de.interoberlin.sugarmonkey.view.panels.MonkeyPanel;
import de.interoberlin.sugarmonkey.view.panels.MonkeyTailPanel;
import de.interoberlin.sugarmonkey.view.panels.PathsPanel;
import de.interoberlin.sugarmonkey.view.panels.TestPanel;
import de.interoberlin.sugarmonkey.view.panels.TouchPanel;

public class DrawingActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;
	// private static SugarMonkeyController controller;

	private static SensorManager	sensorManager;
	private WindowManager			windowManager;
	private static Display			display;

	private static APanel			panel;
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

		switch (SugarMonkeyController.getCurrentPanel())
		{
			case TEST:
			{
				panel = new TestPanel(activity);
				break;
			}
			case MONKEY:
			{
				panel = new MonkeyPanel(activity);
				break;
			}
			case MONKEY_TAIL:
			{
				panel = new MonkeyTailPanel(activity);
				break;
			}
			case PATHS:
			{
				panel = new PathsPanel(activity);
				break;
			}
			case LYMBO:
			{
				panel = new LymboPanel(activity);
				break;
			}
			case TOUCH:
			{
				panel = new TouchPanel(activity);
				break;
			}
			case ARC:
			{
				panel = new ArcPanel(activity);
				break;
			}

		}

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
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);

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
		panel.onResume();
		AcceleratorListener.getInstance(activity).start();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		panel.onPause();
		AcceleratorListener.getInstance(activity).stop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		AcceleratorListener.getInstance(activity).stop();
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