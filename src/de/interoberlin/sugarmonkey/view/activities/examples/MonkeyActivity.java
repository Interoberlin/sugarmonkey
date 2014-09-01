package de.interoberlin.sugarmonkey.view.activities.examples;

import java.util.ArrayList;
import java.util.List;

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
import de.interoberlin.sauvignon.model.smil.AAnimate;
import de.interoberlin.sauvignon.model.smil.AnimateTransform;
import de.interoberlin.sauvignon.model.smil.EAnimateTransformType;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.elements.SVGGElement;
import de.interoberlin.sauvignon.model.util.SVGPaint;
import de.interoberlin.sauvignon.model.util.Vector2;
import de.interoberlin.sauvignon.view.DebugLine;
import de.interoberlin.sauvignon.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class MonkeyActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;
	// private static SugarMonkeyController controller;

	private static SensorManager	sensorManager;
	private WindowManager			windowManager;
	private static Display			display;

	private static SVG				svg;
	private static SVGPanel			panel;

	private static LinearLayout		lnr;
	private static DebugLine		dlFps;

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

		svg = SvgLoader.getSVGFromAsset(context, "monkey.svg");

		panel = new SVGPanel(activity);
		panel.setSVG(svg);
		panel.setBackgroundColor(new SVGPaint(255, 200, 200, 200));
		panel.setBoundingRectsParallelToAxes(true);

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

		// Initialize
		uiInit();
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

	public static void draw()
	{
		if (lnr != null)
		{
			lnr.removeAllViews();

			// Add debug lines
			dlFps = new DebugLine(activity, "FPS", String.valueOf(SugarMonkeyController.getFps()), String.valueOf(SugarMonkeyController.getCurrentFps()));

			lnr.setOrientation(1);
			lnr.addView(dlFps, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	public static void uiInit()
	{
		synchronized (svg)
		{
			// Find elements by id
			SVGGElement gArmLeft = (SVGGElement) svg.getElementById("gArmLeft");
			SVGGElement gArmRight = (SVGGElement) svg.getElementById("gArmRight");
			SVGGElement gEyeLeft = (SVGGElement) svg.getElementById("gEyeLeft");
			SVGGElement gEyeRight = (SVGGElement) svg.getElementById("gEyeRight");
			SVGGElement gFootLeft = (SVGGElement) svg.getElementById("gFootLeft");
			SVGGElement gFootRight = (SVGGElement) svg.getElementById("gFootRight");

			// Add animations - armLeft
			AnimateTransform aArmLeft = new AnimateTransform();
			aArmLeft.setBegin("0");
			aArmLeft.setFrom("360 275 177");
			aArmLeft.setTo("0 275 177");
			aArmLeft.setDur("5");
			aArmLeft.setRepeatCount("1000");
			aArmLeft.setType(EAnimateTransformType.ROTATE);

			List<AAnimate> animationsArmLeft = new ArrayList<AAnimate>();
			animationsArmLeft.add(aArmLeft);
			gArmLeft.setAnimations(animationsArmLeft);

			// Add animations - armRight
			AnimateTransform aArmRight = new AnimateTransform();
			aArmRight.setBegin("0");
			aArmRight.setFrom("0 135 177");
			aArmRight.setTo("360 135 177");
			aArmRight.setDur("5");
			aArmRight.setRepeatCount("1000");
			aArmRight.setType(EAnimateTransformType.ROTATE);

			List<AAnimate> animationsArmRight = new ArrayList<AAnimate>();
			animationsArmRight.add(aArmRight);
			gArmRight.setAnimations(animationsArmRight);

			// Add animations - footLeft
			AnimateTransform aFootLeft = new AnimateTransform();
			aFootLeft.setBegin("0");
			aFootLeft.setFrom("0 150 260");
			aFootLeft.setTo("360 150 260");
			aFootLeft.setDur("5");
			aFootLeft.setRepeatCount("1000");
			aFootLeft.setType(EAnimateTransformType.ROTATE);

			List<AAnimate> animationsFootLeft = new ArrayList<AAnimate>();
			animationsFootLeft.add(aFootLeft);
			gFootLeft.setAnimations(animationsFootLeft);

			// Add animations - footRight
			AnimateTransform aFootRight = new AnimateTransform();
			aFootRight.setBegin("0");
			aFootRight.setFrom("360 265 260");
			aFootRight.setTo("0 265 260");
			aFootRight.setDur("5");
			aFootRight.setRepeatCount("1000");
			aFootRight.setType(EAnimateTransformType.ROTATE);

			List<AAnimate> animationsFootRight = new ArrayList<AAnimate>();
			animationsFootRight.add(aFootRight);
			gFootRight.setAnimations(animationsFootRight);

			// Add animations - eyeLeft
			AnimateTransform aEyeLeft = new AnimateTransform();
			aEyeLeft.setBegin("0");
			aEyeLeft.setFrom("0 230 85");
			aEyeLeft.setTo("360 230 85");
			aEyeLeft.setDur("5");
			aEyeLeft.setRepeatCount("1000");
			aEyeLeft.setType(EAnimateTransformType.ROTATE);

			List<AAnimate> animationsEyeLeft = new ArrayList<AAnimate>();
			animationsEyeLeft.add(aEyeLeft);
			gEyeLeft.setAnimations(animationsEyeLeft);

			// Add animations - eyeRight
			AnimateTransform aEyeRight = new AnimateTransform();
			aEyeRight.setBegin("0");
			aEyeRight.setFrom("0 181 85");
			aEyeRight.setTo("360 181 85");
			aEyeRight.setDur("5");
			aEyeRight.setRepeatCount("1000");
			aEyeRight.setType(EAnimateTransformType.ROTATE);

			List<AAnimate> animationsEyeRight = new ArrayList<AAnimate>();
			animationsEyeRight.add(aEyeRight);
			gEyeRight.setAnimations(animationsEyeRight);
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