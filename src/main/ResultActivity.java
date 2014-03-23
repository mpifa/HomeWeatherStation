package main;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.homeweatherstation.R;

public class ResultActivity extends Activity implements SensorEventListener {

	private SensorManager sManager;
	private Sensor sensor;
	private Item item;
	private TextView result;
	private Handler handler;
	private Object[] buffer;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private ConnectivityManager connManager;
	private NetworkInfo netInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		//ONLY 1 minute of samples, each sample every second
		this.buffer = new Object[60];
		this.item = getIntent().getParcelableExtra("Sensor");
		this.result = (TextView) findViewById(R.id.result);
		//SENSORS
		this.sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.sensor = sManager.getDefaultSensor(item.getSensorType());
		//INTERNET
		this.connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		this.netInfo = connManager.getActiveNetworkInfo();
		//GET LOCATION
		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			
			@Override
			public void onProviderEnabled(String provider) {				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			}
			
			@Override
			public void onLocationChanged(Location location) {
			}
		};
		this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		//HANDLER TO GET DATA RUNNING IN BACKGROUND
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					//TODO SENDING DATA TO SERVER
					break;
				}
				super.handleMessage(msg);
			}
		};
		/* USE IT TO LOAD SOME UI GRAPHICS
		if (sensor != null && item.getName().equals("Height")) {
			// Sensor 6
		} else if (sensor != null && item.getName().equals("Barometer")) {
			// Sensor 6
		} else if (sensor != null && item.getName().equals("Humidity")) {
			// Sensor 12
		} else if (sensor != null && item.getName().equals("Temperature")) {
			// Sensor 13
		}*/

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;

		if (event.sensor.getType() == 6 && item.getName().equals("Height")) {
			heightManager(values);
		} else if (event.sensor.getType() == 6 && item.getName().equals("Barometer")) {
			barometerManager(values);
		} else if (event.sensor.getType() == 12) {
			humidityManager(values);
		} else if (event.sensor.getType() == 13) {
			temperatureManager(values);
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

	private void barometerManager(float[] values) {
		result.setText(Float.toString(values[0]));
	}

	private void heightManager(float[] values) {
		result.setText(Float.toString(values[1]));
	}

	private void temperatureManager(float[] values) {
		result.setText(Float.toString(values[0]));
	}

	private void humidityManager(float[] values) {
		result.setText(Float.toString(values[0]));
	}
	
	public boolean isOnline() {
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
