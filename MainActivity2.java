package com.nav.mytestingapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity2 extends Activity {

    ImageView myImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main2);
        myImageView = (ImageView)findViewById(R.id.myImageView);
        ((Button)findViewById(R.id.myButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				animate();
//				a.setDuration(1500);
//				myImageView.startAnimation(a);
//				animate2();
			}
		});

    }
    
    Animation a = new Animation() {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
        	final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)myImageView.getLayoutParams();
            params.leftMargin = (int)(0 * interpolatedTime);
            params.rightMargin = (int)(0 * interpolatedTime);
            myImageView.setLayoutParams(params);
        }
    };
    
    public void animate2() {
    	final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myImageView.getLayoutParams();
    	AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(myImageView, "Width",20))
           .with(ObjectAnimator.ofFloat(myImageView, "Height",20));
        set.setDuration(1500);
        set.start();
	}
    
    public void animate() {
    	final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myImageView.getLayoutParams();
    	final LayoutParams params2 =  myImageView.getLayoutParams();
    	ValueAnimator animation = ValueAnimator.ofInt(params.leftMargin, 200);
    	animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    	    @Override
    	    public void onAnimationUpdate(ValueAnimator valueAnimator)
    	    {
    	        params.leftMargin = (Integer) valueAnimator.getAnimatedValue();
    	        myImageView.setLayoutParams(params);
    	    }
    	});
    	ValueAnimator animation2 = ValueAnimator.ofInt(params.topMargin, 200);
    	animation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    	    @Override
    	    public void onAnimationUpdate(ValueAnimator valueAnimator)
    	    {
    	        params.topMargin = (Integer) valueAnimator.getAnimatedValue();
    	        myImageView.setLayoutParams(params);
    	    }
    	});
    	ValueAnimator animation3 = ValueAnimator.ofInt(params2.height, 300);
    	animation3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    	    @Override
    	    public void onAnimationUpdate(ValueAnimator valueAnimator)
    	    {
    	        params.height = (Integer) valueAnimator.getAnimatedValue();
    	        myImageView.setLayoutParams(params);
    	    }
    	});
    	ValueAnimator animation4 = ValueAnimator.ofInt(params2.width, 300);
    	animation4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    	    @Override
    	    public void onAnimationUpdate(ValueAnimator valueAnimator)
    	    {
    	        params.width = (Integer) valueAnimator.getAnimatedValue();
    	        myImageView.setLayoutParams(params);
    	    }
    	});
    	
    	AnimatorSet set = new AnimatorSet();
    	set.playTogether(animation, animation2, animation3, animation4);
    	set.setDuration(500);
    	set.start();
	}
    
    private Bitmap loadChart(String urlRqs) {   
        Bitmap bm = null;
        InputStream inputStream = null;

        try {
            inputStream = OpenHttpConnection(urlRqs);
            if (inputStream!=null) {
                bm = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
            else {
                Common.LOG("inputstream is null");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bm;
    }
    private InputStream OpenHttpConnection(String strURL) throws IOException {
        InputStream is = null;  
        try {
            URL url = new URL(strURL);          
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
            httpConn.setConnectTimeout(30000);
            httpConn.setReadTimeout(30000);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.connect();
            is = httpConn.getInputStream();     
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return is;
    }

   
}
