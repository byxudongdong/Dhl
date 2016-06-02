/**
 * 
 */
package com.opration;

import com.login.DatabaseHelper;
import com.login.R;
import android.app.Activity;
import android.content.Context;
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
public class Fenjian_SKU extends Activity {
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //声明一个子线程    
	
	private EditText sku_id_data;
	private EditText count_data;
	
	private boolean record_flag =false;
	private boolean thread_flag =true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fenjian_sku);
		sku_id_data = (EditText)findViewById(R.id.sku_id_data);
		count_data = (EditText)findViewById(R.id.count_data);
		
		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);
		
		newThread = new Thread(new Runnable() {
		    @Override
	            public void run() {
		    		while(thread_flag)
		    		{
		    			if(record_flag)
		    			{
			            	newtime = sp.getString("NEW_TIME", null);//
							//获取同步时间
			            	if(newtime != null)
			            	{
								Editor editor = sp.edit();
								editor.putString("NEW_TIME", newtime);
								editor.commit();
								//创建一个SQLiteHelper对象
						        DatabaseHelper helper = new DatabaseHelper(Fenjian_SKU.this, newtime.substring(0,10) + ".db");
						        //使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
						        SQLiteDatabase db = helper.getWritableDatabase();
						        
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
						        				        
						        db.execSQL("insert into ptsdata (user_id,task_name,"
						        		+ "task_event,doc_id,"+"task_id,"+"loc_id,"+"sku,"+"qty,"
						        		+ "last_opt_id,"
						        		+ "pushstate) "
						        		+ "values ("
						        		+ "'"+sp.getString("user_id", "")+"'"+","
						        		+ "'分拣',"
						        		+ "'"
						        		+ "扫描SKU"+"-"+sp.getString("task_event", "")
						        		+ "',"
						        		+ sp.getInt("doc_id", 0)+","
						        		+ sp.getInt("task_id",0)+","
						        		+ "'"+sp.getString("loc_id", "")+"'"+","
						        		+ "'"+sp.getString("sku", "")+"'"+","
						        		+ sp.getInt("qty", 0)+","
						        		+ "0,0)");
						        
						        //获取游标对象
						        Cursor queryResult = db.rawQuery("select * from ptsdata", null);
						        if (queryResult.getColumnCount() != 0) {
						            //打印记录
						            while (queryResult.moveToNext()) {
						                Log.i("info", "user_id: " + queryResult.getString(queryResult.getColumnIndex("user_id"))
						                        + " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
						                        + " String: " + queryResult.getString(queryResult.getColumnIndex("sku"))
						                        );
						            }				            
						        }
							      	//关闭游标对象
						            queryResult.close();
						        //关闭数据库
						        db.close();
			            	}
			            	else{
			            		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
			            		t.setToNow(); // 取得系统时间。  
			            		int year = t.year;  
			            		int month = t.month + 1;  
			            		int date = t.monthDay;  
		
			            		newtime = getString(year)+"-"+getString(month)+"-"+getString(date);
			            		Editor editor = sp.edit();
								editor.putString("NEW_TIME", newtime);
								editor.commit();
								//创建一个SQLiteHelper对象
						        DatabaseHelper helper = new DatabaseHelper(Fenjian_SKU.this, newtime.substring(0,10) + ".db");
						        //使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
						        SQLiteDatabase db = helper.getWritableDatabase();
						        
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
						        				        
						        db.execSQL("insert into ptsdata (user_id,task_name,"
						        		+ "task_event,doc_id,"+"task_id,"+"loc_id,"+"sku,"+"qty,"
						        		+ "last_opt_id,"
						        		+ "pushstate) "
						        		+ "values ("
						        		+ "'"+sp.getString("user_id", "")+"'"+","
						        		+ "'分拣',"
						        		+ "'扫描SKU',"
						        		+ sp.getInt("doc_id", 0)+","
						        		+ sp.getInt("task_id",0)+","
						        		+ "'"+sp.getString("loc_id", "")+"'"+","
						        		+ "'"+sp.getString("sku", "")+"'"+","
						        		+ sp.getInt("qty", 0)+","
						        		+ "0,0)");
						        
						        //关闭数据库
						        db.close();						        
			            	}
			            	record_flag = false;
		    			}//if()
		    		}//while()
	            }
        	},"fenjian_sku");
		newThread.start();
	}
	
	public void sku_tijiao(View v)
	{
		if(!TextUtils.isEmpty(sku_id_data.getText().toString()) && !TextUtils.isEmpty(count_data.getText().toString() ))
		{
			Editor editor = sp.edit();
			editor.putString("task_event", "sorting");
			editor.putString("sku",  sku_id_data.getText().toString() ); //Integer.parseInt()
			editor.putInt("qty", Integer.parseInt( count_data.getText().toString() ));
			editor.commit();
			
			record_flag = true;
		}

	}	
	
	public void sku_back(View v)
	{
		finish();
	}
	

}
