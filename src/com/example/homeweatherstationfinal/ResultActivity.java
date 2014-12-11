package com.example.homeweatherstationfinal;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.homeweatherstationfinal.dataendpoint.Dataendpoint;
import com.example.homeweatherstationfinal.dataendpoint.model.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;


public class ResultActivity extends Activity implements SensorEventListener {

	private SensorManager sManager;
	private Sensor sensor;
	private Item item;
	private TextView result;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private ConnectivityManager connManager;
	private NetworkInfo netInfo;
	public Dataendpoint endpoint;
	private List<String> resultsList;
	private Data data;
	private String coords = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		resultsList = new ArrayList<String>();
		data = new Data();
		//ONLY 1 minute of samples, each sample every second
		this.item = getIntent().getParcelableExtra("Sensor");
		//DATA
		data.setSensorType(item.getName());
		this.result = (TextView) findViewById(R.id.result);
		//SENSORS
		this.sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.sensor = sManager.getDefaultSensor(item.getSensorType());
		//INTERNET
		this.connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		this.netInfo = connManager.getActiveNetworkInfo();
		//GET LOCATION
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
						coords=location.getLatitude()+","+location.getLongitude();
					}
				};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		/* USE IT TO LOAD SOME UI GRAPHICS*/
		if (sensor != null && item.getName().equals("Height")) {
			// Sensor 6
			data.setId("6");
		} else if (sensor != null && item.getName().equals("Barometer")) {
			// Sensor 6
			data.setId("7");
		} else if (sensor != null && item.getName().equals("Humidity")) {
			// Sensor 12
			data.setId("12");
		} else if (sensor != null && item.getName().equals("Temperature")) {
			// Sensor 13
			data.setId("13");
		} else if(item.getName().equals("Noise")){
			data.setId("0");
		}

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
		resultsList.add(Float.toString(values[0]));
	}

	private void heightManager(float[] values) {
		result.setText(Float.toString(values[1]));
		resultsList.add(Float.toString(values[1]));
	}

	private void temperatureManager(float[] values) {
		result.setText(Float.toString(values[0]));
		resultsList.add(Float.toString(values[0]));
	}

	private void humidityManager(float[] values) {
		result.setText(Float.toString(values[0]));
		resultsList.add(Float.toString(values[0]));
	}
	
	public boolean isOnline() {
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	private String getAverage(){
		double num = 0;
		for(String res: resultsList){
			num+= Double.parseDouble(res);
		}
		return Double.toString(num/resultsList.size());
		
	}
	public class DataCollectTask extends AsyncTask<Data, Void, String>{

		@Override
		protected String doInBackground(Data... params) {
			
			Dataendpoint.Builder builder = new Dataendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
			
			builder = CloudEndpointUtils.updateBuilder(builder);
			
			endpoint = builder.build();
			
			try {
				endpoint.insertData(params[0]).execute();
				return "OK";
			} catch (IOException e) {
				e.printStackTrace();
				return "ERROR";
			}
		}

	}
	
	@Override
	public void onDestroy() {
		
		data.setValues(getAverage());
		data.setCoordinates(coords);
		new AndroidToNodeJS(data);
		new DataCollectTask().execute(data);
		super.onDestroy();
	}
	
}
