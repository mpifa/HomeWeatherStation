package com.example.homeweatherstationfinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.homeweatherstationfinal.dataendpoint.model.Data;

public class AndroidToNodeJS {

	private static Data data;

	public AndroidToNodeJS(Data data) {
		this.data = data;
		new HttpAsyncTask().execute(data);
	}

	public static String POST(String url, Data d) {
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			String json = "";

			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("id", data.getId());
			jsonObject.accumulate("name", data.getSensorType());
			jsonObject.accumulate("coords", data.getCoordinates());
			jsonObject.accumulate("Value", data.getValues());

			json = jsonObject.toString();

			StringEntity se = new StringEntity(json);

			httpPost.setEntity(se);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpclient.execute(httpPost);

			inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
		}

		return result;
	}

	private class HttpAsyncTask extends AsyncTask<Data, Void, String> {
		@Override
		protected String doInBackground(Data... urls) {

			return POST("http://192.168.1.14:3000/dades", urls[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("DONE!");
		}
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

}
