package com.example.customobjectgl;

import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;

public class MainActivity extends Activity {

	private Scene glScene;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(glScene == null)
        	glScene = new Scene(this);
        setContentView(glScene);
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
    public boolean onTouchEvent(MotionEvent event){
    	return glScene.onTouchEvent(event);
    }
    
}
