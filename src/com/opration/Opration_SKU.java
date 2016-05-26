/**
 * 
 */
package com.opration;

import com.dhl.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * @author 
 *
 */
public class Opration_SKU extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_sku);
		
	}
	
	public void sku(View v)
	{
//		startActivity( new Intent( Opration_SKU.this,
//              com.opration.Opration_SKU.class));
	}
	
	public void sku_back(View v)
	{
		finish();
	}
	

}
