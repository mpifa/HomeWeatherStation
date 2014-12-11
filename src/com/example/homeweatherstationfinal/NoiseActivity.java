package com.example.homeweatherstationfinal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.homeweatherstationfinal.dataendpoint.Dataendpoint;
import com.example.homeweatherstationfinal.dataendpoint.model.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;

public class NoiseActivity extends Activity {

	public static final int SAMPLE_RATE = 16000;

	private AudioRecord mRecorder;
	private short[] mBuffer;
	private final String startRecordingLabel = "Start recording";
	private final String stopRecordingLabel = "Stop recording";
	private boolean mIsRecording = false;
	private ProgressBar mProgressBar;
	private TextView tv;
	private Handler handler;
	public Dataendpoint endpoint;
	private List<String> resultsList;
	private Data data;
	private Item item;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = new Data();
		data.setId("0");
		item = getIntent().getParcelableExtra("Sensor");
		data.setSensorType(item.getName());
		setContentView(R.layout.activity_noise);
		tv = (TextView) findViewById(R.id.DB);
		resultsList = new ArrayList<String>();

		initRecorder();
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		final Button button = (Button) findViewById(R.id.start);
		button.setText(startRecordingLabel);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mIsRecording) {
					button.setText(stopRecordingLabel);
					mIsRecording = true;
					mRecorder.startRecording();
					startBufferedWrite();
				} else {
					button.setText("Start");
					mIsRecording = false;
					mRecorder.stop();
				}
			}
		});

		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					resultsList.add((String) msg.obj);
					tv.setText((String) msg.obj+"dB");
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	/**
	 * Method to start configure bandwidth and initialize a buffer.
	 */
	private void initRecorder() {
		int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		mBuffer = new short[bufferSize];
		mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
				bufferSize);
	}

	/**
	 * Use a thread to calculate in background.
	 */
	private void startBufferedWrite() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (mIsRecording) {
					double sum = 0;
					int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
					for (int i = 0; i < readSize; i++) {
						sum += mBuffer[i] * mBuffer[i];
					}
					if (readSize > 0) {
						final double amplitude = sum / readSize;
						mProgressBar.setProgress((int) Math.sqrt(amplitude));
						Message msg = new Message();
						double d = 10 * Math.log10((int) Math.sqrt(amplitude)
								* (int) Math.sqrt(amplitude));
						msg.obj = "" + (int) d;
						NoiseActivity.this.handler.sendMessage(msg);
					}
				}
			}
		}).start();

	}

	@Override
	public void onDestroy() {
		data.setValues(getAverage()+" dB");
		new AndroidToNodeJS(data);
		new DataCollectTask().execute(data);
		mRecorder.release();
		super.onDestroy();
	}

	private String getAverage(){
		int num = 0;
		for(String res: resultsList){
			num+= Integer.parseInt(res);
		}
		return Integer.toString(num/resultsList.size());
		
	}
	public class DataCollectTask extends AsyncTask<Data, Void, String>{

		@Override
		protected String doInBackground(Data... params) {
			
			Dataendpoint.Builder builder = new Dataendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
			
			builder = CloudEndpointUtils.updateBuilder(builder);
			
			endpoint = builder.build();
			
			try {
				endpoint.insertData(params[0]).execute();
				return "OK";
			} catch (IOException e) {
				e.printStackTrace();
				return "ERROR";
			}
		}
	}

}
