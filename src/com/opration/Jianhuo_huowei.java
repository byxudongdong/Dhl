/**
 * 
 */
package com.opration;

import com.login.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author 
 *
 */
public class Jianhuo_huowei extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_huowei);
		
	}
	
	public void huowei(View v)
	{
		startActivity( new Intent( Jianhuo_huowei.this,
              com.opration.Jianhuo_SKU.class));
	}
	
	public void huowei_back(View v)
	{
		finish();
	}
	

}
