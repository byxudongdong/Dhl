package com.baozhuang;

import com.login.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Doc_baozhuang extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jianhuo_doc);
		
	}
	
	public void DocID_ok(View v)
	{
		startActivity( new Intent( Doc_baozhuang.this,
              com.baozhuang.Box_baozhuang.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}

}
