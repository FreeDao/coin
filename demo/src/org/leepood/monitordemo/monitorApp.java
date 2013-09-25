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
	public static final String ACTION="org.leepood.monitordemo.APPS_CHANGED";//自定义动作
	private ActivityManager am=null;
	private HashMap<String,Integer> appsStored=null;//保存开启和关闭的程序进程名称列表
	private final int STARTED_APP=0;//刚开机的程序标记为0
	private final int CLOSED_APP=1;//刚关闭的程序标记为1
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
				 * 获取正在运行程序进程名称列表
				 */
				List<RunningAppProcessInfo> oldrunningapps=am.getRunningAppProcesses();//获取运行的程序
				List<String>oldrunningappsprocessnameList=new ArrayList<String>();//保存所有正在运行程序的进程名称
				for(RunningAppProcessInfo old:oldrunningapps)
				{
					oldrunningappsprocessnameList.add(old.processName);
					Log.i("old", old.processName);
				}
				
				
				
				try {
					Thread.sleep(1000);//休眠一段时间
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				/**
				 * 再次获取所有正在运行的程序名称列表
				 */
				List<RunningAppProcessInfo> newrunningapps=am.getRunningAppProcesses();//再次获取运行的程序
				List<String>newrunningappsprocessnameList=new ArrayList<String>();//再次保存所有正在运行程序的进程名称
				
				for(RunningAppProcessInfo newapp:newrunningapps)
				{
					newrunningappsprocessnameList.add(newapp.processName);
					//Log.i("new", newapp.processName);
				}
				/**
				 * 开始比对
				 */
				for(String newapp:newrunningappsprocessnameList)
				{
					//如果新获取的程序在原来获取的程序列表中则该程序没有变化，否则该程序为刚启动
					if(!oldrunningappsprocessnameList.contains(newapp))
					{
						appsStored.put(newapp,STARTED_APP);
						Log.i("newstart",newapp);
					}
				}
				for(String oldapp:oldrunningappsprocessnameList)
				{
					//如果以前获取的程序在刚刚获取的程序列表中则该程序没有变化，否则该程序为刚关闭
					if(!newrunningappsprocessnameList.contains(oldapp))
					{
						appsStored.put(oldapp,CLOSED_APP);
						Log.i("newclose", oldapp);
					}
					
				}
				//发出广播
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
