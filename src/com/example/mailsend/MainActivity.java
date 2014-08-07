package com.example.mailsend;

import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String[] m = {"��ѡ����������","��","۴��","����","����"};
	private Spinner spinner;
	private TextView txtview ;
	private ArrayAdapter<String> adapter;
	private String area;
	private String AimVersion;
	private final String fileVer = "ver.ctf";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		spinner = (Spinner) findViewById(R.id.spinner1);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);		
		txtview = (TextView) findViewById(R.id.textArea);
		txtview.setTextColor(android.graphics.Color.RED);
		txtview.setVisibility(View.INVISIBLE);
		final	SharedPreferences ruleInfo = getSharedPreferences("rule_info", MODE_PRIVATE);
		AimVersion = ruleInfo.getString("AimVersion", "");
	
		
		//����¼�����
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		//����Ĭ��ֵ
		spinner.setVisibility(View.VISIBLE);
		
		String version = getVersionCode();
		if(!AimVersion.equals(version))
    	{
			UpdateManager upm = new UpdateManager(this);
			upm.checkUpdateInfo();
    	}
		
			
			

	}
	
	/**
	 * �˵�����ֵ��Ӧ
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode ==KeyEvent.KEYCODE_BACK)
		{
			exitBy2Click();  //˫���˳�����
		}
		return false;
	}
	
	private static Boolean isExit = false;
	
	private void exitBy2Click()
	{
		Timer tExit = null;
		if (isExit == false)
		{
			isExit = true;//׼���˳�
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask(){
				@Override
				public void run() {
					isExit = false; //ȡ���˳�
				}
				
			}, 2000);
		}
		else
		{
			finish();
			System.exit(0);
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
	
	public void nextView(View view)
	{
	if(area == "��ѡ����������"){
		txtview.setText("��ѡ����������");
		txtview.setVisibility(View.VISIBLE);
	}
	else
	{
		Intent intent = new Intent();
		intent.setClass(this, MailShow.class);
		Bundle bundle = new Bundle();
		bundle.putString("area", area);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	
	}

	
	class SpinnerSelectedListener implements OnItemSelectedListener
	{
		 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,			
				                 long arg3) {
				       //     txtview.setText("��ѡ��������ǣ�"+m[arg2]);
				            area = m[arg2];
			 			
				         }
				  
				         public void onNothingSelected(AdapterView<?> arg0) {
				         }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
