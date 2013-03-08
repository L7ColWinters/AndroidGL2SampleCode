package com.example.customobjectgl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.app.Activity;

public class MainActivity extends Activity {

	private Scene glScene;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = (FrameLayout)LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        if(glScene == null)
        	glScene = new Scene(this);
        layout.addView(glScene);
        setContentView(layout);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	glScene.onPause();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	glScene.onResume();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	glScene.onDestroy();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event){
    	return glScene.onTouchEvent(event);
    }
    
}
