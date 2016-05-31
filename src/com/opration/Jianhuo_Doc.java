package com.opration;

import com.dhl.Main_menu;
import com.login.DatabaseHelper;
import com.login.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import gettime.Gettime;

public class Jianhuo_Doc extends Activity{
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jianhuo_doc);
		
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
				        DatabaseHelper helper = new DatabaseHelper(Jianhuo_Doc.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        SQLiteDatabase db = helper.getWritableDatabase();
				        
				      //����һ����				        
				        db.execSQL("create table if not exists ptsdata "
				        		+"("			                    				                    
			                    +"ref_id integer primary key," 
			                    +"user_id text not null,"
			                    +"task_time timestamp not null default CURRENT_TIMESTAMP,"
			                    +"task_name text not null,"
			                    +"task_event text,"
			                    +"doc_id integer,"
			                    +"task_id integer,"
			                    +"loc_id integer,"
			                    +"box_id integer,"
			                    +"sku integer,"
			                    +"qty integer,"
			                    +"last_opt_id integer,"
			                    +"pushstate integer not null"
			                    + ")"
			                    );
				        				        
				        db.execSQL("insert into ptsdata (user_id,task_name,"
				        		+ "task_event,doc_id,last_opt_id,"
				        		+ "pushstate) "
				        		+ "values ("
				        		+ sp.getString("user_id", "")+","
				        		+ "'�ܼ�','ɨ��DOCID',"
				        		+ "413413,0,0)");
				        
				        //��ȡ�α����
				        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
				        if (queryResult.getColumnCount() != 0) {
				            //��ӡ���м�¼
				            while (queryResult.moveToNext()) {
				                Log.i("info", "user_id: " + queryResult.getInt(queryResult.getColumnIndex("user_id"))
				                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
				                        + " String: " + queryResult.getString(queryResult.getColumnIndex("task_event"))
				                        );
				            }				            
				        }
					      	//�ر��α����
				            queryResult.close();
				        //�ر����ݿ�
				        db.close();
	            	}
	            	else{
	            		Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
	            		t.setToNow(); // ȡ��ϵͳʱ�䡣  
	            		int year = t.year;  
	            		int month = t.month + 1;  
	            		int date = t.monthDay;  
	            		int hour = t.hour; // 0-23  
	            		int minute = t.minute;  
	            		int second = t.second;
	            		newtime = getString(year)+"-"+getString(month)+"-"+getString(date);
	            		Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						editor.commit();
						//����һ��SQLiteHelper����
				        DatabaseHelper helper = new DatabaseHelper(Jianhuo_Doc.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        SQLiteDatabase db = helper.getWritableDatabase();
				        
				      //����һ����				        
				        db.execSQL("create table if not exists ptsdata "
				        		+"("			                    				                    
			                    +"ref_id integer primary key," 
			                    +"user_id text not null,"
			                    +"task_time timestamp not null default CURRENT_TIMESTAMP,"
			                    +"task_name text not null,"
			                    +"task_event text,"
			                    +"doc_id integer,"
			                    +"task_id integer,"
			                    +"loc_id integer,"
			                    +"box_id integer,"
			                    +"sku integer,"
			                    +"qty integer,"
			                    +"last_opt_id integer,"
			                    +"pushstate integer not null"
			                    + ")"
			                    );
				        				        
				        db.execSQL("insert into ptsdata (user_id,task_time,task_name,"
				        		+ "task_event,doc_id,task_id,loc_id,box_id,sku,qty,last_opt_id,"
				        		+ "pushstate) "
				        		+ "values ("
				        		+ "'user01',"
				        		+ "'2016-05-30 11:12:25',"
				        		+ "'�ܼ�','ɨ��DOCID',"
				        		+ "413413,111111,111112,111113,2312343,342352,0,0)");
				        
				        //�ر����ݿ�
				        db.close();
				        
	            	}

	            }
        	},"zongjian_doc");
		newThread.start(); //�����߳�
		
	}
	
	public void DocID_ok(View v)
	{
		startActivity( new Intent( Jianhuo_Doc.this,
              com.opration.Jianhuo_Task.class));
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}

}
