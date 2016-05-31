package com.others;

import com.login.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Sync extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync);
		
	}
	
	public void back(View v)
	{
		finish();
	}
}
