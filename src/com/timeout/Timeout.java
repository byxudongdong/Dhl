package com.timeout;

import com.baozhuang.Doc_baozhuang;
import com.dhl.R;

import android.app.Activity;
import android.content.Intent;
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
