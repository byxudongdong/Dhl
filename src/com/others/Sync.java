package com.others;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.gson.SendJson;
import com.gson.Task;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
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
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class Sync extends Activity {
	DatabaseHelper helper;
	SQLiteDatabase db;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //声明一个子线程    
	private SharedPreferences sp;
	
	private Task sendtask;
	private List<Task> sendtasks = new ArrayList<Task>();;
	Boolean endFlag = false;
	Gson gson = new Gson();
	
	TextView textView1;
	static Boolean result = false;
	static String receiveFail;
	static int receiveSuccess;
	int firstCursor=0;
	int packSize = 3;
	int sendpackSize = 0;
	int totalCount = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync);
		textView1 = (TextView)findViewById(R.id.textView1);
		
		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);
		
		new Thread(sendData).start();
	}
	
	Runnable sendData = new Runnable() 
	{
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub
	    	Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
    		t.setToNow(); // 取得系统时间。  
    		int year = t.year;  
    		int month = t.month + 1;  
    		int date = t.monthDay;  
//    		int hour = t.hour; // 0-23  
//    		int minute = t.minute;  
//    		int second = t.second;
    		
    		newtime = String.valueOf(year)
    				+"-"+String.format("%02d",month)
            		+"-"+String.format("%02d",date);
    		
    		Editor editor = sp.edit();
			editor.putString("NEW_TIME", newtime);
			editor.commit();
			//创建一个SQLiteHelper对象
	        helper = new DatabaseHelper(Sync.this, newtime.substring(0,10) + ".db");
	        //使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
	        db = helper.getWritableDatabase();
	        
	      //表是否存在				        
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
	        //Cursor queryResult = db.rawQuery("select * from ptsdata", null);
	        Cursor queryResult = db.rawQuery("select * from ptsdata where pushstate=? limit ?,?", 
	        									new String[]{"0","0","3" });//String.valueOf(packSize)
	        int count=queryResult.getCount();
	        Log.i("取出条数", String.valueOf(count));
	        if (count > 0)
	        {
	        	endFlag = false;
	            //打印记录
	        	//queryResult.moveToPosition(queryResult.getColumnCount()-3);
	            while (queryResult.moveToNext() == true && sendtasks.size()<packSize) {
	            	
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
	            	
//	                Log.i("info", "user_id: " + user_id
//	                        + " timastamp: " + task_time
//	                        + " String: " + task_event
//	                        );
	                
			        sendtask = SendJson.main( String.valueOf(ref_id),  
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
			        sendtasks = SendJson.SendTasks(sendtasks, sendtask, endFlag);
			        sendpackSize++;
			        firstCursor = ref_id;
	            }				            
	        
		      	//关闭游标对象
	            queryResult.close();
	            //Json转换
				String TaskListJson = gson.toJson(sendtasks);  
				//System.out.println("list转化为json==" + TaskListJson); 
				
				String senddata = "{\"tasklist\":" + TaskListJson + "}"; 
				System.out.println("----------Json转化-------------");
				//System.out.println("list转化为json==" + senddata); 
				
				//设置传输参数。
			    RequestParams params = new RequestParams();					    
			    params.addBodyParameter("scanDataList", senddata);
			    String serviseUrl = sp.getString("service", "http://117.185.79.178:8005/PTSService.asmx/PTS_DATA");
			    doPost(serviseUrl,params, handler);
	        }
	    }
	};
	
	final Handler handler=new Handler()
	{		 		        
        @Override
        public void handleMessage(Message msg) {
           	super.handleMessage(msg);
          
           	switch(msg.what){
           	case 0:
           		sendtasks.clear(); //清空jsonlist          		
           		totalCount = totalCount + sendpackSize;  //总计上传成功条数
//           		sendpackSize = 0;	//发送包包含条数
           		textView1.setText("同步成功:"+totalCount+"条");
           		changePushState();
           		db.close();
           		new Thread(sendData).start();
        	    break;
           	case 1:
        	   	textView1.setText("同步失败，错误代码" + receiveFail );
                break;
           	}
        }		        
	};
	
	public void changePushState()			//修该已上传记录pushstate标志 ： 0未上传成功，1已上传成功
	{
		for(int i=firstCursor;i>firstCursor-sendpackSize;i--)
		{
			db.execSQL("update ptsdata set pushstate=? where ref_id=?",
				    new Object[]{1, i});
		}
		sendpackSize = 0;	//发送包包含条数
	}
	
	public void back(View v)
	{
		dialog();
	}
	
	protected void dialog() { 
		AlertDialog.Builder builder = new Builder(Sync.this); 
		builder.setMessage("确定要退出吗?"); 
		builder.setTitle("提示"); 
		builder.setPositiveButton("确认", 
			new android.content.DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) 
				{ 
					dialog.dismiss();
					Sync.this.finish();
				} 
			}
		); 
		builder.setNegativeButton("取消", 
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
		if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
			dialog(); 
			return false; 
		} 
		return super.onKeyDown(keyCode, event);
	}
	
	public static void doPost(String url, RequestParams params, final Handler handler) {
		
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("MyLog", "发送失败");
				Log.i("MyLog", arg1);
				receiveFail = arg1;
				result = false;		
				Message message=new Message();
		    	message.what=1;
		    	handler.sendMessage(message);
			}
		
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				// TODO Auto-generated method stub
				String data = info.result;//这里是返回值
				
				if(info.statusCode == 200){
					Log.i("MyLog", "发送成功");					
				}
				Log.i("请求结果", String.valueOf(info.statusCode));
				
				receiveSuccess = info.statusCode;
				result = true;
				Message message=new Message();
		    	message.what=0;
		    	handler.sendMessage(message);

			}
		});
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        //关闭数据库
        if(db.isOpen())
        {
        	db.close();
        }
    }
}
