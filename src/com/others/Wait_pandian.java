package com.others;

import com.login.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Wait_pandian extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait2);
		
	}
	
	public void back(View v)
	{
		finish();
	}
}
