package com.dhl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main_menu extends Activity{
	public Button fahuo_main;
	public Button qita_main;
	public Button sync_main;
	public Button back_main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		fahuo_main = (Button) findViewById(R.id.fahuo_main);
		qita_main = (Button) findViewById(R.id.qita_main);
		sync_main = (Button) findViewById(R.id.sync_main);
		
	}
	
	public void fahuo_main(View v)
	{
		startActivity( new Intent( Main_menu.this,
              com.dhl.Fahuo_menu.class));
	}

	public void back(View v)
	{
		finish();
	}
}
