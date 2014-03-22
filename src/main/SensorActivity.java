package main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.homeweatherstation.R;

public class SensorActivity extends Activity {

	private GridView gridV;
	private SensorManager sManager;
	private List<Item> sensorList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		this.sensorList = new ArrayList<Item>();
		this.gridV = (GridView) findViewById(R.id.gridView1);
		this.sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// MANAGE SENSORS
		if (sManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
			this.sensorList.add(new Item("Height", Sensor.TYPE_PRESSURE,
					R.drawable.elevation));
			this.sensorList.add(new Item("Barometer", Sensor.TYPE_PRESSURE,
					R.drawable.barometro));
		}

		if (sManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
			this.sensorList.add(new Item("Humidity",
					Sensor.TYPE_RELATIVE_HUMIDITY, R.drawable.humidity));
		}
		if (sManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
			this.sensorList.add(new Item("Temperature",
					Sensor.TYPE_AMBIENT_TEMPERATURE,
					R.drawable.blue_temperature));
		}
		this.sensorList.add(new Item("Noise", 999, R.drawable.noise));
		// GRID VIEW
		this.gridV.setAdapter(new ImageButtonAdapter(getApplicationContext(),
				sensorList));
		this.gridV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!sensorList.get(position).getName().equals("Noise")) {
					Intent i = new Intent(getApplicationContext(),
							ResultActivity.class);
					i.putExtra("Sensor", sensorList.get(position));
					startActivity(i);
				}
				else{
					Intent i = new Intent(getApplicationContext(),
							NoiseActivity.class);
					startActivity(i);
				}
			}
		});
	}

	public class ImageButtonAdapter extends BaseAdapter {

		private List<Item> sensorLst;
		private Context mContext;

		public ImageButtonAdapter(Context applicationContext,
				List<Item> sensorLst) {
			this.sensorLst = sensorLst;
			this.mContext = applicationContext;

		}

		@Override
		public int getCount() {
			return sensorLst.size();
		}

		@Override
		public Object getItem(int position) {
			return sensorLst.get(position);
		}

		@Override
		public long getItemId(int position) {
			return sensorLst.get(position).getDrawableID();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageResource(sensorLst.get(position).getDrawableID());
			return imageView;
		}

	}

}
