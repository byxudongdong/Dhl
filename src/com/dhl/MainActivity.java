package com.dhl;

import com.login.LoginActivity;
import com.login.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	IntentFilter mFilter =null;
	public Button buttom_ok;
	public Button button_close;
	public EditText userid;
	public String bt_data;
	private SharedPreferences sp;
	
	BroadcastReceiver mreceiver = new  BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			bt_data = intent.getStringExtra(broadrec.EXTRA_DATA);
			
			userid.setText(bt_data);
			userid.setSelection(bt_data.length());
			Log.i("user_id", userid.getText().toString());
			Editor editor = sp.edit();
			editor.putString("user_id", bt_data);
			editor.commit();
			
			login_ok(null);

		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttom_ok = (Button) findViewById(R.id.ok);
		button_close = (Button) findViewById(R.id.back);
		userid = (EditText) findViewById(R.id.user_id_data);
		
		mFilter = new IntentFilter();
		mFilter.addAction(broadrec.ACTION_DATA_AVAILABLE);
		registerReceiver(mreceiver,mFilter);
		
		//���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		
	}
	
	public void login_ok(View v)
	{
		if(!TextUtils.isEmpty(userid.getText()))
		{
			Editor editor = sp.edit();
			editor.putString("user_id", userid.getText().toString());
			editor.commit();
			Log.i("user_id", userid.getText().toString());
			startActivity( new Intent( MainActivity.this,
	              com.dhl.Main_menu.class));
		}else{
			Toast.makeText(getApplicationContext(), "�û�ID����Ϊ�գ�", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        registerReceiver(mreceiver,mFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        unregisterReceiver(mreceiver);
    }
	
	public void login_close(View v)
	{
		//finish();
		Intent intent = new Intent(MainActivity.this, LoginActivity.class); 
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

		startActivity(intent); 

	}
}
