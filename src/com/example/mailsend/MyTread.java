package com.example.mailsend;

import android.os.Handler;
import android.os.Message;


public class MyTread implements Runnable {

	Handler handler=new Handler();
	Runnable runnable=new Runnable() {
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub
	        //Ҫ��������
	        handler.postDelayed(this, 2000);
	    }
	};
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
            try {
                Thread.sleep(10000);// �߳���ͣ10�룬��λ����
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);// ������Ϣ
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}

}

