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
public class Fenjian_Task extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_task);
		
	}
	
	public void opration_task(View v)
	{
		startActivity( new Intent( Fenjian_Task.this,
              com.opration.Fenjian_Huowei.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}
	

}
