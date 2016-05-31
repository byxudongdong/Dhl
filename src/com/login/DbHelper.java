package com.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.login.DbHelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
public class DbHelper extends Activity {
	
	DatabaseHelper dbsqlite;
	private SQLiteDatabase  db = null;
	private Cursor cursor = null;    
    //private SimpleCursorAdapter adapter = null;
    
    private ListView lv;
    private List<Map<String, Object>> data;
    MyAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbhelper);
        lv = (ListView)this.findViewById(R.id.lv);
        //获取将要绑定的数据设置到data中
        data = getData();
        adapter = new MyAdapter(this);
        lv.setAdapter(adapter);
        
        //每个程序都有自己的数据库
        //通过openOrCreateDatabase来打开或创建一个数据库,返回SQLiteDatabase对象
        /**
         *  openOrCreateDatabase(String name,int mode,SQLiteDatabase.CursorFactory factory)
         *  name: 数据库名
         *  mode: 数据库权限，MODE_PRIVATE为本应用程序私有，MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE分别为全局可读和可写。
         *  factory: 可以用来实例化一个cusor对象的工厂类
         */
        db = openOrCreateDatabase("user.db",MODE_PRIVATE,null);
        //创建一个表
        db.execSQL("create table if not exists userTb (" +
                    "_id integer primary key," +
                    "name text not null,age integer not null," +
                    "sex text not null)");
        //向表中插入记录
        db.execSQL("insert into userTb (name,age,sex) values ('张三',18,'女')");
        db.execSQL("insert into userTb (name,age,sex) values ('李四',19,'男')");
        db.execSQL("insert into userTb (name,age,sex) values ('王五',20,'女')");
        //dbsqlite.getWritableDatabase();
        //db= (new dbsqlite(getApplicationContext())).getWritableDatabase();    
        //Cursor为查询结果对象，类似于JDBC中的ResultSet
        Cursor queryResult = db.rawQuery("select * from userTb", null);
        if (queryResult != null) {
            while (queryResult.moveToNext()) {
                Log.i("info", "id: " + queryResult.getInt(queryResult.getColumnIndex("_id"))
                          + " 姓名: " + queryResult.getString(queryResult.getColumnIndex("name"))
                          + " 年龄: " + queryResult.getInt(queryResult.getColumnIndex("age"))
                          + " 性别: " + queryResult.getString(queryResult.getColumnIndex("sex")));
            }
            //关闭游标对象
            queryResult.close();
        }
        //关闭数据库
        db.close();
        
        //处理Item的点击事件
        lv.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Map<String, Object> getObject = data.get(position);	//通过position获取所点击的对象
        		//int infoId = getObject.;	//获取信息id
        		String infoTitle = (String) getObject.get("title");	//获取信息标题
        		String infoDetails = (String) getObject.get("info");	//获取信息详情
        		
        		//Toast显示测试
        		Toast.makeText(DbHelper.this, "信息ID:"+position+infoTitle,Toast.LENGTH_SHORT).show();
        	}
        });
        
      //长按菜单显示
        lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
        	public void onCreateContextMenu(ContextMenu conMenu, View view , ContextMenuInfo info) {
        		conMenu.setHeaderTitle("菜单");
        		conMenu.add(0, 0, 0, "条目一");
        		conMenu.add(0, 1, 1, "条目二");
        		conMenu.add(0, 2, 2, "条目三");
        	}
        });
    }
    
  //长按菜单处理函数
  	public boolean onContextItemSelected(MenuItem aItem) {
          AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)aItem.getMenuInfo();
          Map<String, Object> map;
          switch (aItem.getItemId()) {
               case 0:
            	 data.removeAll(data);
                 adapter.notifyDataSetChanged();
                 lv.invalidate();
              	 Toast.makeText(DbHelper.this, "你点击了条目一",Toast.LENGTH_SHORT).show();
              	 return true;
               case 1:
            	   map = new HashMap<String, Object>();
                   map.put("img", R.drawable.ic_launcher);
                   map.put("title", "跆拳道" + "add");
                   map.put("info", "快乐源于生活..." + "add");
                   data.add(0,map);
                   adapter.notifyDataSetChanged();
                   lv.invalidate();
              	 Toast.makeText(DbHelper.this, "你点击了条目二",Toast.LENGTH_SHORT).show();            
              	 return true;
               case 2:
            	   data.remove(0);
            	   adapter.notifyDataSetChanged();
                   lv.invalidate();
              	 Toast.makeText(DbHelper.this, "你点击了条目三",Toast.LENGTH_SHORT).show();
              	 return true;
          }
  		return false;
     }
                                                    
    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<88;i++)
        {
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.ic_launcher);
            map.put("title", "跆拳道" + i);
            map.put("info", "快乐源于生活..." + i);
            list.add(map);
        }
        return list;
    }
                                                    
    //ViewHolder静态类
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView info;
    }
                                                    
    public class MyAdapter extends BaseAdapter
    {   
        private LayoutInflater mInflater = null;
        private MyAdapter(Context context)
        {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }
        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }
                                                        
        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        public int counter=0;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.listitem, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.tv);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.img.setBackgroundResource((Integer)data.get(position).get("img"));
            holder.title.setText((String)data.get(position).get("title"));
            holder.info.setText((String)data.get(position).get("info"));
            Log.i("Count", String.valueOf(counter ++));                                                
            return convertView;
        }                                                      
    }
}