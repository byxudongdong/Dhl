package com.login;

import com.dhl.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private EditText userName, password;
	private CheckBox rem_pw, auto_login;
	private Button btn_login,btn_set;
	private ImageButton btnQuit;
    private String userNameValue,passwordValue;
    private LinearLayout login_dialog_progress_line;
	private SharedPreferences sp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
        //���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		userName = (EditText) findViewById(R.id.et_zh);
		password = (EditText) findViewById(R.id.et_mima);
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_set = (Button) findViewById(R.id.btn_set);
        btnQuit = (ImageButton)findViewById(R.id.img_btn);
        login_dialog_progress_line = (LinearLayout) findViewById(R.id.login_dialog_progress_line);
		login_dialog_progress_line.setVisibility(View.GONE);
        
		//�жϼ�ס�����ѡ���״̬
      if(sp.getBoolean("ISCHECK", false))
        {
    	  //����Ĭ���Ǽ�¼����״̬
          rem_pw.setChecked(true);
       	  userName.setText(sp.getString("USER_NAME", ""));    //��ȡ�������Ϣ
       	  password.setText(sp.getString("PASSWORD", ""));
       	  //�ж��Զ���½��ѡ��״̬
       	  if(sp.getBoolean("AUTO_ISCHECK", false))
       	  {
       		     //����Ĭ�����Զ���¼״̬
       		     auto_login.setChecked(true);
       		    //��ת����
				Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
				LoginActivity.this.startActivity(intent);
				
       	  }
        }
		
        btn_set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				final EditText et = new EditText(LoginActivity.this);
				et.setInputType(InputType.TYPE_CLASS_NUMBER);
	  			
				new AlertDialog.Builder(LoginActivity.this).setTitle("����Ա����")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(et)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					String input = et.getText().toString();
					if (input.equals("")) {
						Toast.makeText(getApplicationContext(), "���ݲ���Ϊ�գ�" + input, Toast.LENGTH_LONG).show();
					}
					else{
							if(input.equals("1234"))
							{
								//��ת����
								Intent intent = new Intent(LoginActivity.this,Setting.class);
								LoginActivity.this.startActivity(intent);
							}else {
								Toast.makeText(getApplicationContext(), "��֤ʧ�ܣ�" + input, Toast.LENGTH_SHORT).show();
								//finish();
							}						
						}
					}
					})
				.setNegativeButton("ȡ��", null)
				.show();

				
			}
		});
      
	    // ��¼�����¼�  ����Ĭ��Ϊ�û���Ϊ��123 ���룺1234
		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userNameValue = userName.getText().toString();
			    passwordValue = password.getText().toString();
			    
				if(userNameValue.equals("test")&&passwordValue.equals("123"))
				{
					Toast.makeText(LoginActivity.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();
					//��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ
					if(rem_pw.isChecked())
					{
					 //��ס�û��������롢
					  Editor editor = sp.edit();
					  editor.putString("USER_NAME", userNameValue);
					  editor.putString("PASSWORD",passwordValue);
					  editor.commit();
					}
					//��ת����
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					LoginActivity.this.startActivity(intent);
					//finish();
					
				}else{
					
					Toast.makeText(LoginActivity.this,"�û�����������������µ�¼", Toast.LENGTH_LONG).show();
				}
				
			}
		});

	    //������ס�����ѡ��ť�¼�
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (rem_pw.isChecked()) {
                    
					System.out.println("��ס������ѡ��");
					sp.edit().putBoolean("ISCHECK", true).commit();
					
				}else {
					
					System.out.println("��ס����û��ѡ��");
					sp.edit().putBoolean("ISCHECK", false).commit();
					
				}

			}
		});
		
		//�����Զ���¼��ѡ���¼�
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (auto_login.isChecked()) {
					System.out.println("�Զ���¼��ѡ��");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("�Զ���¼û��ѡ��");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
		
		btnQuit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	protected void onNewIntent(Intent intent) 
	{
		
	}
}