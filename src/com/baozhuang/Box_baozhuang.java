/**
 * 
 */
package com.baozhuang;

import com.login.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author 
 *
 */
public class Box_baozhuang extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_baozhuang);
		
	}
	
	public void opration_task(View v)
	{
		startActivity( new Intent( Box_baozhuang.this,
              com.baozhuang.SKU_baozhuang.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}
	

}
