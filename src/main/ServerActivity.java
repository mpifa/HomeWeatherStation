package main;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import main.SensorActivity.ImageButtonAdapter;

import com.example.homeweatherstation.R;

import android.hardware.Sensor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ServerActivity extends Activity {

	private List<Item> itemLst;
	private GridView gv;
	private ConnectivityManager connManager;
	private NetworkInfo netInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);

		this.itemLst = new ArrayList<Item>();
		gv = (GridView) findViewById(R.id.gridView1);

		this.itemLst.add(new Item("Height", Sensor.TYPE_PRESSURE,
				R.drawable.elevation));
		this.itemLst.add(new Item("Barometer", Sensor.TYPE_PRESSURE,
				R.drawable.barometro));

		this.itemLst.add(new Item("Humidity", Sensor.TYPE_RELATIVE_HUMIDITY,
				R.drawable.humidity));
		this.itemLst.add(new Item("Temperature",
				Sensor.TYPE_AMBIENT_TEMPERATURE, R.drawable.blue_temperature));
		this.itemLst.add(new Item("Noise", R.drawable.noise));
		// Internet
		this.connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		this.netInfo = connManager.getActiveNetworkInfo();
		// Gridview
		this.gv.setAdapter(new ImageButtonAdapter(getApplicationContext(),
				itemLst));
		this.gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (isOnline()) {
					//TODO WHEN SERVER IS RUNNING
				} else {
					Toast.makeText(getApplicationContext(),
							"Please connect to internet", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	public boolean isOnline() {
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
