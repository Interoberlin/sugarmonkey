package de.interoberlin.sugarmonkey.controller.accelerometer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;

import java.util.Observable;

import de.interoberlin.sugarmonkey.controller.Simulation;
import de.interoberlin.sugarmonkey.view.activities.examples.LymboActivity;

public class Accelerometer extends Observable implements SensorEventListener
{
	private Activity				activity;

	private Sensor					accelerometer;
	private float					sensorX;
	private float					sensorY;

	private static Accelerometer	instance;

	private Accelerometer(Activity activity)
	{
		accelerometer = ((LymboActivity) activity).getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.activity = activity;

		addObserver(Simulation.getInstance(activity));
	}

	public static Accelerometer getInstance(Activity activity)
	{
		if (instance == null)
		{
			instance = new Accelerometer(activity);
		}

		return instance;
	}

	public void start()
	{
		LymboActivity.uiToast("Accelerometer started");
		((LymboActivity) activity).getSensorManager().registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	public void stop()
	{
		LymboActivity.uiToast("Accelerometer stopped");
		((LymboActivity) activity).getSensorManager().unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
		{
			return;
		}

		switch (((LymboActivity) activity).getDisplay().getRotation())
		{
			case Surface.ROTATION_0:
			{
				sensorX = event.values[0];
				sensorY = -event.values[1];
				break;
			}
			case Surface.ROTATION_90:
			{
				sensorX = -event.values[1];
				sensorY = -event.values[0];
				break;
			}
			case Surface.ROTATION_180:
			{
				sensorX = -event.values[0];
				sensorY = event.values[1];
				break;
			}
			case Surface.ROTATION_270:
			{
				sensorX = event.values[1];
				sensorY = event.values[0];
				break;
			}
		}

		setChanged();

		notifyObservers(new AccelerationEvent(sensorX, sensorY));
		LymboActivity.uiUpdate();
		LymboActivity.uiDraw();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
	}
}