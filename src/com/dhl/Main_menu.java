package com.dhl;

import com.login.DatabaseHelper;
import com.login.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import gettime.Gettime;

public class Main_menu extends Activity{
	public Button fahuo_main;
	public Button qita_main;
	public Button sync_main;
	public Button back_main;
	private GridLayout main_menu_grid;
	private LinearLayout login_dialog_progress_line;
	private SharedPreferences sp;
	int newref_id;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		fahuo_main = (Button) findViewById(R.id.fahuo_main);
		qita_main = (Button) findViewById(R.id.qita_main);
		sync_main = (Button) findViewById(R.id.sync_main);
		back_main = (Button) findViewById(R.id.back_main);
		
		main_menu_grid = (GridLayout) findViewById(R.id.main_menu_grid);
        login_dialog_progress_line = (LinearLayout) findViewById(R.id.login_dialog_progress_line);
        
		login_dialog_progress_line.setVisibility(View.VISIBLE);
		
		fahuo_main.setClickable(false);
		qita_main.setClickable(false);
		sync_main.setClickable(false);
		back_main.setClickable(false);
		
		//���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
				
		newThread = new Thread(new Runnable() {
		    @Override
	            public void run() {

	            	newtime = Gettime.main(null);//����д�����߳���Ҫ���Ĺ���
					//��סͬ��ʱ��
	            	if(newtime != null)
	            	{
						Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						//editor.putInt("ref_id", 1);
						editor.commit();
						//����һ��SQLiteHelper����
				        DatabaseHelper helper = new DatabaseHelper(Main_menu.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        SQLiteDatabase db = helper.getWritableDatabase();
				        
				      //����һ����
				        db.execSQL("create table if not exists locid (" +
				                    "_id integer primary key,"				                    
				                    +"loc_id text not null," 
				                    +"String text not null)");
				        
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
				        
				        if(db.rawQuery("select * from locid", null).moveToNext() == false)
				        {
				        //����ͬ�����ܼ�¼
					        db.execSQL("insert into locid (loc_id,String) values ('loc_id','001')");
					        db.execSQL("insert into locid (loc_id,String) values ('loc_id','002')");
					        db.execSQL("insert into locid (loc_id,String) values ('loc_id','003')");
				        }

				        db.close();
	            	}
	            	else{
	            		Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
	            		t.setToNow(); // ȡ��ϵͳʱ�䡣  
	            		int year = t.year;  
	            		int month = t.month + 1;  
	            		int date = t.monthDay;  
	            		newtime = getString(year)+"-"+getString(month)+"-"+getString(date);
	            		Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						editor.commit();
						//����һ��SQLiteHelper����
				        DatabaseHelper helper = new DatabaseHelper(Main_menu.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        SQLiteDatabase db = helper.getWritableDatabase();
				        
				      //����һ����
				        db.execSQL("create table if not exists locid (" +
				                    "_id integer primary key,"				                    
				                    +"loc_id text not null," 
				                    +"String text not null)");
				        
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
				        
				        db.close();
				        
	            	}
	            	
	            	
	            	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
	            	handler.sendEmptyMessage(0x001);
	            }
        	},"gettime");
		newThread.start(); //�����߳�
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x001) {
				login_dialog_progress_line.setVisibility(View.GONE);
				fahuo_main.setClickable(true);
				qita_main.setClickable(true);
				sync_main.setClickable(true);
				back_main.setClickable(true);
			}
			else if(msg.what == 0x002)
			{

			}
		};
	};
	
	public void fahuo_main(View v)
	{
		startActivity( new Intent( Main_menu.this,
              com.dhl.Fahuo_menu.class));
	}
	
	public void other(View v)
	{
		startActivity( new Intent( Main_menu.this,
              com.others.Other_func.class));
	}
	
	public void sync(View v)
	{
		startActivity( new Intent( Main_menu.this,
              com.others.Sync.class));
	}

	public void back(View v)
	{
		finish();
	}
}
