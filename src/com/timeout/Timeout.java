package com.timeout;

import java.util.HashMap;
import java.util.Map;

import com.dhl.broadrec;
import com.login.DatabaseHelper;
import com.login.R;
import com.opration.Jianhuo_Doc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Timeout extends Activity {
	
	private SoundPool mSoundPool = null;
	
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	
	private EditText doc_id_data;
	private SharedPreferences sp;
	IntentFilter mFilter =null;
	public String bt_data;
	
	DatabaseHelper helper;
	SQLiteDatabase db;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	
	BroadcastReceiver mreceiver = new  BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			bt_data = intent.getStringExtra(broadrec.EXTRA_DATA);
			if(doc_id_data.hasFocus())
			{
				doc_id_data.setText(bt_data);
				doc_id_data.setSelection(bt_data.length());
				
				mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
				timeout_back(null);
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
		
        mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        soundMap.put(1, mSoundPool.load(this, R.raw.test_2k_8820_200ms, 1));
        soundMap.put(2, mSoundPool.load(this, R.raw.error, 1));
		
		mFilter = new IntentFilter();
		mFilter.addAction(broadrec.ACTION_DATA_AVAILABLE);
		registerReceiver(mreceiver,mFilter);
		
		//���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);
		
		newThread = new Thread(new Runnable() {
		    @Override
	            public void run() {
	            	
	            		Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
	            		t.setToNow(); // ȡ��ϵͳʱ�䡣  
	            		int year = t.year;  
	            		int month = t.month + 1;  
	            		int date = t.monthDay;  
//	            		int hour = t.hour; // 0-23  
//	            		int minute = t.minute;  
//	            		int second = t.second;
	            		
	            		newtime = String.valueOf(year)
	            				+"-"+String.format("%02d",month)
			            		+"-"+String.format("%02d",date);
	            		
	            		Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						editor.commit();
						//����һ��SQLiteHelper����
				        helper = new DatabaseHelper(Timeout.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        db = helper.getWritableDatabase();
				        
				      //����һ����				        
				        db.execSQL("create table if not exists ptsdata "
				        		+"("			                    				                    
			                    +"ref_id integer primary key," 
			                    +"user_id text not null,"
			                    +"task_time timestamp not null default (datetime('now','localtime')),"
			                    +"task_name text not null,"
			                    +"task_event text,"
			                    +"doc_id integer,"
			                    +"task_id integer,"
			                    +"loc_id text,"
			                    +"box_id text,"
			                    +"sku text,"
			                    +"qty integer,"
			                    +"last_opt_id integer,"
			                    +"pushstate integer not null"
			                    + ")"
			                    );				        
				        
	            	}	            
        	},"timeout_doc");
		newThread.start();
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
			Log.i("timeout", sp.getString("doc_id", "") );
			if(sp.getString("doc_id", "").equals(doc_id_data.getText().toString()) )
			{
				Log.i("TimeOut", "�˳�&���ٳ�ʱ����");
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
//        //�ر����ݿ�
//        if(db.isOpen())
//        {
//        	db.close();
//        }
        
        //finish();
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	
    	record();
        //�ر����ݿ�
        if(db.isOpen())
        {
        	db.close();
        }

    }
    
    private void record()
	{
		db = helper.getWritableDatabase();

        db.execSQL("insert into ptsdata (user_id,task_name,"
        		+ "task_event,doc_id,last_opt_id,"
        		+ "pushstate) "
        		+ "values ("
        		+ "'"+sp.getString("user_id", "")+"'"+","
        		+ "'��ʱ','Indirect',"
        		+ "'"+sp.getString("doc_id", "") +"'"+","
        		+ "0,0)");
        
	}
}
