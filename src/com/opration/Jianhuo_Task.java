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
public class Jianhuo_Task extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_task);
		
	}
	
	public void opration_task(View v)
	{
		startActivity( new Intent( Jianhuo_Task.this,
              com.opration.Jianhuo_huowei.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}
	

}
