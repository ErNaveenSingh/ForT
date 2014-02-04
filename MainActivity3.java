package com.nav.mytestingapp;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity3 extends Activity implements OnClickListener{

	Handler mHandler;
	Handler mHandler2;
	long totalTime = 20000;//20seconds
	long timeRemaining = 0;
	long startSystemMilli = 0;
	long sTimeRemaining = 0;
	TextView statusTextView;
	TextView statusTextView2;
	Boolean isPaused;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity3);
		statusTextView = (TextView)findViewById(R.id.statusTextView);
		statusTextView2 = (TextView)findViewById(R.id.statusTextView2);
		((Button)findViewById(R.id.pauseButton)).setOnClickListener(this);
		((Button)findViewById(R.id.resetButton)).setOnClickListener(this);
		((Button)findViewById(R.id.startButton)).setOnClickListener(this);
		statusTextView.setText("reset running");
		isPaused = false;
		reset();
		mHandler2 = new Handler();
		mHandler.postDelayed(nRunnable2, 100);
	}
	
	void reset(){
		isPaused = false;
		if (mHandler!=null) {
			mHandler.removeCallbacks(nRunnable);
		}
		mHandler = new Handler();
		mHandler.postDelayed(nRunnable, totalTime);
		startSystemMilli = SystemClock.uptimeMillis();
		timeRemaining = totalTime;
		sTimeRemaining = timeRemaining;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity3, menu);
		return true;
	}
	
	
	Runnable nRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			statusTextView.setText("finished");
		}
	};
	
	Runnable nRunnable2 = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (isPaused) {
				startSystemMilli = SystemClock.uptimeMillis();
			}
			statusTextView2.setText("time remaining:"+(sTimeRemaining - (SystemClock.uptimeMillis() - startSystemMilli))+", timeElapsed "+(totalTime-(sTimeRemaining - (SystemClock.uptimeMillis() - startSystemMilli))));
			mHandler.postDelayed(nRunnable2, 100);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.resetButton) {
			reset();
			statusTextView.setText("reset running");
		}
		else if (v.getId()==R.id.pauseButton) {
			if (mHandler!=null) {
				mHandler.removeCallbacks(nRunnable);
			}
			timeRemaining = timeRemaining - (SystemClock.uptimeMillis() - startSystemMilli);
			statusTextView.setText("paused: "+timeRemaining);

			sTimeRemaining = timeRemaining;
			isPaused = true;
		}else if (v.getId()==R.id.startButton) {
			isPaused = false;
			mHandler = new Handler();
			mHandler.postDelayed(nRunnable, timeRemaining);
			startSystemMilli = SystemClock.uptimeMillis();
			statusTextView.setText("running");
		}
	}
}
