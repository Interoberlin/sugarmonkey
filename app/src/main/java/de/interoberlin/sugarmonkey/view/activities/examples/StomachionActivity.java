package de.interoberlin.sugarmonkey.view.activities.examples;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import de.interoberlin.mate.lib.model.Log;
import de.interoberlin.sauvignon.lib.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.lib.model.svg.SVG;
import de.interoberlin.sauvignon.lib.model.svg.elements.AGeometric;
import de.interoberlin.sauvignon.lib.model.svg.elements.polygon.SVGPolygon;
import de.interoberlin.sauvignon.lib.model.svg.elements.rect.SVGRect;
import de.interoberlin.sauvignon.lib.model.svg.transform.transform.SVGTransformTranslate;
import de.interoberlin.sauvignon.lib.model.util.SVGPaint;
import de.interoberlin.sauvignon.lib.model.util.Vector2;
import de.interoberlin.sauvignon.lib.view.DebugLine;
import de.interoberlin.sauvignon.lib.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.Simulation;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

public class StomachionActivity extends Activity
{
    private static Context			context;
    private static Activity			activity;

    private static SensorManager	sensorManager;
    private WindowManager			windowManager;
    private static Display			display;

    private static SVG svg;
    private static SVGPanel			panel;
    private static ImageView		ivLogo;

    private static LinearLayout		lnr;
    private static DebugLine dlFps;
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

        svg = SvgLoader.getSVGFromAsset(context, "stomachion.svg");

        panel = new SVGPanel(activity);
        panel.setSVG(svg);
        panel.setBackgroundColor(new SVGPaint(255, 255, 255, 255));

        // Add surface view
        activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        panel.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                float x = event.getX();
                float y = event.getY();

                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);

                SugarMonkeyController.setOffsetX(Simulation.getDataX());
                SugarMonkeyController.setOffsetY(Simulation.getDataY());

                // Inform panel
                panel.setTouch(new Vector2(x, y));

                return true;
            }
        });

        // Add linear layout
        lnr = new LinearLayout(activity);
        activity.addContentView(lnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // Initialize
        uiInit();
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

    public static void draw()
    {
        if (lnr != null)
        {
            lnr.removeAllViews();

            // Add debug lines
            dlFps = new DebugLine(activity, "FPS", String.valueOf(SugarMonkeyController.getFps()), String.valueOf(SugarMonkeyController.getCurrentFps()));
            dlData = new DebugLine(activity, "Data", String.valueOf(Simulation.getDataX()), String.valueOf(Simulation.getDataY()));
            dlRaw = new DebugLine(activity, "Raw", String.valueOf(Simulation.getRawX()), String.valueOf(Simulation.getRawY()));

            lnr.setOrientation(LinearLayout.VERTICAL);
            lnr.addView(dlFps, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            lnr.addView(dlData, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            lnr.addView(dlRaw, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private Drawable loadFromAssets(String image)
    {
        try
        {
            InputStream is = getAssets().open(image);
            return Drawable.createFromStream(is, null);
        } catch (IOException ex)
        {
            return null;
        }
    }

    public static void uiInit()
    {
		synchronized (svg)
		{
            float origWidth = svg.getWidth();
            float origHeight = svg.getHeight();

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
                    for (AGeometric e : svg.getAllSubElements()) {
                        if (e instanceof SVGPolygon) {
                            float x = Simulation.getRawX() * (e.getzIndex() - svg.getMaxZindex() / 2) * -5;
                            float y = Simulation.getRawY() * (e.getzIndex() - svg.getMaxZindex() / 2) * -5;

                            Log.debug(new Integer(svg.getMaxZindex()).toString());

                            e.getAnimationSets().clear();
                            e.setAnimationTransform(new SVGTransformTranslate(x, y));
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