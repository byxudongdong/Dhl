package com.dhl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class jianhuo_func extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jianhuo_func);
		
	}
	
	public void DocID_ok(View v)
	{
		startActivity( new Intent( jianhuo_func.this,
              com.opration.Opration_Task.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}

}
