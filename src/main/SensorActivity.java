package main;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
		if (sManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
			this.sensorList.add(new Item("Altitud", "TYPE_PRESSURE",
					R.drawable.barometro));
			this.sensorList.add(new Item("Barometro", "TYPE_PRESSURE",
					R.drawable.barometro));
		}

		if (sManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
			this.sensorList.add(new Item("Humedad", "TYPE_RELATIVE_HUMIDITY",
					R.drawable.barometro));
		}
		if (sManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
			this.sensorList.add(new Item("Temperatura",
					"TYPE_AMBIENT_TEMPERATURE", R.drawable.barometro));
		}
		this.sensorList.add(new Item("Ruido", "Ruido", R.drawable.barometro));
		this.gridV.setAdapter(new ImageButtonAdapter(getApplicationContext(),
				sensorList));
	}

	public class ImageButtonAdapter extends BaseAdapter {

		private List<Item> sensorLst;
		//private LayoutInflater inflater;
		private Context mContext;
		public ImageButtonAdapter(Context applicationContext,
				List<Item> sensorLst) {
			this.sensorLst = sensorLst;
			this.mContext = applicationContext;
		//	this.inflater = LayoutInflater.from(applicationContext);

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
			return sensorLst.get(position).drawableID;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

		        ImageView imageView;
		        if (convertView == null) {  // if it's not recycled, initialize some attributes
		            imageView = new ImageView(mContext);
		            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
		            imageView.setScaleType(ImageView.ScaleType.CENTER);
		            imageView.setPadding(8, 8, 8, 8);
		        } else {
		            imageView = (ImageView) convertView;
		        }
		        imageView.setImageResource(sensorLst.get(position).getDrawableID());
		        imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "AA", Toast.LENGTH_SHORT).show();
					}
				});
		        return imageView;
		}

	}

	public class Item {
		private String name;
		private String sensorType;
		private int drawableID;

		public Item(String name, String sensorType, int drawableID) {
			this.name = name;
			this.sensorType = sensorType;
			this.drawableID = drawableID;
		}

		public String getName() {
			return this.name;
		}

		public int getDrawableID() {
			return this.drawableID;
		}

		public String getSensorType() {
			return this.sensorType;
		}
	}
}
