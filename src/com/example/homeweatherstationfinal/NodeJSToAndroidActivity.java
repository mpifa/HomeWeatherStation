package com.example.homeweatherstationfinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NodeJSToAndroidActivity extends Activity {
	
	private static ListView resultsList;
	private static String sensorType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_result);
		resultsList = (ListView) findViewById(R.id.results);
		sensorType = getIntent().getStringExtra("sensor");
		new HttpAsyncTask().execute("http://192.168.1.14:3000/dades/json");
		
	}
	
	public static List<String> GET(String url) {
		List<String> lst = new ArrayList<String>();
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Error";
			JSONArray inputArray = new JSONArray(result);
			JSONObject jo = inputArray.getJSONObject(0);
		
			for(int i=0; i<inputArray.length();i++){
				JSONObject js = (JSONObject) inputArray.get(i);
				if(js.getString("Sid").equals(sensorType)){
					lst.add(js.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		
		return lst;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... urls) {
 
            return GET(urls[0]);
        }
        @Override
        protected void onPostExecute(List<String> result) {
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_format,
					R.id.textView1, result);

			resultsList.setAdapter(adapter);
       }
    }
}
