package com.login;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//DatabaseHelper��Ϊһ������SQLite�������࣬�ṩ��������Ĺ��ܣ�
//��һ��getReadableDatabase(),getWritableDatabase()���Ի��SQLiteDatabse����ͨ���ö�����Զ����ݿ���в���
//�ڶ����ṩ��onCreate()��onUpgrade()�����ص����������������ڴ������������ݿ�ʱ�������Լ��Ĳ���

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	//��SQLiteOepnHelper�����൱�У������иù��캯��
    /**
     *  context:�����Ķ���
     *  name:���ݿ���
     */
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		//����ͨ��super���ø��൱�еĹ��캯��
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	//�ú������ڵ�һ�δ������ݿ��ʱ��ִ��,ʵ�������ڵ�һ�εõ�SQLiteDatabse�����ʱ�򣬲Ż�����������
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		//execSQL��������ִ��SQL���
		//db.execSQL("create table user(id int,name varchar(20))");
		 //����
//        db.execSQL("create table if not exists userTb (" +
//                "_id integer primary key," +
//                "name text not null,age integer not null," +
//                "sex text not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a Database");
	}
	
	public void add_data()
	{
		
	}
	
	/**
     * ��������(����1W�����ݺ�ʱ��1365ms)
     * @param openHelper
     * @param list
     * @return
     */ 
//    public static boolean insertBySql(SQLiteOpenHelper openHelper, 
//            List<RemoteAppInfo> list) { 
//        if (null == openHelper || null == list || list.size() <= 0) { 
//            return false; 
//        } 
//        SQLiteDatabase db = null; 
//        try { 
//            db = openHelper.getWritableDatabase(); 
//            String sql = "insert into " + RemoteDBHelper.TABLE_APP_REMOTE + "(" 
//                    + RemoteDBHelper.COL_PKG_NAME + ","// ���� 
//                    + RemoteDBHelper.COL_USER_ACCOUNT + ","// �˺� 
//                    + RemoteDBHelper.COL_APP_SOURCE + ","// ��Դ 
//                    + RemoteDBHelper.COL_SOURCE_UNIQUE + ","// PC mac ��ַ 
//                    + RemoteDBHelper.COL_MOBILE_UNIQUE + ","// �ֻ�Ψһ��ʶ 
//                    + RemoteDBHelper.COL_IMEI + ","// �ֻ�IMEI 
//                    + RemoteDBHelper.COL_INSTALL_STATUS + ","// ��װ״̬ 
//                    + RemoteDBHelper.COL_TRANSFER_RESULT + ","// ����״̬ 
//                    + RemoteDBHelper.COL_REMOTE_RECORD_ID // Ψһ��ʶ 
//                    + ") " + "values(?,?,?,?,?,?,?,?,?)"; 
//            SQLiteStatement stat = db.compileStatement(sql); 
//            db.beginTransaction(); 
//            for (RemoteAppInfo remoteAppInfo : list) { 
//                stat.bindString(1, remoteAppInfo.getPkgName()); 
//                stat.bindString(2, remoteAppInfo.getAccount()); 
//                stat.bindLong(3, remoteAppInfo.getFrom()); 
//                stat.bindString(4, remoteAppInfo.getFromDeviceMd5()); 
//                stat.bindString(5, remoteAppInfo.getMoblieMd5()); 
//                stat.bindString(6, remoteAppInfo.getImei()); 
//                stat.bindLong(7, remoteAppInfo.getInstallStatus()); 
//                stat.bindLong(8, remoteAppInfo.getTransferResult()); 
//                stat.bindString(9, remoteAppInfo.getRecordId()); 
//                long result = stat.executeInsert(); 
//                if (result < 0) { 
//                    return false; 
//                } 
//            } 
//            db.setTransactionSuccessful(); 
//        } catch (Exception e) { 
//            e.printStackTrace(); 
//            return false; 
//        } finally { 
//            try { 
//                if (null != db) { 
//                    db.endTransaction(); 
//                    db.close(); 
//                } 
//            } catch (Exception e) { 
//                e.printStackTrace(); 
//            } 
//        } 
//        return true; 
//    } 

}