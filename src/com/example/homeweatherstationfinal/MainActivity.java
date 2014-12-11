package com.example.homeweatherstationfinal;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.homeweatherstationfinal.dataendpoint.Dataendpoint;

public class MainActivity extends Activity implements OnClickListener {

	Button serverBtn, sensorBtn, settingsButton, exitBtn;
	public Dataendpoint endpoint;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		serverBtn = (Button) findViewById(R.id.btnServer);
		sensorBtn = (Button) findViewById(R.id.btnSensor);
		settingsButton = (Button) findViewById(R.id.Settings);
		exitBtn = (Button) findViewById(R.id.Exit);

		serverBtn.setOnClickListener(this);
		sensorBtn.setOnClickListener(this);
		settingsButton.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnServer:
			//new DataCollectTask().execute(new Data());
			startActivity(new Intent(this, ServerActivity.class));
			break;
		case R.id.btnSensor:
			startActivity(new Intent(this, SensorActivity.class));
			break;
		case R.id.Settings:
			startActivity(new Intent(this,ServerActivity2.class));
			break;
		case R.id.Exit:
			System.exit(0);
			break;
		}
	}
}
