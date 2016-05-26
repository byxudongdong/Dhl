/**
 * 
 */
package com.opration;

import com.dhl.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author 
 *
 */
public class Fenjian_Huowei extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_huowei);
		
	}
	
	public void huowei(View v)
	{
		startActivity( new Intent( Fenjian_Huowei.this,
              com.opration.Fenjian_SKU.class));
	}
	
	public void huowei_back(View v)
	{
		finish();
	}
	

}
