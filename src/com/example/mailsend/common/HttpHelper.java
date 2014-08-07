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

         HttpPost request = new HttpPost(SERVER_URL); // 根据内容来源地址创建一个Http请求   

         request.addHeader("Content-Type", "application/json; charset=utf-8");//必须要添加该Http头才能调用WebMethod时返回JSON数据
         JSONObject jsonParams=new JSONObject();
         HttpResponse httpResponse = new DefaultHttpClient().execute(request); // 发送请求并获取反馈      

         if (httpResponse.getStatusLine().getStatusCode() !=404)  //StatusCode为200表示与服务端连接成功，404为连接不成功 

         {           	 
                result = EntityUtils.toString(httpResponse.getEntity());  
                JSONObject resultJSON=new JSONObject(result).getJSONObject("d");//获取ModelUser类型的JSON对象
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
	 * 判断网络是否可用
	 */
	public boolean note_Internet(Context context){
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		
		if (networkinfo == null || !networkinfo.isAvailable()) {  
		    // 当前网络不可用   
			   new AlertDialog.Builder(context)
	   			.setMessage("请连接Internet")
	   			.setPositiveButton("确定",new DialogInterface.OnClickListener()
	   			{
	   				public void onClick(DialogInterface dialog, int which) {               				
	   				
	   					
	   			}
	   			}).show();
			   return false;
		    }
	/*	boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)  
		        .isConnectedOrConnecting();  
		    if (!wifi) { // 提示使用wifi   
		        Toast.makeText(context.getApplicationContext(), "建议您使用WIFI以减少流量！",  
		        Toast.LENGTH_SHORT).show();  
		    }  */
		    return true; 
	}
}
