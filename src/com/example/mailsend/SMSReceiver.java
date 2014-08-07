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

	// action 名称
    String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED" ;
    
	@Override
	public void onReceive(Context context, Intent intent) {		
		// TODO Auto-generated method stub
		
		 if (intent.getAction().equals( SMS_RECEIVED )) {
	           // 相关处理 : 地域变换、电量不足、来电来信；
	       }
		System.out.println("sms receiver");  
		Bundle bundle = intent.getExtras();
		Object[] puObj = (Object[])bundle.get("pdus");  
		SmsMessage[] messages = new SmsMessage[puObj.length];
		 System.out.println(messages.length);  
		 for (int i = 0; i < puObj.length; i++) {  
	            // 使用Object中的对象创建SmsMessage对象  
	            messages[i] = SmsMessage.createFromPdu((byte[])puObj[i]);  
	            //开始使用SmsMessage对象中的方法调用消息内容  
	            System.out.println("content: "+messages[i].getDisplayMessageBody()+"   address:"+messages[i].getDisplayOriginatingAddress());  
	        } 
		         //产生一个Toast
		 
		 System.exit(0);
		      
		  

	}

}
