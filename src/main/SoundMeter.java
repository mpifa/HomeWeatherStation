package main;

import java.io.IOException;

import android.media.MediaRecorder;

public class SoundMeter {
	
	private MediaRecorder mRecorder;
	
	public SoundMeter(){
		this.mRecorder = new MediaRecorder();
	}
	
	public void start() throws IllegalStateException, IOException{
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile("/dev/null");
		mRecorder.prepare();
		mRecorder.start();
	}
	
	public void stop(){
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}
	
	public double getAmplitude(){
		return mRecorder.getMaxAmplitude();
	}

}
