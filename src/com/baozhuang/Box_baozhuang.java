/**
 * 
 */
package com.baozhuang;

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

/**
 * @author 
 *
 */
public class Box_baozhuang extends Activity {
	private static final int SHOW_ANOTHER_ACTIVITY = 0;
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	
	private EditText huowei_data;
	
	DatabaseHelper helper;
	SQLiteDatabase db;
	IntentFilter mFilter =null;
	public String bt_data;
	BroadcastReceiver mreceiver = new  BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			bt_data = intent.getStringExtra(broadrec.EXTRA_DATA);
			
			Editor editor = sp.edit();
			if(huowei_data.hasFocus())
			{
				huowei_data.setText(bt_data);
				huowei_data.setSelection(bt_data.length());
				editor.putString("box_id", bt_data);
				Log.i("user_data", huowei_data.getText().toString());
			}							
			editor.commit();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_baozhuang);
		huowei_data = (EditText)findViewById(R.id.box_id_data);
		
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

            	newtime = sp.getString("NEW_TIME", null);//
				//��ȡͬ��ʱ��
            	if(newtime != null)
            	{
					Editor editor = sp.edit();
					editor.putString("NEW_TIME", newtime);
					editor.commit();
					//����һ��SQLiteHelper����
			        helper = new DatabaseHelper(Box_baozhuang.this, newtime.substring(0,10) + ".db");
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
			        
			        //��ȡ�α����
			        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
			        if (queryResult.getColumnCount() != 0) {
			            //��ӡ��¼
			            while (queryResult.moveToNext()) {
			                Log.i("info", "user_id: " + queryResult.getInt(queryResult.getColumnIndex("user_id"))
			                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
			                        + " String: " + queryResult.getString(queryResult.getColumnIndex("box_id"))
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

            		newtime = String.valueOf(year)
            				+"-"+String.format("%02d",month)
		            		+"-"+String.format("%02d",date);
            		
            		Editor editor = sp.edit();
					editor.putString("NEW_TIME", newtime);
					editor.commit();
					//����һ��SQLiteHelper����
			        helper = new DatabaseHelper(Box_baozhuang.this, newtime.substring(0,10) + ".db");
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
            }
    	},"baozhuang_box");		
		newThread.start(); //�����߳�
	}
	
	public void opration_task(View v)
	{
		if(!TextUtils.isEmpty(huowei_data.getText().toString() ) )
		{
			Editor editor = sp.edit();
			editor.putString("box_id",  huowei_data.getText().toString() ); //Integer.parseInt()
			editor.commit();
			
			record();
			while(newThread.isAlive());
			
			startActivity( new Intent( Box_baozhuang.this,
              com.baozhuang.SKU_baozhuang.class));
		}
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        resetTime();
        registerReceiver(mreceiver,mFilter); 
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        unregisterReceiver(mreceiver);
        mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//����Ϣ������Ƴ�  
        //�ر����ݿ�
        db.close();
    }
	
	private void record()
	{
		db = helper.getWritableDatabase();
        db.execSQL("insert into ptsdata (user_id,task_name,"
        		+ "task_event,doc_id,"+"box_id,"
        		+ "last_opt_id,"
        		+ "pushstate) "
        		+ "values ("
        		+ "'"+sp.getString("user_id", "")+"'"+","
        		+ "'��װ',"
        		+ "'ɨ��BOXID'"+","
        		+ sp.getInt("doc_id", 0)+","
        		//+ sp.getInt("task_id",0)+","
        		+ "'"+sp.getString("box_id", "")+"'"+","
        		+ "0,0)");
	}
	
	@Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        // TODO Auto-generated method stub  
        //Log.i("TAG", "����ing");  
        resetTime();  
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
                 Intent intent=new Intent(Box_baozhuang.this,Timeout.class);  
                 startActivity(intent);  
            }  
        }  
    };  

    
}
