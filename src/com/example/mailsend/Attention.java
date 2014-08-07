package com.example.mailsend;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Attention extends Activity {

	private String phone_number;
	private String phone_number_2;
	private String sms_content_2;
	private String sms_content;
	private String str_rule;
	private Button btnAgree;
	private Button btnDisAgree;
	private TextView txtRule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_attention);
		getData();
		btnAgree = (Button)findViewById(R.id.btnGet);
		btnDisAgree = (Button)findViewById(R.id.btnDisagree);
		txtRule = (TextView)findViewById(R.id.txtRule);
		
		txtRule.setText(str_rule);
		//ЭЌвт
		btnAgree.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0)
			{
				SendSmsjoin();			
				Intent it = new Intent();
				it.setClass(Attention.this, GetQr.class);
				startActivity(it);
			}
		});
		
		btnDisAgree.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0)
			{
				Attention.this.finish();
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attention, menu);
		return true;
	}
	
	public void getData()
	{
	final	SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
	    phone_number = ruleInfo.getString("AimNum1","");
	    phone_number_2 = ruleInfo.getString("AimNum2", "");	    
	    sms_content = ruleInfo.getString("SmsContent1", "");
	    sms_content_2 = ruleInfo.getString("SmsContent2","");
	    str_rule = ruleInfo.getString("ruleContent", "");
	}
	
	private void SendSmsjoin(){
		if (phone_number_2.equals("")){
			
		}
		else
		{
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phone_number_2, null, sms_content_2, null, null);
		}
	}
	

}
