package com.example.sample_app;

import java.text.DateFormat;
import java.util.Date;

import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThirdActivity extends Activity {
	WebView myWebView ;
	RelativeLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		layout= (RelativeLayout) findViewById(R.id.webviewRelativeLayout);
	     
	    myWebView = (WebView) findViewById(R.id.webview2);

		WebSettings webSettings = myWebView.getSettings();
	
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
		
		// String url ="https://ievolvecontent.ultimatix.net/Courses/CLI_Culture_Pro_United_States_of_America/index.html";
		//String url ="https://ievolvecontent.ultimatix.net/ScormEngineInterface/defaultui/deliver.jsp?preventRightClick=false&cc=&configuration=&ieCompatibilityMode=none&forceReview=false&tracking=true&package=ContentId%7C3804%21VersionId%7C0&registration=ContentId%7C3804%21InstanceId%7C0%21UserId%7C891403";
		String url="https://ievolve.ultimatix.net/ScormEngineInterface/defaultui/launch.jsp?registration=ContentId|3804!UserId|891403&cc=en_US&configuration=";
		
		myWebView.setWebChromeClient(new WebChromeClient()
		{
			public boolean onCreateWindow(final WebView view, boolean isDialog, boolean isUserGesture, android.os.Message resultMsg) {
				view.removeAllViews();
				WebView childView = new WebView(ThirdActivity.this);
		        childView.getSettings().setJavaScriptEnabled(true);
		        childView.setBackgroundColor(Color.argb(1, 0, 0, 0));
		        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		        childView.setWebChromeClient(new WebChromeClient()
		        {
		        	@Override
		        	public void onCloseWindow(WebView childView) {
		        		// TODO Auto-generated method stub
		        		view.removeView(childView);
		        		childView.removeAllViews();
		        		childView.destroy();
						Log.d("On Close", "Closing child WebView Only");
				        Handler handler = new Handler();
				        handler.postDelayed(new Runnable() {
				            public void run() {
				            	layout.removeView(view);
				            	ThirdActivity.this.finish();
				            }
				        }, 20000);
				        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
						Log.d("TIME AT CLOSE: ", currentDateTimeString);
		        		super.onCloseWindow(childView);
		        	}
		        });
		        childView.setWebViewClient(new WebViewClient());
		        childView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		        view.addView(childView);
		        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		        transport.setWebView(childView);
		        resultMsg.sendToTarget();
		        return true;
		    }
			
			@Override
			public void onCloseWindow(WebView window) {
			
				window.destroy();
				Log.d("On Close", "Closing parentview in 20 seconds");
				super.onCloseWindow(window);
			}

			
		});
		myWebView.setWebViewClient(new WebViewClient(){

	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                view.loadUrl(url);
	                return true;
	                }
	        });
		myWebView.loadUrl(url);
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_third, menu);
		return true;
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	    System.out.println("on stop calld");
		myWebView.clearCache(true);
		myWebView.clearHistory();
		myWebView.clearFormData();
	
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("ThirdActivitiy *****", "On Pause Called");
		((AudioManager)getSystemService(
	            Context.AUDIO_SERVICE)).requestAudioFocus(
	                    new OnAudioFocusChangeListener() {
	                        @Override
	                        public void onAudioFocusChange(int focusChange) {}
	                    }, AudioManager.STREAM_MUSIC, 
	                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Log.d("ThirdActivitiy *****", "Finishing Activty , destroying webview.");
	/*	myWebView.clearCache(true);
		myWebView.clearHistory();
		myWebView.clearFormData();*/
		myWebView.removeAllViews();
		myWebView.destroy();
	}

}
