package com.stevenhu.lock;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class StarLockService extends Service implements MediaControl
{
	
	private static final boolean DBG = true;
	private static final String TAG = "FxLockService";
	private Intent mFxLockIntent = null;
	private KeyguardManager mKeyguardManager = null ;
	private KeyguardManager.KeyguardLock mKeyguardLock = null ;

	private MusicBroadcastReceiver mMBR;
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		refreshInfo();
		if (DBG)Log.d(TAG, "-->onCreate()");
		mFxLockIntent = new Intent(StarLockService.this, MainActivity.class);
		mFxLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		registerComponent();
	}
	
	
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		if(DBG) Log.d(TAG, "-->onStart()");
	}



	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(DBG) Log.d(TAG, "-->onDestroy()");
		unregisterComponent();
		//被销毁时启动自身，保持自身在后台存活
		startService(new Intent(StarLockService.this, StarLockService.class));
	}
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		if(DBG) Log.d(TAG, "-->onBind()");
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(DBG) Log.d(TAG, "-->onStartCommand()");
		return Service.START_STICKY;
	}
	
	//监听来自用户按Power键点亮点暗屏幕的广播
	private BroadcastReceiver mScreenOnOrOffReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(DBG) Log.d(TAG, "mScreenOffReceiver-->" + intent.getAction());
			
			if (intent.getAction().equals("android.intent.action.SCREEN_ON")
					|| intent.getAction().equals("android.intent.action.SCREEN_OFF"))
			{
				refreshInfo();
				mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
				mKeyguardLock = mKeyguardManager.newKeyguardLock("FxLock");
				//屏蔽手机内置的锁屏
				mKeyguardLock.disableKeyguard();
				//启动该第三方锁屏
				startActivity(mFxLockIntent);
			}	
			
			if(intent.getAction().equals("com.phicomm.hu.action.music"))
			{
				refreshInfo();
			}
		}
	};
	
	 //监听接收后台音乐服务MediaPlaybackService发送的广播，获取广播中携带的播放音乐信息(歌曲、歌手、专辑等)
	 private class MusicBroadcastReceiver extends BroadcastReceiver
	    {

			@Override
			public void onReceive(Context context, Intent intent) 
			{
				// TODO Auto-generated method stub
				if(DBG) Log.d(TAG, "MusicBroadcastReceiver-->" + intent.getAction());
				
				MusicInfo.setArtistName(intent.getStringExtra("artist"));
				//artistName = intent.getStringExtra("artist");
				MusicInfo.setMusicName(intent.getStringExtra("track"));
				//musicName = intent.getStringExtra("track");
				MusicInfo.setPlaying(intent.getBooleanExtra("playing", false));
				//playing = intent.getBooleanExtra("playing", playing);
				//String album = intent.getStringExtra("album");
				setMusicViewText();
				
				if(DBG) Log.d(TAG, "artistName-->" + MusicInfo.getArtistName());
				if(DBG) Log.d(TAG, "musicName-->" + MusicInfo.getMusicName());
				if(DBG) Log.d(TAG, "playing-->" + MusicInfo.isPlaying());
				//Log.d(TAG, "album-->" + album);
				
				//因为音乐后台服务发送的是粘性广播，所以接收后要删除，不然会保持
				removeStickyBroadcast(intent);
			}
	    	
	    }
	 
	//刷新音乐播放信息
	private void refreshInfo()
	{
		if (MusicInfo.isMusic())
		{
			setMusicViewText();
		}
		else
		{
			setLockViewText();
		}
	}
	
	//设置显示音乐播放信息
	private void setMusicViewText()
	{
		if(MusicInfo.getArtistName() != null 
				|| MusicInfo.getMusicName()!= null)
		{
			/*StringBuilder s = new StringBuilder();
			s.append(MusicInfo.getArtistName()).append("-").append(MusicInfo.getMusicName());
			MainActivity.mStatusViewManager.mArtistView.setText(s.toString());*/
			MainActivity.mStatusViewManager.mArtistView.setText(MusicInfo.getArtistName());
			MainActivity.mStatusViewManager.mMusicView.setText(MusicInfo.getMusicName());
		}
		else
		{
			MainActivity.mStatusViewManager.mArtistView.setText("no music to be chosen");
		}
		
	}
	
	//锁屏界面时隐藏音乐播放信息
	private void setLockViewText()
	{
		//MainActivity.mStatusViewManager.mArtistView.setText(null);
	}
	
	@Override
	public void connectMediaService()
	{
		// TODO Auto-generated method stub
		//MainActivity.mStatusViewManager.connectMediaService();
	}

    //注册广播监听
	@Override
	public void registerComponent()
	{
		// TODO Auto-generated method stub
		if(DBG) Log.d(TAG, "registerComponent()");
		IntentFilter mScreenOnOrOffFilter = new IntentFilter();
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_ON");
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_OFF");
		mScreenOnOrOffFilter.addAction(StarLockView.SHOW_MUSIC);
		StarLockService.this.registerReceiver(mScreenOnOrOffReceiver, mScreenOnOrOffFilter);
		
		/*if (mMBR == null)
		{   
			mMBR = new MusicBroadcastReceiver();
        	IntentFilter mFilter = new IntentFilter();
        	mFilter.addAction("com.android.music.playstatechanged"); 	
        	mFilter.addAction("com.android.music.metachanged");
        	mFilter.addAction("com.android.music.queuechanged");
        	mFilter.addAction("com.android.music.playbackcomplete"); 	
        	StarLockService.this.registerReceiver(mMBR, mFilter);
		}*/
	}

    //解除广播监听
	@Override
	public void unregisterComponent() 
	{
		// TODO Auto-generated method stub
		if(DBG) Log.d(TAG, "unregisterComponent()");
		if (mScreenOnOrOffReceiver != null)
		{
			StarLockService.this.unregisterReceiver(mScreenOnOrOffReceiver);
		}
		
		if (mMBR != null)
		{
			StarLockService.this.unregisterReceiver(mMBR);
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

}
