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
	
	public void jianhuo_main(View v)
	{
		startActivity( new Intent( jianhuo_func.this,
              com.dhl.jianhuo_func.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}

}
