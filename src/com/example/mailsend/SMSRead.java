package com.example.mailsend;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SMSRead {
	
	Context content;
	private Uri SMS_INBOX = Uri.parse("content://sms"); 	
	public void getSmsFromPhone()
	{	 
		ContentResolver cr = content.getContentResolver();  
		String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
		
		 String where = " address = '15957495110' AND date >  "  
	                + (System.currentTimeMillis() - 10 * 60 * 1000);  
		 Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");  
		 
		 if (null == cur)
			 return;
		 
		 if (cur.moveToNext())
		 {
			 	String number = cur.getString(cur.getColumnIndex("address"));//手机号  
	            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表  
	            String body = cur.getString(cur.getColumnIndex("body"));  
	          //这里我是要获取自己短信服务号码中的验证码~~  
	            Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");  
	            Matcher matcher = pattern.matcher(body);  
	            if (matcher.find()) {  
	                String res = matcher.group().substring(1, 11);  
	                Toast.makeText(content, "默认Toast样式",
	                	     Toast.LENGTH_SHORT).show();
	               // mobileText.setText(res);  
	            }  
	            //这里我是要获取自己短信服务号码中的验证码~~  
		 }
	}
/*	public List<SmsEntity> doReadSMS(String phoneNumber,String name)
	{
		Cursor SmsCursor = null;
		List<SmsEntity> data = new ArrayList<SmsEntity>(); //SmsEntity是一个短信消息实体
		SmsCursor = managedQuery(Uri.parse("content://sms/inbox"),new String[]{
			"id","thread_id","address","body","date","read"},"address=?",new String[]{"86"+phoneNumber},"date desc");
		Log.v("AAAAAA",SmsCursor+":"+phoneNumber);
		
		if(SmsCursor !=null){
			SmsCursor.moveToFirst();
			if(!(SmsCursor.getCount()<=0)) //判断短信数量
			{
				//得到相关的thread_id去查指定的目标的所发短信
				String thread_id = SmsCursor.getString(SmsCursor.getColumnIndex("thread_id"));
				SmsCursor = managedQuery(Uri.parse("content://sms/"),new String[]{
					"_id","thread_id","address","body","date","read","type"},"thread_id=?",
					new String[]{thread_id},"date_desc");		
			}
		}		
	}
*/
}
