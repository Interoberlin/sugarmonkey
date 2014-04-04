package de.interoberlin.sugarmonkey;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;
import de.interoberlin.sugarmonkey.view.panels.DrawingPanel;

public class SplashActivity extends Activity {
	private static Context context;
	private static Activity activity;
	// private static SVGDemoController controller;

	private WindowManager mWindowManager;
	private static Display mDisplay;

	private static DrawingPanel panel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Get activity and context
		activity = this;
		context = getApplicationContext();

		// Get an instance of the WindowManager
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();

		// Add surface view
		panel = new DrawingPanel(activity);
		activity.addContentView(panel, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Get controller
		// controller = (SVGDemoController) getApplicationContext();
	}

	public void onResume() {
		super.onResume();
		panel.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		panel.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public Display getDisplay() {
		return mDisplay;
	}

	public static void uiToast(final String message) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}