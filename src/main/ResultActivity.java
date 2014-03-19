package main;

import com.example.homeweatherstation.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;

public class ResultActivity extends Activity implements SensorEventListener {

	SensorManager sManager;
	Sensor sensor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Item item = getIntent().getParcelableExtra("Sensor");

		this.sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.sensor = sManager.getDefaultSensor(item.getSensorType());

		if (sensor != null && item.getName().equals("Altitud")) {
			//Sensor 6
		} else if (sensor != null && item.getName().equals("Barometro")) {
			//Sensor 6
		} else if (sensor != null && item.getName().equals("Humedad")) {
			System.out.println(sensor.getType());
		} else if (sensor != null && item.getName().equals("Temperatura")) {
			System.out.println(sensor.getType());
		} else if (item.getSensorType() == 999) {
		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		/*if (event.sensor.getType()) {

		} else if (event.sensor.getType()) {

		} else if (event.sensor.getType()) {
			
		} else if (event.sensor.getType()) {

		} else if (item.getSensorType() == 999) {

		}*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		sManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sManager.unregisterListener(this);
	}

}
