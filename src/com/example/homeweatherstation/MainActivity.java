package com.example.homeweatherstation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button serverbtn = (Button) findViewById(R.id.btnServer);
		Button sensorbtn = (Button) findViewById(R.id.btnSensor);
		serverbtn.setOnClickListener(this);
		sensorbtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	    }
	}

}
