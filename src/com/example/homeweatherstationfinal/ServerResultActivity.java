package com.example.homeweatherstationfinal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.homeweatherstationfinal.dataendpoint.Dataendpoint;
import com.example.homeweatherstationfinal.dataendpoint.model.CollectionResponseData;
import com.example.homeweatherstationfinal.dataendpoint.model.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Build;

public class ServerResultActivity extends Activity {

	private ListView resultsList;
	public Dataendpoint endpoint;
	private String sensorType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_result);
		resultsList = (ListView) findViewById(R.id.results);
		sensorType = getIntent().getStringExtra("sensor");
		new ListOfDataAsyncRetriever().execute();
		registerForContextMenu(resultsList);
	}

	private class ListOfDataAsyncRetriever extends
			AsyncTask<Void, Void, CollectionResponseData> {

		@Override
		protected CollectionResponseData doInBackground(Void... params) {

			Dataendpoint.Builder endpointBuilder = new Dataendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			endpointBuilder = CloudEndpointUtils.updateBuilder(endpointBuilder);
			CollectionResponseData result;

			Dataendpoint endpoint = endpointBuilder.build();

			try {
				result = endpoint.listData().execute();
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = null;
			}
			return result;
		}

		@Override
		@SuppressWarnings("null")
		protected void onPostExecute(CollectionResponseData result) {

			final List<Data> datas = result.getItems();

			List<String> list = new ArrayList<String>();

			for (Data data : datas) {
				if(data.getSensorType().equals(sensorType))
					list.add(data.getId()+"\r"+data.getSensorType() + "\r" + data.getValues());
			}
			if (list.size() < 1) {
				list.add("EMPTY");
			}
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.list_format,
					R.id.textView1, list);

			resultsList.setAdapter(adapter);

			resultsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long id) {
				}

			});

		}

	}
}
