package com.example.homeweatherstation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button serverBtn, sensorBtn, exitBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		serverBtn = (Button) findViewById(R.id.btnServer);
		sensorBtn = (Button) findViewById(R.id.btnSensor);
		exitBtn = (Button) findViewById(R.id.Exit);
		
		serverBtn.setOnClickListener(this);
		sensorBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	    case R.id.btnServer:
	    	startActivity(new Intent(this, ServerActivity.class));
	        break;
	    case R.id.btnSensor:
	    	startActivity(new Intent(this, SensorActivity.class));
	    	break;
	    case R.id.Exit:
	    	System.exit(0);
	    }
	}

}
