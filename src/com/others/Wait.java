package com.others;

import com.dhl.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Wait extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait);
		
	}
	
	public void back(View v)
	{
		finish();
	}
}
