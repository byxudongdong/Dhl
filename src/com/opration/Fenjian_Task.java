/**
 * 
 */
package com.opration;

import java.util.HashMap;

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
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * @author 
 *
 */
public class Fenjian_Task extends Activity {
	
	private SoundPool mSoundPool = null;
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	
	private static final int SHOW_ANOTHER_ACTIVITY = 0;
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //声明一个子线程    	
	
	private EditText task_id_data;
	
	DatabaseHelper helper;
	SQLiteDatabase db;
	IntentFilter mFilter =null;
	public String bt_data;
	BroadcastReceiver mreceiver = new  BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			bt_data = intent.getStringExtra(broadrec.EXTRA_DATA);
			
			Editor editor = sp.edit();
			if(task_id_data.hasFocus())
			{
				task_id_data.setText(bt_data);
				task_id_data.setSelection(bt_data.length());
				editor.putString("box_id", bt_data );
				
				mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
				
				Log.i("user_data", task_id_data.getText().toString());
			}							
			editor.commit();
			
			opration_task(null);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_task);
		task_id_data = (EditText)findViewById(R.id.task_id_data);
		
		mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		soundMap.put(1, mSoundPool.load(this, R.raw.test_2k_8820_200ms, 1));
		soundMap.put(2, mSoundPool.load(this, R.raw.error, 1));
		
		mFilter = new IntentFilter();
		mFilter.addAction(broadrec.ACTION_DATA_AVAILABLE);
		registerReceiver(mreceiver,mFilter);
		
		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);
		
		resetTime();
		
		newThread = new Thread(new Runnable() {
		    @Override
            public void run() {

            	newtime = sp.getString("NEW_TIME", null);//
				//获取同步时间
            	if(newtime != null)
            	{
					Editor editor = sp.edit();
					editor.putString("NEW_TIME", newtime);
					editor.commit();
					//创建一个SQLiteHelper对象
			        helper = new DatabaseHelper(Fenjian_Task.this, newtime.substring(0,10) + ".db");
			        //使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
			        db = helper.getWritableDatabase();
			        
			      //创建一个表				        
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

			        
			        //获取游标对象
			        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
			        if (queryResult.getColumnCount() != 0) {
			            //打印记录
			            if (queryResult.moveToLast()) {
			                Log.i("info", "user_id: " + queryResult.getString(queryResult.getColumnIndex("user_id"))
			                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
			                        + " String: " + queryResult.getInt(queryResult.getColumnIndex("task_id"))
			                        );
			            }				            
			        }
				      	//关闭游标对象
			            queryResult.close();

            	}
            	else{
            		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
            		t.setToNow(); // 取得系统时间。  
            		int year = t.year;  
            		int month = t.month + 1;  
            		int date = t.monthDay;  

            		newtime = String.valueOf(year)
            				+"-"+String.format("%02d",month)
		            		+"-"+String.format("%02d",date);
            		
            		Editor editor = sp.edit();
					editor.putString("NEW_TIME", newtime);
					editor.commit();
					//创建一个SQLiteHelper对象
			        helper = new DatabaseHelper(Fenjian_Task.this, newtime.substring(0,10) + ".db");
			        //使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
			        db = helper.getWritableDatabase();
			        
			      //创建一个表				        
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
			        
            	}
            }
    	},"fenjian_task");
		newThread.start(); //启动线程
	}
	
	public void opration_task(View v)
	{
		if(!TextUtils.isEmpty(task_id_data.getText()) )
		{
			Editor editor = sp.edit();
			editor.putString("task_id", task_id_data.getText().toString() );
			editor.commit();
			
			record();
			
			startActivity( new Intent( Fenjian_Task.this,
					com.opration.Fenjian_Huowei.class));			
			
			finish();
		}
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_ENTER) { //监控/拦截/屏蔽返回键			
			opration_task(null);
			return false; 
		} 
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        //resetTime();
        registerReceiver(mreceiver,mFilter); 
        task_id_data.setText("");
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        unregisterReceiver(mreceiver);
        //mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除  
        //关闭数据库
        if(db.isOpen())
        {
        	db.close();
        }
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("销毁", "销毁");
		mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除  
		
	};
    
	private void record()
	{
		db = helper.getWritableDatabase();
        db.execSQL("insert into ptsdata (user_id,task_name,"
        		+ "task_event,doc_id,"+"task_id,"
        		+ "last_opt_id,"
        		+ "pushstate) "
        		+ "values ("
        		+ "'"+sp.getString("user_id", "")+"'"+","
        		+ "'分拣','扫描TASKID',"
        		+  "'"+sp.getString("doc_id", "")+"'"+","
        		+  "'"+sp.getString("task_id","")+"'"+","
        		+ "0,0)");
	}
	
	
	@Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        // TODO Auto-generated method stub  
        //Log.i("TAG", "操作ing");  
        //resetTime();  
        return super.dispatchTouchEvent(ev);  
    }  
      
    private void resetTime() {  
        // TODO Auto-generated method stub  
        mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除  
        Message msg = mHandler.obtainMessage(SHOW_ANOTHER_ACTIVITY);  
        mHandler.sendMessageDelayed(msg, 1000*60* sp.getInt("timeout", 10) );//無操作?分钟后進入屏保  
    }
      
    private Handler mHandler = new Handler()  
    {  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);  
            if(msg.what==SHOW_ANOTHER_ACTIVITY)  
            {  
                //跳到activity  
//               Log.i(TAG, "跳到activity");  
                 Intent intent=new Intent(Fenjian_Task.this,Timeout.class);  
                 startActivity(intent);  
            }  
        }  
    };  
	

}
