package com.example.mailsend.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;
import org.json.JSONArray; 
import org.json.JSONException; 
import org.json.JSONObject; 

import com.example.mailsend.Splash;

public class HttpHelper {

	public String getConnect()
    {
		String result = "";
    
        try{
       	       
        	   final String SERVER_URL = "http://10.0.2.2:9341/GetRule.asmx/GetInfo";

         HttpPost request = new HttpPost(SERVER_URL); // ����������Դ��ַ����һ��Http����   

         request.addHeader("Content-Type", "application/json; charset=utf-8");//����Ҫ��Ӹ�Httpͷ���ܵ���WebMethodʱ����JSON����
         JSONObject jsonParams=new JSONObject();
         HttpResponse httpResponse = new DefaultHttpClient().execute(request); // �������󲢻�ȡ����      

         if (httpResponse.getStatusLine().getStatusCode() !=404)  //StatusCodeΪ200��ʾ���������ӳɹ���404Ϊ���Ӳ��ɹ� 

         {           	 
                result = EntityUtils.toString(httpResponse.getEntity());  
                JSONObject resultJSON=new JSONObject(result).getJSONObject("d");//��ȡModelUser���͵�JSON����
                 result = resultJSON.getString("AimNum1").toString();
          //      return result;
               //  final  SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
         }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
        }
       return result;
        
    }

	/*
	 * �ж������Ƿ����
	 */
	public boolean note_Internet(Context context){
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		
		if (networkinfo == null || !networkinfo.isAvailable()) {  
		    // ��ǰ���粻����   
			   new AlertDialog.Builder(context)
	   			.setMessage("������Internet")
	   			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener()
	   			{
	   				public void onClick(DialogInterface dialog, int which) {               				
	   				
	   					
	   			}
	   			}).show();
			   return false;
		    }
	/*	boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)  
		        .isConnectedOrConnecting();  
		    if (!wifi) { // ��ʾʹ��wifi   
		        Toast.makeText(context.getApplicationContext(), "������ʹ��WIFI�Լ���������",  
		        Toast.LENGTH_SHORT).show();  
		    }  */
		    return true; 
	}
}
