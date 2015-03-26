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

public class ExampleActivity extends Activity
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

		tbl.addView(getEntry("de.interoberlin.sugarmonkey.view.activities.examples.LymboActivity"));
		tbl.addView(getEntry("de.interoberlin.sugarmonkey.view.activities.examples.MonkeyActivity"));
        tbl.addView(getEntry("de.interoberlin.sugarmonkey.view.activities.examples.DebugActivity"));
        tbl.addView(getEntry("de.interoberlin.sugarmonkey.view.activities.examples.InteroberlinActivity"));
        tbl.addView(getEntry("de.interoberlin.sugarmonkey.view.activities.examples.LevelActivity"));
        tbl.addView(getEntry("de.interoberlin.sugarmonkey.view.activities.examples.StomachionActivity"));
        tbl.addView(getEntry("de.interoberlin.sugarmonkey.view.activities.examples.Stomachion2Activity"));
	}

	private TableRow getEntry(final String c)
	{
		TableRow tr = new TableRow(context);
		TextView tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
		String label = c.replaceAll("(.*)\\.", "");
		label = label.replaceAll("Activity", "");
		label = label.toUpperCase();
		tv.setText(label);
		tr.addView(tv);
		tr.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					startActivity(new Intent(ExampleActivity.this, Class.forName(c)));
				} catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		});

		return tr;
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