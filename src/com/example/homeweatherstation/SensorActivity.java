package com.example.homeweatherstation;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SensorActivity extends Activity {
	
	ListView lstv;
	SensorManager sManager;
	List<Sensor> listSensor;
	List<String> sensorNames;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		this.lstv = (ListView) findViewById(R.id.Sensorlist);
		this.sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.listSensor = sManager.getSensorList(Sensor.TYPE_ALL);
		sensorNames = new ArrayList<String>();
		for(Sensor sensor: listSensor){
			sensorNames.add(sensor.getName());
		}
		
		this.lstv.setAdapter(new ArrayAdapter<String>(
				getApplicationContext(), 
				android.R.layout.simple_list_item_1,
				sensorNames
				));
	}

	

}
