package main;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.homeweatherstation.R;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noise);
		tv = (TextView) findViewById(R.id.DB);

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
					tv.setText((String) msg.obj);
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
						msg.obj = "" + (int) d + "dB";
						NoiseActivity.this.handler.sendMessage(msg);
					}
				}
			}
		}).start();

	}

	@Override
	public void onDestroy() {
		mRecorder.release();
		super.onDestroy();
	}

}
