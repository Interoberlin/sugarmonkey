package de.interoberlin.sugarmonkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;
import de.interoberlin.sugarmonkey.view.activities.AnimateTransformActivity;
import de.interoberlin.sugarmonkey.view.activities.DrawingActivity;
import de.interoberlin.sugarmonkey.view.activities.NewActivity;
import de.interoberlin.sugarmonkey.view.panels.EPanel;

public class SplashActivity extends Activity
{
	private static TableLayout	tbl;

	private static Context		context;
	private static Activity		activity;

	// private static SugarMonkeyController controller;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Get views by id
		tbl = (TableLayout) findViewById(R.id.tableLayout);

		// Get activity and context
		activity = this;
		context = getApplicationContext();

		// Get controller
		// controller = (SugarMonkeyController) getApplicationContext();
	}

	public void onResume()
	{
		super.onResume();

		tbl.removeAllViews();

		// Iterate over all enum values
		for (EPanel p : EPanel.values())
		{
			final EPanel panel = p;

			TableRow tr = new TableRow(context);
			TextView tv = new TextView(context);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
			tv.setText(p.toString());
			tr.addView(tv);
			tr.setOnClickListener(new OnClickListener()
			{ 
				@Override
				public void onClick(View v)
				{
					SugarMonkeyController.setCurrentPanel(panel);

					Intent openStartingPoint = new Intent(SplashActivity.this, DrawingActivity.class);
					startActivity(openStartingPoint);
				}
			});

			tbl.addView(tr);
		}
		
//		View v = new View(this);
//		v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
//		v.setBackgroundColor(Color.rgb(51, 51, 51));
		
		TableRow tr = new TableRow(context);
		TextView tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
		tv.setText("NEW");
		tr.addView(tv);
		tr.setOnClickListener(new OnClickListener()
		{ 
			@Override
			public void onClick(View v)
			{
				Intent openStartingPoint = new Intent(SplashActivity.this, NewActivity.class);
				startActivity(openStartingPoint);
			}
		});

		tbl.addView(tr);
		
		TableRow trAT = new TableRow(context);
		TextView tvAT = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
		tv.setText("ANIMATE TRANSFORM");
		tr.addView(tvAT);
		tr.setOnClickListener(new OnClickListener()
		{ 
			@Override
			public void onClick(View v)
			{
				Intent openStartingPoint = new Intent(SplashActivity.this, AnimateTransformActivity.class);
				startActivity(openStartingPoint);
			}
		});

		tbl.addView(trAT);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
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