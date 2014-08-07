package com.example.mailsend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MailShow extends Activity {
	
	private Button send_sms_button;
	private Button refresh_button;
	private Button join_button;
	private Button btn_open_url;
	private String phone_number;
	private String phone_number_2;
	private String sms_content_2;
	private String sms_content;
	private String str_rule;
	//private TimeCount time;
	private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	private Uri COLOR_SMS = Uri.parse("content://mms/inbox");  //�����ռ���
	private Uri COLOR_SMS_PART = Uri.parse("content//mms/part");  //���Ÿ�����
	private static int count = 0;
	private TextView result_txt;
	private static int init_sms_id = 0 ;
	private static int last_sms_id = 0;
	
	
	Intent intent;
	Handler handler=new Handler();
	Runnable runnable=new Runnable() {
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub
	        //Ҫ��������
	    	count ++;	    	
	    last_sms_id = getSmsId();
	    if (last_sms_id > init_sms_id)
	    {
	    	result_txt.setText(getSmsAndSendBack());
	    }
	    else
	    {
	    	result_txt.setText("���Ŷ�ȡ�ɹ�����δ���ܵ�����");
	    	   handler.postDelayed(this, 3000); 
	    }
	    
	     
	  
	    }
	};
	
	public void getData()
	{
	final	SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
	    phone_number = ruleInfo.getString("AimNum1","");
	    phone_number_2 = ruleInfo.getString("AimNum2", "");	    
	    sms_content = ruleInfo.getString("SmsContent1", "");
	    sms_content_2 = ruleInfo.getString("SmsContent2","");
	    str_rule = ruleInfo.getString("ruleContent", "");
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mailshow);
		
		getData();
//		time = new TimeCount(60000, 1000);//����CountDownTimer����
		
		//��ȡ���ݹ����Ĳ���
		intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String area = bundle.getString("area");
		
		TextView tv = new TextView(this);
		
		
		result_txt = (TextView)findViewById(R.id.txtResult);

		
		send_sms_button = (Button)findViewById(R.id.btnSendAgn);
		refresh_button = (Button)findViewById(R.id.btnRead);
//		sms_edit_text = (EditText)findViewById(R.id.editText1);
		join_button = (Button)findViewById(R.id.btnJion);
		btn_open_url = (Button)findViewById(R.id.btnShop);
	//	btn_open_url.setEnabled(false);
		join_button.setEnabled(false);
	//	send_sms_button.setEnabled(false);
		handler.postDelayed(runnable, 3000);
		init_sms_id = getSmsId();
		TimeCount time = new TimeCount(50000,1000);
		time.start();
						
		
		send_sms_button.setOnClickListener(new OnClickListener(){		
			public void onClick(View arg0)
			{
			//	String phone_num = phone_number;
			//	String sms_content = "hello";
				
				if(phone_number.equals(""))
				{
					
				}
				else
				{
					
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone_number, null, sms_content, null, null);
					TimeCount time = new TimeCount(50000,1000);
					time.start();
				}
			}		
		});
		
		refresh_button.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0)
			{			
			//  runnable.run();	
				result_txt.setText(getSmsAndSendBack());
			//	result_txt.setText(phone_number);
			//	handler.postDelayed(runnable, 2000);
				//sms_edit_text.setText("hello!!!");
				//result_txt.setText("11111111");
			}
		});
		
		btn_open_url.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
				//���Ա���ֵ��������
				Intent intent = new Intent();
				intent.setData(Uri.parse("http://item.taobao.com/item.htm?id=21788655537"));
				intent.setAction(Intent.ACTION_VIEW);
				MailShow.this.startActivity(intent); //���������
			}
		});
		
		join_button.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
				
				Intent it = new Intent();
				it.setClass(MailShow.this, Attention.class);
				startActivity(it);
				
				//ȷ�ϲμӣ������Ի���ȴ��û�ȷ��
//				new AlertDialog.Builder(MailShow.this)
//				.setMessage(str_rule)
//				.setPositiveButton("ͬ��",new DialogInterface.OnClickListener()
//				{
//					public void onClick(DialogInterface dialog, int which) {               				
//						SendSmsjoin();			
//						dialog.dismiss();
//						Intent it = new Intent();
//						it.setClass(MailShow.this, GetQr.class);
//						startActivity(it);
//				}
//				}).setNegativeButton("��ͬ��", new DialogInterface.OnClickListener(){ 
//					public void onClick(DialogInterface dialog,int which)
//					{
//						System.exit(0);
//					}
//				}).show();
			}
		});
		
		
		
	SendSms();//������֤����
		
		
		
	//	getSmsFromPhone();
		
	}
	

	
	public String getSmsAndSendBack()
	{	 
		ContentResolver cr = this.getContentResolver();  
		String[] projection = new String[] {"_id","address","person","body","date" };
		StringBuilder str = new StringBuilder();
		String result ="";

		try
		{
			Cursor myCursor = getContentResolver().query(Uri.parse("content://sms/inbox"),
				      projection,
				      "address=?", new String[] {"+86"+phone_number} , "date desc");
			
			if(myCursor.moveToFirst()){
				String name;
				String phoneNumber;
				String smsbody;
				String date;
				String type;
				String smsid;
				int smsbodyColumn = myCursor.getColumnIndex("body");
				int smsidColumn = myCursor.getColumnIndex("_id");
			//	do{
					smsbody = myCursor.getString(smsbodyColumn);
					smsid = myCursor.getString(smsidColumn);
				//}while(myCursor.moveToNext()); 
				last_sms_id = Integer.parseInt(smsid);
			    str.append(smsid);
				str.append("��������[");				
				str.append(smsbody);
				str.append("]");
				str.append("\n�������ݻ�ȡ�ɹ�!");
			}
	
//			str.append(processResults(myCursor,true));
			int index = str.indexOf("������");
			if (index !=-1){
				//���ܲμ�
				result = "��Ǹ�㲻�ܲμӻ";
			//	result_txt.setText("");
			}
			else
			{
				if(str.indexOf("���Բμ�")!=-1)
				{
					//���Բμ�
					result ="��ϲ����Բμӻ!";
					join_button.setEnabled(true);
				}
				else
				{
					result = "��Ǹ�㲻�ܲμӻ";
				}
				
			}
		
			
		}
		catch(SQLiteException ex)
		{
			//Log.d(LOG_TAG, ex.getMessage());
		}
	//	return str.toString();
		return result;
			
	}

	public int getSmsId(){
		ContentResolver cr = this.getContentResolver();  
		String[] projection = new String[] {"_id"};
		StringBuilder str = new StringBuilder();
		int result = 0;
		try
		{
			Cursor myCursor = getContentResolver().query(Uri.parse("content://sms/inbox"),
				      projection,
				      "address=?", new String[] { "+86"+phone_number} , "date desc");
			
			if(myCursor.moveToFirst()){
		
				String smsid;
				int smsbodyColumn = myCursor.getColumnIndex("body");
				int smsidColumn = myCursor.getColumnIndex("_id");
			//	do{
		
					smsid = myCursor.getString(smsidColumn);
				//}while(myCursor.moveToNext()); 
		     result = Integer.parseInt(smsid);
	
		
			}
			

		}
		catch(SQLiteException ex)
		{
			//Log.d(LOG_TAG, ex.getMessage());
		}
		return result;
	}
	
	private StringBuilder processResults(Cursor cur, boolean all) {
		   // TODO Auto-generated method stub
		   StringBuilder str=new StringBuilder();
		   if (cur.moveToFirst()) {

		         String name; 
		         String phoneNumber;       
		         String sms;
		         		         
		         int nameColumn = cur.getColumnIndex("person");
		         int phoneColumn = cur.getColumnIndex("address");
		         int smsColumn = cur.getColumnIndex("body");
		         
		         do {
		             // Get the field values
		             name = cur.getString(nameColumn);             
		             phoneNumber = cur.getString(phoneColumn);
		             sms = cur.getString(smsColumn);
		             
		             str.append("{");
		        //     str.append(name+",");
		             str.append(phoneNumber+",");
		             str.append(sms);
		             str.append("}");
		             

		             
		             if (null==sms)
		             sms="";
		             		            
		                          
		         } while (cur.moveToNext());

		     }
		     else
		     {
		     str.append("no result!");

		     }

		   return str;
		}//processResults
		
	private void SendSms(){
//		String phone_num = "15957495110";
//		String sms_content = "CX";
		if(phone_number.equals(""))
		{
			
		}
		else
		{
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phone_number, null, sms_content, null, null);
			
		}
	}
	
	private void SendSmsjoin(){
//		String phone_num = "15957495110";
	//	String sms_content = "100574476*1234";
		if (phone_number_2.equals("")){
			
		}
		else
		{
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phone_number_2, null, sms_content_2, null, null);
		}
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}
		@Override
		public void onFinish() {//��ʱ���ʱ����
		send_sms_button.setText("���·��Ͷ���");
		send_sms_button.setEnabled(true);
		send_sms_button.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//��ʱ������ʾ
		send_sms_button.setEnabled(false);
		send_sms_button.setClickable(false);
		send_sms_button.setText("���·��Ͷ���("+millisUntilFinished /1000+")��");
		}
		}
	

	
}
