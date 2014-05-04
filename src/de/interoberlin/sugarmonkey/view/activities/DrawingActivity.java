package de.interoberlin.sugarmonkey.view.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;
import de.interoberlin.sugarmonkey.view.panels.APanel;
import de.interoberlin.sugarmonkey.view.panels.MonkeyPanel;
import de.interoberlin.sugarmonkey.view.panels.PathsPanel;
import de.interoberlin.sugarmonkey.view.panels.TestPanel;

public class DrawingActivity extends Activity
{
	private static Context	context;
	private static Activity	activity;
	// private static SugarMonkeyController controller;

	private WindowManager	windowManager;
	private static Display	display;

	private static APanel	panel;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Get activity and context
		activity = this;
		context = getApplicationContext();

		// Get an instance of the WindowManager
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
			case PATHS:
			{
				panel = new PathsPanel(activity);
				break;
			}

		}

		// Add surface view
		activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Get controller
		// controller = (SugarMonkeyController) getApplicationContext();
	}

	public void onResume()
	{
		super.onResume();
		panel.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		panel.onPause();
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