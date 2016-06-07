package com.gson;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UtilsPost {
	
	public static void doPost(String url, RequestParams params, final Handler handler) {
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("MyLog", "����ʧ��");
				Log.i("MyLog", arg1);
			}
		
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				// TODO Auto-generated method stub
				String data = info.result;//�����Ƿ���ֵ
				
				if(info.statusCode == 200){
					Log.i("MyLog", "���ͳɹ�");
				}
				Log.i("������", String.valueOf(info.statusCode));
				
				//Message message = new Message();
				//message.obj = data;
				//handler.sendMessage(message);
			}
		});
	}

}
