package org.leepood.monitordemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class monitorApp extends Service {
	public static final String ACTION="org.leepood.monitordemo.APPS_CHANGED";//�Զ��嶯��
	private ActivityManager am=null;
	private HashMap<String,Integer> appsStored=null;//���濪���͹رյĳ�����������б�
	private final int STARTED_APP=0;//�տ����ĳ�����Ϊ0
	private final int CLOSED_APP=1;//�չرյĳ�����Ϊ1
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}

	
	@Override
	public void onCreate() {
		am=(ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		Log.i("service----->", "start");
	}


	@Override
	public void onStart(Intent intent, int startId) {
		Thread th_monitor=new Thread(new Runnable(){

			@Override
			public void run() {
				while(true)
				{
					appsStored=new HashMap<String,Integer>();
				/**
				 * ��ȡ�������г�����������б�
				 */
				List<RunningAppProcessInfo> oldrunningapps=am.getRunningAppProcesses();//��ȡ���еĳ���
				List<String>oldrunningappsprocessnameList=new ArrayList<String>();//���������������г���Ľ�������
				for(RunningAppProcessInfo old:oldrunningapps)
				{
					oldrunningappsprocessnameList.add(old.processName);
					Log.i("old", old.processName);
				}
				
				
				
				try {
					Thread.sleep(1000);//����һ��ʱ��
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				/**
				 * �ٴλ�ȡ�����������еĳ��������б�
				 */
				List<RunningAppProcessInfo> newrunningapps=am.getRunningAppProcesses();//�ٴλ�ȡ���еĳ���
				List<String>newrunningappsprocessnameList=new ArrayList<String>();//�ٴα��������������г���Ľ�������
				
				for(RunningAppProcessInfo newapp:newrunningapps)
				{
					newrunningappsprocessnameList.add(newapp.processName);
					//Log.i("new", newapp.processName);
				}
				/**
				 * ��ʼ�ȶ�
				 */
				for(String newapp:newrunningappsprocessnameList)
				{
					//����»�ȡ�ĳ�����ԭ����ȡ�ĳ����б�����ó���û�б仯������ó���Ϊ������
					if(!oldrunningappsprocessnameList.contains(newapp))
					{
						appsStored.put(newapp,STARTED_APP);
						Log.i("newstart",newapp);
					}
				}
				for(String oldapp:oldrunningappsprocessnameList)
				{
					//�����ǰ��ȡ�ĳ����ڸոջ�ȡ�ĳ����б�����ó���û�б仯������ó���Ϊ�չر�
					if(!newrunningappsprocessnameList.contains(oldapp))
					{
						appsStored.put(oldapp,CLOSED_APP);
						Log.i("newclose", oldapp);
					}
					
				}
				//�����㲥
				if(appsStored.size()!=0)
				{
					Intent intent=new Intent();
					Bundle bundle=new Bundle();
					bundle.putSerializable("app_info", appsStored);
					intent.putExtra("bundle", bundle);
					intent.setAction(ACTION);
					monitorApp.this.sendBroadcast(intent);
					Log.i("sendbroadcast", "true");
					appsStored=null;
				}
				}
				
			}
			
			
			
		});
		
		th_monitor.start();
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
