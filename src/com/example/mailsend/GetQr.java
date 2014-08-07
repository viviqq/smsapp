package com.example.mailsend;

import java.util.Hashtable;

import com.example.mailsend.MailShow.TimeCount;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GetQr extends Activity {
	
	private ImageView qrImgImageView;
	private Button btnSendSms;
	private Button btnReadSms;
	private ProgressBar progress;
	private TextView tv;
	private String phone_number;
	private String sms_content;
	private EditText sms_text;

	Handler handler=new Handler();
	Runnable runnable=new Runnable() {
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub
	        //要做的事情
	      //  handler.postDelayed(this, 2000);
	    	String smscontent = ReadSms();
	    	if (smscontent!="")
			{
				createImage(smscontent);
			}
	    	else
	    	{
	    		   handler.postDelayed(this, 3000); 
	    		   sms_text.setText(smscontent);
	    	}
	       // 
	    }
	};
	
	
	public void getData()
	{
		final  SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
	    phone_number = ruleInfo.getString("AimNum2","");
	    sms_content = ruleInfo.getString("SmsContent2", "");	   
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS );   
		setContentView(R.layout.qr);
		getData();
		qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);  
		btnSendSms = (Button) this.findViewById(R.id.btn_send_again);
		btnReadSms = (Button) this.findViewById(R.id.btnGetSms);
		progress = (ProgressBar)this.findViewById(R.id.progressBar1);
		tv = (TextView)this.findViewById(R.id.txtRule);
		sms_text = (EditText)this.findViewById(R.id.editText1);
		
	//	createImage("123");
		//btnSendSms.setEnabled(false);
		
		TimeCount time = new TimeCount(30000,1000);
		time.start();
		handler.postDelayed(runnable, 3000);
		
		btnReadSms.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
		//		btnReadSms.setEnabled(false);
				String sms = ReadSms();
				if (sms!="")
				{
					createImage(sms);
				}
				
			}
		});
		
		btnSendSms.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0)
			{
				progress.setVisibility(ProgressBar.GONE );
			//	runnable.run();
				SendSms();
				TimeCount time = new TimeCount(30000,1000);
				time.start();
			
			     
			}
		});
		
	}
	
	
	private void createImage(String text){
		int QR_WIDTH = 350;
		int QR_HEIGHT = 350;
				
		try
		{
			QRCodeWriter writer = new QRCodeWriter();
		//	String text = "12345678";
			//把输入的文本转化成二维码
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);
			
			System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());
			
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                    	
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            qrImgImageView.setImageBitmap(bitmap);
			
			
		}catch(WriterException e)
		{
			e.printStackTrace();
		}
	}

	
	private void SendSms()
	{
		//String phone_num = "15957495110";
	//	String sms_content = "GetQr";
		if(phone_number.equals(""))
		{
			
		}
		else
		{
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phone_number, null, sms_content, null, null);
		}
	}
	
	
	private String ReadSms(){
		ContentResolver cr = this.getContentResolver();  
		String[] projection = new String[] {"_id","address","person","body","date" };
		StringBuilder str = new StringBuilder();
		try
		{
			Cursor myCursor = getContentResolver().query(Uri.parse("content://sms/inbox"),
				      projection,
				      "address=?", new String[] { "+86"+phone_number} , "date desc");
			
			if(myCursor.moveToFirst()){
				String name;
				String phoneNumber;
				String smsbody;
				String date;
				String type;
				
				int smsbodyColumn = myCursor.getColumnIndex("body");
				
					smsbody = myCursor.getString(smsbodyColumn);
				
				str.append(smsbody);

			}
			
//			str.append(processResults(myCursor,true));

		}
		catch(SQLiteException ex)
		{
			//Log.d(LOG_TAG, ex.getMessage());
		}
	
		return str.toString().trim();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_qr, menu);
		return true;
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			btnSendSms.setText("重新获取二维码");
			btnSendSms.setClickable(true);
			btnSendSms.setEnabled(true);
			progress.setVisibility(ProgressBar.GONE );

		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示		
			btnSendSms.setEnabled(false);
			btnSendSms.setClickable(false);
			btnSendSms.setText("重新获取二维码("+millisUntilFinished /1000+")秒");
		}
		}
	

}
