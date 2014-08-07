package com.example.mailsend;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.widget.Toast;
import android.app.AlertDialog;

public class SMSReceiver extends BroadcastReceiver {

	// action ����
    String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED" ;
    
	@Override
	public void onReceive(Context context, Intent intent) {		
		// TODO Auto-generated method stub
		
		 if (intent.getAction().equals( SMS_RECEIVED )) {
	           // ��ش��� : ����任���������㡢�������ţ�
	       }
		System.out.println("sms receiver");  
		Bundle bundle = intent.getExtras();
		Object[] puObj = (Object[])bundle.get("pdus");  
		SmsMessage[] messages = new SmsMessage[puObj.length];
		 System.out.println(messages.length);  
		 for (int i = 0; i < puObj.length; i++) {  
	            // ʹ��Object�еĶ��󴴽�SmsMessage����  
	            messages[i] = SmsMessage.createFromPdu((byte[])puObj[i]);  
	            //��ʼʹ��SmsMessage�����еķ���������Ϣ����  
	            System.out.println("content: "+messages[i].getDisplayMessageBody()+"   address:"+messages[i].getDisplayOriginatingAddress());  
	        } 
		         //����һ��Toast
		 
		 System.exit(0);
		      
		  

	}

}
