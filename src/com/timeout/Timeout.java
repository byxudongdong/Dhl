package com.timeout;

import com.dhl.broadrec;
import com.login.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Timeout extends Activity {
	
	private EditText doc_id_data;
	private SharedPreferences sp;
	IntentFilter mFilter =null;
	public String bt_data;
	
	BroadcastReceiver mreceiver = new  BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			bt_data = intent.getStringExtra(broadrec.EXTRA_DATA);
			if(doc_id_data.hasFocus())
			{
				doc_id_data.setText(bt_data);
				doc_id_data.setSelection(bt_data.length());
				Log.i("user_data", doc_id_data.getText().toString());
			}
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeout);
		doc_id_data = (EditText)findViewById(R.id.doc_id_data);
		
		mFilter = new IntentFilter();
		mFilter.addAction(broadrec.ACTION_DATA_AVAILABLE);
		registerReceiver(mreceiver,mFilter);
		
		//���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) { //���/����/���η��ؼ�
			//dialog(); 
			Toast.makeText(Timeout.this, "��������ȷDOCID", Toast.LENGTH_SHORT).show();
			return false; 
		} else if(keyCode == KeyEvent.KEYCODE_MENU) {
			// rl.setVisibility(View.VISIBLE);
			Toast.makeText(Timeout.this, "��������ȷDOCID", Toast.LENGTH_SHORT).show();
			return false;
		} else if(keyCode == KeyEvent.KEYCODE_HOME) {
			//����Home��Ϊϵͳ�����˴����ܲ�����Ҫ��дonAttachedToWindow()
			Toast.makeText(Timeout.this, "��������ȷDOCID", Toast.LENGTH_SHORT).show();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void timeout_back(View v)
	{
		if(doc_id_data.getText().toString() != null)
		{
			Log.i("timeout", String.valueOf(sp.getInt("doc_id", 0)) );
			if(String.valueOf(sp.getInt("doc_id", 0)).equals(doc_id_data.getText().toString()) )
			{
				finish();
			}else {
				Toast.makeText(this, "DocId����", 500).show();
			}
		}else
		{
			Toast.makeText(this, "����д��ǰ����DocId", 500).show();
		}
	}

	@Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        registerReceiver(mreceiver,mFilter); 
        Log.i("TimeOut", "���볬ʱ����");
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        unregisterReceiver(mreceiver);
        Log.i("TimeOut", "�˳�&���ٳ�ʱ����");
        finish();
    }
}
