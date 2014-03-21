package main;

import java.io.IOException;

import com.example.homeweatherstation.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class ResultActivity extends Activity implements SensorEventListener {

	private SensorManager sManager;
	private Sensor sensor;
	private Item item;
	private TextView result;
	private SoundMeter sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		this.item = getIntent().getParcelableExtra("Sensor");
		this.result = (TextView) findViewById(R.id.ResultView);
		this.sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.sensor = sManager.getDefaultSensor(item.getSensorType());

		if (sensor != null && item.getName().equals("Altitud")) {
			// Sensor 6
			result.setText(sensor.toString());
		} else if (sensor != null && item.getName().equals("Barometro")) {
			// Sensor 6
			result.setText(sensor.toString());
		} else if (sensor != null && item.getName().equals("Humedad")) {
			// Sensor 12
			result.setText(sensor.toString());
		} else if (sensor != null && item.getName().equals("Temperatura")) {
			// Sensor 13
			result.setText(sensor.toString());
		} else if (item.getSensorType() == 999) {
			sound = new SoundMeter();
			try {
				sound.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			result.setText(Double.toString(sound.getAmplitude()));
		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;

		if (event.sensor.getType() == 6 && item.getName().equals("Altitud")) {
			result.setText(Float.toString(values[1]));
		} else if (event.sensor.getType() == 6 && item.getName().equals("Barometro")) {
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
