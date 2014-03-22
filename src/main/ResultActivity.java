package main;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.homeweatherstation.R;

public class ResultActivity extends Activity implements SensorEventListener {

	private SensorManager sManager;
	private Sensor sensor;
	private Item item;
	private TextView result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		this.item = getIntent().getParcelableExtra("Sensor");
		this.result = (TextView) findViewById(R.id.result);
		this.sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.sensor = sManager.getDefaultSensor(item.getSensorType());

		if (sensor != null && item.getName().equals("Height")) {
			// Sensor 6
			result.setText(sensor.toString());
		} else if (sensor != null && item.getName().equals("Barometer")) {
			// Sensor 6
			result.setText(sensor.toString());
		} else if (sensor != null && item.getName().equals("Humidity")) {
			// Sensor 12
			result.setText(sensor.toString());
		} else if (sensor != null && item.getName().equals("Temperature")) {
			// Sensor 13
			result.setText(sensor.toString());
		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;

		if (event.sensor.getType() == 6 && item.getName().equals("Height")) {
			result.setText(Float.toString(values[1]));
		} else if (event.sensor.getType() == 6 && item.getName().equals("Barometer")) {
			result.setText(Float.toString(values[0]));
		} else if (event.sensor.getType() == 12) {
			result.setText(Float.toString(values[0]));
		} else if (event.sensor.getType() == 13) {
			result.setText(Float.toString(values[0]));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		sManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sManager.unregisterListener(this);
	}

}
