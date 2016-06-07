package com.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setting extends Activity{
	private Button set_timeout,set_service,set_back;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		//���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
				
		set_timeout = (Button)findViewById(R.id.set_timeout);
		set_service = (Button)findViewById(R.id.set_service);
		set_back = (Button)findViewById(R.id.set_back);
		
        set_timeout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				final EditText et = new EditText(Setting.this);
				et.setInputType(InputType.TYPE_CLASS_NUMBER);
				et.setHint(String.valueOf(sp.getInt("timeout", 10)));
	  			
				new AlertDialog.Builder(Setting.this).setTitle("���ó�ʱ�����ӣ�")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(et)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					String input = et.getText().toString();
					if (input.equals("")) {
						Toast.makeText(getApplicationContext(), "���ݲ���Ϊ�գ�" + input, Toast.LENGTH_LONG).show();
					}
					else {
	            		Editor editor = sp.edit();
						editor.putInt("timeout", Integer.parseInt(input));
						editor.commit();
						Toast.makeText(getApplicationContext(), "�ɹ����ó�ʱʱ��Ϊ��" + input +"����", Toast.LENGTH_LONG).show();

						}
					}
					})
				.setNegativeButton("ȡ��", null)
				.show();	
				
			}
		});
        
        set_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				final EditText et = new EditText(Setting.this);
				et.setHint(sp.getString("service", "http://117.185.79.178:8005/PTSService.asmx/PTS_DATA"));
	  			
				new AlertDialog.Builder(Setting.this).setTitle("���÷�������ַ")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(et)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					String input = et.getText().toString();
					if (input.equals("")) {
						Toast.makeText(getApplicationContext(), "���ݲ���Ϊ�գ�" + input, Toast.LENGTH_LONG).show();
					}
					else {
	            		Editor editor = sp.edit();
						editor.putString("service", input);
						editor.commit();
						Toast.makeText(getApplicationContext(), "�ɹ����÷�������ַΪ��" + input, Toast.LENGTH_LONG).show();
						
						}
					}
					})
				.setNegativeButton("ȡ��", null)
				.show();
				
			}
		});
        
        set_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}
		});
	}

}
