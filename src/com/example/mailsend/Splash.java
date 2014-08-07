package com.example.mailsend;

import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.example.mailsend.MainActivity;
import com.example.mailsend.R;
import com.example.mailsend.Splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import com.example.mailsend.common.HttpHelper;

public class Splash extends Activity {
	
	 private final int SPLASH_DISPLAY_LENGHT = 3000;  
	 private static String AimNum1;
	 private static boolean i;
	 public static DefaultHttpClient httpClient ;
	 private  UpdateManager mUpdateManager;
	 private final String fileVer = "ver.ctf";
	 private String AimVersion;
	 
	 
	 Handler handler = new Handler()
		{
			public void handlerMessage(Message msg)
			{
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String val = data.getString("value");
			}
		};
		
		Runnable runnable1 = new Runnable(){
			public void run(){
				 String Result = requestPost();
				
				 Message msg = new Message();
			        Bundle data = new Bundle();
			       
			        if(Result!=""&&Result!="ERROR")
					{
						configCommit(Result);
					}
					else
					{
						String temp = "";
						if(Result == "")
						{
							temp = "Զ�̷���˷�������Ϊ��";
						}
						else if(Result =="ERROR"){
							temp = "Զ�̷����δ��Ӧ";
						}
						
						 new AlertDialog.Builder(Splash.this)
							.setMessage(temp)
							.setPositiveButton("ȷ��",new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which) {               				

									
							}
							}).show();
					}
					
					if(i == true &&Result!=""&&Result!="ERROR")
					{
						new Handler().postDelayed(new Runnable(){  
				            @Override  
				            public void run() {  
				                // TODO Auto-generated method stub  
				            	//�ж��Ƿ���Ҫ����
//				            	String version = getVersionCode();
//				            	if(!AimVersion.equals(version))
//				            	{
//				        		UpdateManager upm = new UpdateManager(Splash.this);
//				        		upm.checkUpdateInfo();
//				        		
//				        						        		 
//				            	}
//				            	else
//				            	{
//				            		//�жϸ���
//					                Intent mainIntent = new Intent(Splash.this, MainActivity.class);  
//					                Splash.this.startActivity(mainIntent);  
//					                Splash.this.finish();  
//				            	}
				            	  Intent mainIntent = new Intent(Splash.this, MainMenu.class);  
					                Splash.this.startActivity(mainIntent);  
					                Splash.this.finish();  
				        		
				        	
				            }  
				        }, SPLASH_DISPLAY_LENGHT);  
					}

				
			
			}
		};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
//		
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_splash);
		HttpHelper hp = new HttpHelper();
		
		i = hp.note_Internet(this);
		
		
		
		new Thread(runnable1).run();
		
		
		//�ж�����״��
		

	
		
	
	}

	public static DefaultHttpClient getHttpClient(){
		//��ȡ�̰߳�ȫ��httpClient�����������վ���ҵ��ģ���֪���Է�
		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
			ClientConnectionManager connMgr = httpClient.getConnectionManager();
			HttpParams params = httpClient.getParams();
			ThreadSafeClientConnManager safeConnMgr = new ThreadSafeClientConnManager(params, connMgr.getSchemeRegistry());
			httpClient = new DefaultHttpClient(safeConnMgr, params);
		}
		return httpClient;
	}
	
	public void initView(){
		final  SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
		 AimNum1 = ruleInfo.getString("AimNum1", "");
		 
		 if(AimNum1 == ""){
			 //��ȡRule
		
		 
		 }
		 else
		 {
			 new AlertDialog.Builder(Splash.this)
				.setMessage(AimNum1)
				.setPositiveButton("ȷ��",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which) {               				

						
				}
				}).show();
		 }
	}
	
	public String getVersionCode()
	{
		String result ="";
		try {
			Properties properties = new Properties(); 
			
			InputStream stream = this.getAssets().open(fileVer); 
			properties.load(stream);
			result =  String.valueOf(properties.get("Version").toString()); 
		}
		catch(Exception e){
			e.printStackTrace();
		}
	return result;
		
	}
	
	private String requestPost()
	{
		String result = "";
   try{
	   
	   
	   
        final String SERVER_URL = "http://61.153.144.174:8089/GetRule.asmx/GetInfo";
         HttpPost request = new HttpPost(SERVER_URL); // ����������Դ��ַ����һ��Http����   

         request.addHeader("Content-Type", "application/json; charset=utf-8");//����Ҫ��Ӹ�Httpͷ���ܵ���WebMethodʱ����JSON����
         JSONObject jsonParams=new JSONObject();
         
         
         HttpResponse httpResponse =  getHttpClient().execute(request); // �������󲢻�ȡ����      
  //       client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
    //     client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

         if (httpResponse.getStatusLine().getStatusCode() !=404)  //StatusCodeΪ200��ʾ���������ӳɹ���404Ϊ���Ӳ��ɹ� 

         {           	 
                result = EntityUtils.toString(httpResponse.getEntity());  
               
                 return result;
              
         }
         else{
        	   new AlertDialog.Builder(Splash.this)
   			.setMessage("Զ�̷��������Ӧ����ϵ����Ա")
   			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener()
   			{
   				public void onClick(DialogInterface dialog, int which) {               				

   					
   			}
   			}).show();
         }
        }
   catch(Exception e)
   {
   	  Log.e("K", "Exception: "+Log.getStackTraceString(e));;
   	  result = "ERROR";
       
   }
		return result;
	}
	
	
	private void configCommit(String result)
	{
		try{
			JSONObject resultJSON=new JSONObject(result).getJSONObject("d");//��ȡModelUser���͵�JSON����
			final  SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
			 //  if(ruleInfo.getString("AimNum1", "")==""){
				   SharedPreferences.Editor editor = ruleInfo.edit();
            	 String AimNum1 = resultJSON.getString("AimNum1").toString();
            	 AimVersion= resultJSON.get("version").toString();
            	   editor.putString("AimVersion", AimVersion);
                   editor.putString("AimNum1", AimNum1);
                   editor.putString("AimNum2", resultJSON.getString("AimNum2").toString());
                   editor.putString("SmsContent1", resultJSON.getString("SmsContent1").toString());
                   editor.putString("SmsContent2", resultJSON.getString("SmsContent2").toString());
                   editor.putString("ruleContent", resultJSON.getString("ruleContent").toString());
                   editor.commit();
                   
               
                   //String temp2 = resultJSON.getString("AimNum1").toString();
             
           //    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void getConnect()
    {
		String result = "";
    
        try{
       	       
        	   final String SERVER_URL = "http://10.0.3.2:9341/GetRule.asmx/GetInfo";

         HttpPost request = new HttpPost(SERVER_URL); // ����������Դ��ַ����һ��Http����   

         request.addHeader("Content-Type", "application/json; charset=utf-8");//����Ҫ��Ӹ�Httpͷ���ܵ���WebMethodʱ����JSON����
         
         
         HttpResponse httpResponse =  getHttpClient().execute(request); // �������󲢻�ȡ����      

         if (httpResponse.getStatusLine().getStatusCode() !=404)  //StatusCodeΪ200��ʾ���������ӳɹ���404Ϊ���Ӳ��ɹ� 

         {           	 
                result = EntityUtils.toString(httpResponse.getEntity());  
                JSONObject resultJSON=new JSONObject(result).getJSONObject("d");//��ȡModelUser���͵�JSON����                    
                 //д��
                 final  SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
                 
                 if(ruleInfo.getString("AimNum1", "")==""){
                	 Editor editor = ruleInfo.edit();
                	 String AimNum1 = resultJSON.getString("AimNum1").toString();
                     editor.putString(AimNum1, "");
                     editor.putString(resultJSON.getString("AimNum2").toString(), "");
                     editor.putString(resultJSON.getString("SmsContent1").toString(), "");
                     editor.putString(resultJSON.getString("SmsContent2").toString(), "");
                     editor.putString(resultJSON.getString("ruleContent").toString(), "");
                     editor.commit();
                 }
                 
             
             
              
         }
         else{
        	   new AlertDialog.Builder(Splash.this)
   			.setMessage("Զ�̷��������Ӧ����ϵ����Ա")
   			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener()
   			{
   				public void onClick(DialogInterface dialog, int which) {               				

   					
   			}
   			}).show();
         }
        }
        catch(Exception e)
        {
        	  Log.e("K", "Exception: "+Log.getStackTraceString(e));;
            
        }
    //   return result;
        
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
