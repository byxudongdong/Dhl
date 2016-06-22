package com.opration;

import com.dhl.MainActivity;
import com.dhl.broadrec;
import com.login.DatabaseHelper;
import com.login.R;
import com.timeout.Timeout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class Jianhuo_Doc extends Activity{
	private static final int SHOW_ANOTHER_ACTIVITY = 0;
	private SharedPreferences sp;
	private String newdate;
	private EditText doc_id_data;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	private PlayBeepSound playBeepSound;
	
	DatabaseHelper helper;
	SQLiteDatabase db;
	IntentFilter mFilter =null;
	public String bt_data;
	BroadcastReceiver mreceiver = new  BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			bt_data = intent.getStringExtra(broadrec.EXTRA_DATA);
			
			Editor editor = sp.edit();
			if(doc_id_data.hasFocus())
			{
				doc_id_data.setText(bt_data);
				doc_id_data.setSelection(bt_data.length());
				editor.putString("box_id", bt_data );
				//playBeepSound.playBeepSoundAndVibrate(0);
				playBeepSound.playBeepSoundAndVibrate(0);
				
				Log.i("user_data", doc_id_data.getText().toString());
			}							
			editor.commit();
			DocID_ok(null);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jianhuo_doc);
		doc_id_data = (EditText)findViewById(R.id.doc_id_data);
		
		playBeepSound = new PlayBeepSound(Jianhuo_Doc.this);
		
		mFilter = new IntentFilter();
		mFilter.addAction(broadrec.ACTION_DATA_AVAILABLE);
		registerReceiver(mreceiver,mFilter);
		
		//���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);
		
		//resetTime();
		newThread = new Thread(new Runnable() {
		    @Override
	            public void run() {

	            	newtime = sp.getString("NEW_TIME", null);//
					//��ȡͬ��ʱ��
	            	if(newtime != null)
	            	{
						Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						editor.commit();
						//����һ��SQLiteHelper����
				        helper = new DatabaseHelper(Jianhuo_Doc.this, newtime.substring(0,10) + ".db");
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
			                    +"doc_id text,"
			                    +"task_id text,"
			                    +"loc_id text,"
			                    +"box_id text,"
			                    +"sku text,"
			                    +"qty integer,"
			                    +"last_opt_id integer,"
			                    +"pushstate integer not null"
			                    + ")"
			                    );
				        
				        //��ȡ�α����
				        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
				        if (queryResult.getColumnCount() != 0) {
				            //��ӡ��¼
				            if (queryResult.moveToLast()) {
				                Log.i("info", "user_id: " + queryResult.getInt(queryResult.getColumnIndex("user_id"))
				                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
				                        + " String: " + queryResult.getString(queryResult.getColumnIndex("sku"))
				                        );
				            }				            
				        }
					      	//�ر��α����
				            queryResult.close();

	            	}
	            	else{
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
				        helper = new DatabaseHelper(Jianhuo_Doc.this, newtime.substring(0,10) + ".db");
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
			                    +"doc_id text,"
			                    +"task_id text,"
			                    +"loc_id text,"
			                    +"box_id text,"
			                    +"sku text,"
			                    +"qty integer,"
			                    +"last_opt_id integer,"
			                    +"pushstate integer not null"
			                    + ")"
			                    );
				        
				        //��ȡ�α����
				        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
				        if (queryResult.getColumnCount() != 0) {
				            //��ӡ��¼
				            if (queryResult.moveToLast()) {
				                Log.i("info", "user_id: " + queryResult.getInt(queryResult.getColumnIndex("user_id"))
				                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
				                        + " String: " + queryResult.getString(queryResult.getColumnIndex("sku"))
				                        );
				            }				            
				        }
					      	//�ر��α����
				            queryResult.close();
				        
	            	}
	            }
        	},"zongjian_doc");
		newThread.start();
	}
	
	public void DocID_ok(View v)
	{
		if(!TextUtils.isEmpty(doc_id_data.getText()) )
		{
			Editor editor = sp.edit();
			editor.putString("doc_id",  doc_id_data.getText().toString() );
			editor.commit();
			
			record();
			
			startActivity( new Intent( Jianhuo_Doc.this,
	              com.opration.Jianhuo_Task.class));
			//finish();
		}
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}

	private void record()
	{
		db = helper.getWritableDatabase();

        db.execSQL("insert into ptsdata (user_id,task_name,"
        		+ "task_event,doc_id,last_opt_id,"
        		+ "pushstate) "
        		+ "values ("
        		+ "'"+sp.getString("user_id", "")+"'"+","
        		+ "'�ܼ�','ɨ��DOCID',"
        		+  "'"+sp.getString("doc_id", "")+"'"+","
        		+ "0,0)");
        
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        //resetTime();
        registerReceiver(mreceiver,mFilter); 
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        unregisterReceiver(mreceiver);
        mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//����Ϣ������Ƴ�  
        //�ر����ݿ�
        if(db.isOpen())
        {
        	db.close();
        }
    }
	
	
	@Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        // TODO Auto-generated method stub  
        //Log.i("TAG", "����ing");  
        //resetTime();  
        return super.dispatchTouchEvent(ev);  
    }  
      
    private void resetTime() {  
        // TODO Auto-generated method stub  
        mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//����Ϣ������Ƴ�  
        Message msg = mHandler.obtainMessage(SHOW_ANOTHER_ACTIVITY);  
        mHandler.sendMessageDelayed(msg, 1000*60* sp.getInt("timeout", 10) );//�o����?���Ӻ��M������  
        
    }
      
    private Handler mHandler = new Handler()  
    {  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);  
            if(msg.what==SHOW_ANOTHER_ACTIVITY)  
            {  
                //����activity  
//               Log.i(TAG, "����activity");  
                 Intent intent=new Intent(Jianhuo_Doc.this,Timeout.class);  
                 startActivity(intent);  
            }  
        }  
    };  
}
