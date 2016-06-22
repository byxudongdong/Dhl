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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author 
 *
 */
public class Jianhuo_huowei extends Activity {
	protected static final int SHOW_ANOTHER_ACTIVITY = 0;
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	
	private SoundPool mSoundPool = null;
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
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

				mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
				Log.i("user_data", huowei_data.getText().toString());
			}							
			editor.commit();
			
			huowei(null);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_huowei);
		huowei_data = (EditText)findViewById(R.id.huowei_data);
		
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
		
		resetTime();
		
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
				        helper = new DatabaseHelper(Jianhuo_huowei.this, newtime.substring(0,10) + ".db");
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
				                Log.i("info", "user_id: " + queryResult.getString(queryResult.getColumnIndex("user_id"))
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

	            		newtime = String.valueOf(year)
	            				+"-"+String.format("%02d",month)
			            		+"-"+String.format("%02d",date);
	            		
	            		Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						editor.commit();
						//����һ��SQLiteHelper����
				        helper = new DatabaseHelper(Jianhuo_huowei.this, newtime.substring(0,10) + ".db");
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
        	},"zongjian_huojia");
		newThread.start(); //�����߳�
	}
	
	public void huowei(View v)
	{
		if(!TextUtils.isEmpty(huowei_data.getText().toString()) &&  chackLocIdRules(huowei_data.getText().toString())==true)
		{
			Editor editor = sp.edit();
			editor.putString("loc_id",  huowei_data.getText().toString() ); //Integer.parseInt()
			editor.commit();
			record();
				 
			startActivity( new Intent( Jianhuo_huowei.this,
	              com.opration.Jianhuo_SKU.class));
			
			finish();
		}else{
			Toast toast = Toast.makeText(getApplicationContext(),
				     "�����ڵĻ���", Toast.LENGTH_LONG);
				   toast.setGravity(Gravity.CENTER, 0, 0);
				   LinearLayout toastView = (LinearLayout) toast.getView();
				   ImageView imageCodeProject = new ImageView(getApplicationContext());
				   imageCodeProject.setImageResource(R.drawable.quit);
				   toastView.addView(imageCodeProject, 0);
				   toast.show();
		}
	}
	
	public Boolean chackLocIdRules(String loc_id)
	{
		//��ȡ�α����
        Cursor queryResult = db.rawQuery("select * from locid where String=? limit ?,?", 
        							new String[]{loc_id,"0","1" });//String.valueOf(packSize)
        int count=queryResult.getCount();
        Log.i("ȡ������", String.valueOf(count));
        if (count > 0)
		{
			return true;
		}
		else
		{
			return false;
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
        		+ "'�ܼ�',"
        		+ "'ɨ��LOCID'"+","
        		+  "'"+sp.getString("doc_id", "")+"'"+","
        		+  "'"+sp.getString("task_id","")+"'"+","
        		+ "'"+sp.getString("loc_id", "")+"'"+","
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
        //mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//����Ϣ������Ƴ�  

    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("����", "����");
		mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//����Ϣ������Ƴ�  
        //�ر����ݿ�
        if(db.isOpen())
        {
        	db.close();
        }
		
	};
	
	
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
                 Intent intent=new Intent(Jianhuo_huowei.this,Timeout.class);  
                 startActivity(intent);  
            }  
        }  
    };  
	
}
