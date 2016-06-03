/**
 * 
 */
package com.opration;

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
public class Jianhuo_huowei extends Activity {
	protected static final int SHOW_ANOTHER_ACTIVITY = 0;
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //声明一个子线程    
	
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
		setContentView(R.layout.opration_huowei);
		huowei_data = (EditText)findViewById(R.id.huowei_data);
		
		mFilter = new IntentFilter();
		mFilter.addAction(broadrec.ACTION_DATA_AVAILABLE);
		registerReceiver(mreceiver,mFilter);
		
		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);
		
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
				        helper = new DatabaseHelper(Jianhuo_huowei.this, newtime.substring(0,10) + ".db");
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
				        
				        //获取游标对象
				        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
				        if (queryResult.getColumnCount() != 0) {
				            //打印记录
				            if (queryResult.moveToLast()) {
				                Log.i("info", "user_id: " + queryResult.getInt(queryResult.getColumnIndex("user_id"))
				                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
				                        + " String: " + queryResult.getString(queryResult.getColumnIndex("sku"))
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
				        helper = new DatabaseHelper(Jianhuo_huowei.this, newtime.substring(0,10) + ".db");
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
				        
				        //获取游标对象
				        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
				        if (queryResult.getColumnCount() != 0) {
				            //打印记录
				            if (queryResult.moveToLast()) {
				                Log.i("info", "user_id: " + queryResult.getInt(queryResult.getColumnIndex("user_id"))
				                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
				                        + " String: " + queryResult.getString(queryResult.getColumnIndex("sku"))
				                        );
				            }				            
				        }
					      	//关闭游标对象
				            queryResult.close();
				        
	            	}

	            }
        	},"zongjian_huojia");
		newThread.start(); //启动线程
	}
	
	public void huowei(View v)
	{
		if(!TextUtils.isEmpty(huowei_data.getText().toString() ) )
		{
			Editor editor = sp.edit();
			editor.putString("loc_id",  huowei_data.getText().toString() ); //Integer.parseInt()
			editor.commit();
			record();
			while(newThread.isAlive());
			 
			startActivity( new Intent( Jianhuo_huowei.this,
	              com.opration.Jianhuo_SKU.class));
		}
	}
	
	public void huowei_back(View v)
	{
		finish();
	}
	
	private void record()
	{
		db = helper.getWritableDatabase();
        db.execSQL("insert into ptsdata (user_id,task_name,"
        		+ "task_event,doc_id,"+"task_id,"+"loc_id,"
        		+ "last_opt_id,"
        		+ "pushstate) "
        		+ "values ("
        		+ "'"+sp.getString("user_id", "")+"'"+","
        		+ "'总拣',"
        		+ "'扫描LOCID'"+","
        		+ sp.getInt("doc_id", 0)+","
        		+ sp.getInt("task_id",0)+","
        		+ "'"+sp.getString("loc_id", "")+"'"+","
        		+ "0,0)");
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
        mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除  
        //关闭数据库
        db.close();
    }
	
	
	@Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        // TODO Auto-generated method stub  
        //Log.i("TAG", "操作ing");  
        resetTime();  
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
                 Intent intent=new Intent(Jianhuo_huowei.this,Timeout.class);  
                 startActivity(intent);  
            }  
        }  
    };  
	
}
