package com.dhl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gson.Root;
import com.lidroid.xutils.util.LogUtils;
import com.login.DatabaseHelper;
import com.login.HttpUser;
import com.login.JavaBean;
import com.login.R;
import com.others.Wait_pandian;
import com.timeout.Timeout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import gettime.GetThisDay;
import gettime.Gettime;

public class Main_menu extends Activity{
	
	DatabaseHelper helper;
	SQLiteDatabase db;
	public Button fahuo_main;
	public Button qita_main;
	public Button sync_main;
	public Button back_main;
	private GridLayout main_menu_grid;
	private LinearLayout login_dialog_progress_line;
	private SharedPreferences sp;
	int newref_id;
	String newtime = null;
	String newtimeYesterday = null;
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

	            	//newtime = Gettime.main(null);//����д�����߳���Ҫ���Ĺ���
	        		Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
	        		t.setToNow(); // ȡ��ϵͳʱ�䡣  	            		            		
	        		
	        		int year = t.year;  
	        		int month = t.month + 1;  
	        		int date = t.monthDay; 
	        		int hour = t.hour; // 0-23  
	        		int minute = t.minute;  
	        		int second = t.second;
	        		
	        		newtime = String.valueOf(year)
	        				+"-"+String.format("%02d",month)
		            		+"-"+String.format("%02d",date)
		            		+"-"+String.format("%02d",hour)
		            		+"-"+String.format("%02d",minute)
		            		+"-"+String.format("%02d",second);
					//��סͬ��ʱ��
	        		
	            	if(newtime != null)
	            	{
						Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						//editor.putInt("ref_id", 1);
						editor.commit();
						//����һ��SQLiteHelper����
				        helper = new DatabaseHelper(Main_menu.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        db = helper.getWritableDatabase();
				        
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
				        String Url = sp.getString("locidservice", "http://aux.dhl.com/pts/interface/getLocIdList");
				        //String Url = "http://www.kuaidi100.com/query?type=shentong&postid=3307313264542";
				        //String getdata = HttpUser.getJsonContent(Url);  //�������ݵ�ַ
				        //Log.i("��������","json-lib��JSONת����:"+getdata);
				        
				        String s1 = "{\"loc_id\":[\"001\",\"002\",\"003\",\"004\",\"005\",\"006\",\"007\",\"008\",\"009\",\"010\"]}";
				        //System.out.println("JsonתΪ��Bean===" + s1); 
				    	//JSON���� ת JSONModel����
				    	Root result = JavaBean.getPerson(s1, com.gson.Root.class);
				    	if(result == null)
				    	{
				    		handler.sendEmptyMessage(0x002);
				    	}else{					    	
					    	//ת��String �������
					    	//Log.i("�����б�","json-lib��JSONת����:"+result.toString());
					    	
					        if(db.rawQuery("select * from locid", null).moveToNext() == false)
					        {
					        	Boolean opStyle = false;
					        	//����ͬ�����ܼ�¼
					        	if(opStyle==true)
					        	{
					        		LogUtils.i("��ʼ��������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
						        	for(int i=0;i<result.getLoc_id().size();i++)
						    		{
								        db.execSQL("insert into locid (loc_id,String) "
								        		+ "values ('loc_id','"+result.getLoc_id().get(i)+"')");
						    		}
						        	LogUtils.i("�����������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
						        	
					        	}else{	
						            LogUtils.i("��ʼ��������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
						            
						            db.beginTransaction();
						            String sql = "insert into locid (loc_id,String) values(?,?)";
						        	for (int i=0;i<result.getLoc_id().size();i++) {
						        		SQLiteStatement stat = db.compileStatement(sql);
						        		stat.bindString(1, "loc_id");
						        		stat.bindString(2, result.getLoc_id().get(i));
						        		stat.executeInsert();
						        	}
						        	db.setTransactionSuccessful();
						        	db.endTransaction();
						        	
						            LogUtils.i("�����������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
					        	}
					        	
					        }
				    	}

				        db.close();
	            	}
	            	else{
	            		Time t_1=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
	            		t_1.setToNow(); // ȡ��ϵͳʱ�䡣  	            		            		
	            		
	            		int year_1 = t_1.year;  
	            		int month_1 = t_1.month + 1;  
	            		int date_1 = t_1.monthDay; 
	            		int hour_1 = t_1.hour; // 0-23  
	            		int minute_1 = t_1.minute;  
	            		int second_1 = t_1.second;
	            		
	            		newtime = String.valueOf(year_1)
	            				+"-"+String.format("%02d",month_1)
			            		+"-"+String.format("%02d",date_1)
			            		+"-"+String.format("%02d",hour_1)
			            		+"-"+String.format("%02d",minute_1)
			            		+"-"+String.format("%02d",second_1);
	            		
	            		newtimeYesterday = GetThisDay.getSpecifiedDayBefore(newtime.toString());
	            		
	            		Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						editor.commit();
						//����һ��SQLiteHelper����
				        helper = new DatabaseHelper(Main_menu.this, newtime.substring(0,10) + ".db");
				        DatabaseHelper helper_Yseterday = new DatabaseHelper(Main_menu.this, newtimeYesterday.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        db = helper.getWritableDatabase();
				        SQLiteDatabase db_Yseterday = helper_Yseterday.getWritableDatabase();
				        
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
				        
				        String Url = sp.getString("locidservice", "http://aux.dhl.com/pts/interface/getLocIdList");
				        //String Url = "http://www.kuaidi100.com/query?type=shentong&postid=3307313264542";
				        String getdata = HttpUser.getJsonContent(Url);  //�������ݵ�ַ
				        //Log.i("��������","json-lib��JSONת����:"+getdata);
				        
				        //String s1 = "{\"loc_id\":[\"001\",\"002\",\"003\",\"004\",\"005\",\"006\",\"007\",\"008\",\"009\",\"010\"]}";
				        //System.out.println("JsonתΪ��Bean===" + s1); 
				    	//JSON���� ת JSONModel����
				    	Root result = JavaBean.getPerson(getdata, com.gson.Root.class);
				    	if(result == null)
				    	{
				    		handler.sendEmptyMessage(0x002);
				    	}
				    	//ת��String �������
				    	//Log.i("�����б�","json-lib��JSONת����:"+result.toString());
				    	
				        if(db.rawQuery("select * from locid", null).moveToNext() == false)
				        {
				        	Boolean opStyle = false;
				        	//����ͬ�����ܼ�¼
				        	if(opStyle==true)
				        	{
				        		LogUtils.i("��ʼ��������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
					        	for(int i=0;i<result.getLoc_id().size();i++)
					    		{
							        db.execSQL("insert into locid (loc_id,String) "
							        		+ "values ('loc_id','"+result.getLoc_id().get(i)+"')");
					    		}
					        	LogUtils.i("�����������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
					        	
				        	}else{	//��������
					            LogUtils.i("��ʼ��������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));					            
					            db.beginTransaction();
					            String sql = "insert into locid (loc_id,String) values(?,?)";
					            Cursor queryResult = db_Yseterday.rawQuery("select * from locid", null);
					            if (queryResult.getColumnCount() != 0)
					            {
						            while(queryResult.moveToNext()==true)
						            {
						        		SQLiteStatement stat = db.compileStatement(sql);
						        		stat.bindString(1, "loc_id");
						        		stat.bindString(2, queryResult.getString(queryResult.getColumnIndex("String")));
						        		stat.executeInsert();
						            }
						            db.setTransactionSuccessful();
						        	db.endTransaction();
					            }else{
					            	
					            }
//					            db.beginTransaction();
//					            String sql = "insert into locid (loc_id,String) values(?,?)";
//					        	for (int i=0;i<result.getLoc_id().size();i++) {
//					        		SQLiteStatement stat = db.compileStatement(sql);
//					        		stat.bindString(1, "loc_id");
//					        		stat.bindString(2, result.getLoc_id().get(i));
//					        		stat.executeInsert();
//					        	}
//					        	db.setTransactionSuccessful();
//					        	db.endTransaction();
					        	
					            LogUtils.i("�����������*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
				        	}
				        	
				        }

				        db.close();			        
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
	    		Toast.makeText(getApplicationContext(), "���»���ʧ�ܣ�", Toast.LENGTH_SHORT).show();
	    		finish();
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
		dialog();
		//finish();
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        //�ر����ݿ�
        if(db.isOpen())
        {
        	db.close();
        }
    }
	
	
	protected void dialog() { 
		AlertDialog.Builder builder = new Builder(Main_menu.this); 
		builder.setMessage("������ͬ�����ݣ�ȷ��Ҫ�˳�ҵ�������?"); 
		builder.setTitle("��ʾ"); 
		builder.setPositiveButton("ȷ��", 
			new android.content.DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) 
				{ 
					dialog.dismiss(); 
					
					Main_menu.this.finish(); 
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
			//dialog(); 
			dialog();
			return false; 
		} 
		return super.onKeyDown(keyCode, event);
	}
}
