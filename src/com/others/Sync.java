package com.others;

import java.util.Date;

import com.gson.SendJson;
import com.login.DatabaseHelper;
import com.login.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class Sync extends Activity {
	DatabaseHelper helper;
	SQLiteDatabase db;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync);
		
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
				        helper = new DatabaseHelper(Sync.this, newtime.substring(0,10) + ".db");
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
				            if (queryResult.moveToLast()) {
				            	
					        	int ref_id =  queryResult.getInt(queryResult.getColumnIndex("ref_id"));
							    String user_id = queryResult.getString(queryResult.getColumnIndex("user_id"));  
							    String task_time = queryResult.getString(queryResult.getColumnIndex("task_time"));						     
							    String task_name = queryResult.getString(queryResult.getColumnIndex("task_name"));
							    String task_event = queryResult.getString(queryResult.getColumnIndex("task_event"));  						   
							    int doc_id = queryResult.getInt(queryResult.getColumnIndex("doc_id"));
							    int task_id = queryResult.getInt(queryResult.getColumnIndex("task_id"));					   
							    String loc_id = queryResult.getString(queryResult.getColumnIndex("loc_id"));
							    String box_id = queryResult.getString(queryResult.getColumnIndex("box_id"));
							    String sku = queryResult.getString(queryResult.getColumnIndex("sku"));
							    int qty = queryResult.getInt(queryResult.getColumnIndex("qty"));
							    int last_opt_id = queryResult.getInt(queryResult.getColumnIndex("last_opt_id"));
							    int pushstate = queryResult.getInt(queryResult.getColumnIndex("pushstate"));
				            	
				                Log.i("info", "user_id: " + user_id
				                        + " timastamp: " + task_time
				                        + " String: " + task_event
				                        );
				                
						        SendJson.main( String.valueOf(ref_id),  
									     user_id,  
									     task_time,						     
									     task_name, 
									     task_event,   						   
									     String.valueOf(doc_id),
									     String.valueOf(task_id),						   
									     loc_id,
									     box_id,
									     sku,
									     String.valueOf(qty),
									     String.valueOf(last_opt_id),
									     pushstate);
						        //GsonTest1.main(null);
				            }				            
				        }
				      	//�ر��α����
			            queryResult.close();
				            
	            	}	            
        	},"wait");
		newThread.start();
	}
	
	public void back(View v)
	{
		dialog();
	}
	
	protected void dialog() { 
		AlertDialog.Builder builder = new Builder(Sync.this); 
		builder.setMessage("ȷ��Ҫ�˳���?"); 
		builder.setTitle("��ʾ"); 
		builder.setPositiveButton("ȷ��", 
			new android.content.DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) 
				{ 
					dialog.dismiss(); 
					
					Sync.this.finish(); 
				} 
			}
		); 
		builder.setNegativeButton("ȡ��", 
			new android.content.DialogInterface.OnClickListener() 
			{ 
				public void onClick(DialogInterface dialog, int which) 
				{ 
					dialog.dismiss(); 
				} 
			}
		); 
		builder.create().show(); 
	} 
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) { //���/����/���η��ؼ�
			dialog(); 
			return false; 
		} 
		return super.onKeyDown(keyCode, event);
	}
}
