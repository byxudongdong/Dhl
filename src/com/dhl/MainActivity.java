package com.dhl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	public Button buttom_ok;
	public Button button_close;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttom_ok = (Button) findViewById(R.id.ok);
		button_close = (Button) findViewById(R.id.back);
		
		
	}
	
	public void login_ok(View v)
	{
		startActivity( new Intent( MainActivity.this,
              com.dhl.main_menu.class));
	}
	
	public void login_close(View v)
	{
		finish();
	}
}
