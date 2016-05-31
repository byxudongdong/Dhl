package com.timeout;

import com.login.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Timeout extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeout);
		
	}
	
	
	public void timeout(View v)
	{
		finish();
	}

}
