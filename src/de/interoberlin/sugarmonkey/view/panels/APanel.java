package de.interoberlin.sugarmonkey.view.panels;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.sauvignon.model.util.Vector2;

public class APanel extends SurfaceView implements Runnable
{
	private Thread			thread	= null;
	private SurfaceHolder	surfaceHolder;
	private Vector2			touch;
	private static boolean	running	= false;

	// private static Context c;
	// private static Resources r;

	public APanel(Context context)
	{
		super(context);
		surfaceHolder = getHolder();

		// c = (Context) SugarMonkeyController.getContext();
		// r = c.getResources();
	}

	public void onChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}

	public void onResume()
	{
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void onPause()
	{
		boolean retry = true;
		running = false;

		while (retry)
		{
			try
			{
				thread.join();
				retry = false;
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning()
	{
		return running;
	}

	@Override
	public void run()
	{

	}

	public Vector2 getTouch()
	{
		return touch;
	}

	public void setTouch(Vector2 v)
	{
		touch = new Vector2(v);
	}
}