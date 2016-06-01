/**
 * 
 */
package com.opration;

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
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * @author 
 *
 */
public class Jianhuo_Task extends Activity {
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //����һ�����߳�    
	
	private EditText task_id_data;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opration_task);
		task_id_data = (EditText)findViewById(R.id.task_id_data);
		
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
				        DatabaseHelper helper = new DatabaseHelper(Jianhuo_Task.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        SQLiteDatabase db = helper.getWritableDatabase();
				        
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
				        				        
				        db.execSQL("insert into ptsdata (user_id,task_name,"
				        		+ "task_event,doc_id,"+"task_id,"
				        		+ "last_opt_id,"
				        		+ "pushstate) "
				        		+ "values ("
				        		+ sp.getString("user_id", "")+","
				        		+ "'�ܼ�','ɨ��TASKID',"
				        		+ sp.getInt("doc_id", 0)+","
				        		+ sp.getInt("task_id",0)+","
				        		+ "0,0)");
				        
				        //��ȡ�α����
				        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
				        if (queryResult.getColumnCount() != 0) {
				            //��ӡ��¼
				            while (queryResult.moveToNext()) {
				                Log.i("info", "user_id: " + queryResult.getInt(queryResult.getColumnIndex("user_id"))
				                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
				                        + " String: " + queryResult.getInt(queryResult.getColumnIndex("task_id"))
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

	            		newtime = getString(year)+"-"+getString(month)+"-"+getString(date);
	            		Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						editor.commit();
						//����һ��SQLiteHelper����
				        DatabaseHelper helper = new DatabaseHelper(Jianhuo_Task.this, newtime.substring(0,10) + ".db");
				        //ʹ��getWritableDatabase()��getReadableDatabase()�������SQLiteDatabase����
				        SQLiteDatabase db = helper.getWritableDatabase();
				        
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
				        				        
				        db.execSQL("insert into ptsdata (user_id,task_name,"
				        		+ "task_event,doc_id,"+"task_id,"
				        		+ "last_opt_id,"
				        		+ "pushstate) "
				        		+ "values ("
				        		+ "'"+sp.getString("user_id", "")+"'"+","
				        		+ "'�ܼ�','ɨ��TASKID',"
				        		+ sp.getInt("doc_id", 0)+","
				        		+ sp.getInt("task_id",0)+","
				        		+ "0,0)");
				        
				        //�ر����ݿ�
				        db.close();
				        
	            	}
	            }
        	},"zongjian_task");
		
	}
	
	public void opration_task(View v)
	{	if(!TextUtils.isEmpty(task_id_data.getText()) )
		{
			Editor editor = sp.edit();
			editor.putInt("task_id", Integer.parseInt( task_id_data.getText().toString() ));
			editor.commit();
			newThread.start(); //�����߳�
			
			startActivity( new Intent( Jianhuo_Task.this,
	              com.opration.Jianhuo_huowei.class));
		}
	}
	
	public void jianhuo_back(View v)
	{
		finish();
	}
	

}
