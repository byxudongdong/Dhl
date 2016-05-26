package com.opration;

import com.dhl.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Jianhuo_Doc extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jianhuo_doc);
		
	}
	
	public void DocID_ok(View v)
	{
		startActivity( new Intent( Jianhuo_Doc.this,
              com.opration.Jianhuo_Task.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}

}
