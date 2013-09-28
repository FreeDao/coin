package com.stevenhu.lock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.jq.model.Httpres;
import org.jq.model.WallpaperTask;
import org.jq.nbz.R;

import util.Static;
import util.Tool;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	private static final boolean DBG = true;
	private static final String TAG = "MainActivity";
	public static final int MSG_LAUNCH_HOME = 0;
	public static final int MSG_LAUNCH_DIAL = 1;
	public static final int MSG_LAUNCH_SMS = 2;
	public static final int MSG_LAUNCH_CAMERA = 3;

	private StarLockView mLockView;
	public static StatusViewManager mStatusViewManager;
	public WallpaperTask wtask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DBG)
			Log.d(TAG, "onCreate()");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initViews();
		mStatusViewManager = new StatusViewManager(this,
				this.getApplicationContext());
		// 启动音乐服务
		mStatusViewManager.connectMediaService();
		startService(new Intent(MainActivity.this, StarLockService.class));
		mLockView.setMainHandler(mHandler);
	}

	// 接收来自StarLockView发送的消息，处理解锁、启动相关应用的功能
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_LAUNCH_HOME:
				if (wtask != null) {
					AsyncTask<Void, Void, Httpres> task = new AsyncTask<Void, Void, Httpres>() {
						@Override
						protected Httpres doInBackground(Void... params) {
							return Static.addWallpaper.run(new String[] {
									wtask.name, "1" });
						}
					};
					task.execute();
				}
				finish();
				break;
			case MSG_LAUNCH_SMS:
				launchSms();
				finish();
				break;
			case MSG_LAUNCH_DIAL:
				launchDial();
				finish();
				break;
			case MSG_LAUNCH_CAMERA:
				if (wtask != null) {
					AsyncTask<Void, Void, Httpres> task2 = new AsyncTask<Void, Void, Httpres>() {
						@Override
						protected Httpres doInBackground(Void... params) {
							return Static.addWallpaper.run(new String[] {
									wtask.name, "0" });
						}
					};
					task2.execute();
				}
				launchCamera();
				finish();
				break;
			}
		}

	};

	// 启动短信应用
	private void launchSms() {

		// mFocusView.setVisibility(View.GONE);
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.android.mms",
				"com.android.mms.ui.ConversationList");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.VIEW");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(intent);
	}

	// 启动拨号应用
	private void launchDial() {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(intent);
	}

	// 启动相机应用
	private void launchCamera() {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.android.camera",
				"com.android.camera.Camera");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.VIEW");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(intent);
	}

	// 使home物理键失效
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		// this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		super.onAttachedToWindow();
	}

	// 使back键，音量加减键失效
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return disableKeycode(keyCode, event);
	}

	private boolean disableKeycode(int keyCode, KeyEvent event) {
		int key = event.getKeyCode();
		switch (key) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (DBG)
			Log.d(TAG, "onDestroy()");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (DBG)
			Log.d(TAG, "onResume()");
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		if (DBG)
			Log.d(TAG, "onDetachedFromWindow()");
		// 解除监听
		mStatusViewManager.unregisterComponent();
	}

	@SuppressWarnings("unchecked")
	public void initViews() {
		boolean useDefault = false;
		ArrayList<WallpaperTask> cachedTask = new ArrayList<WallpaperTask>();
		AsyncTask<Void, Void, Integer> getTasks = new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				Static.wallTasks = Static.getWallpaperTask.run("");
				for (WallpaperTask task : Static.wallTasks) {
					task.bindImage.execute();
				}
				Tool.saveObj(System.currentTimeMillis() + "", Static.wallTasks);
				return 0;
			}
		};
		AsyncTask<Void, Void, Integer> sycImg = new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				for (WallpaperTask task : Static.wallTasks) {
					try {
						task.bindImage.execute();
					} catch (Exception e) {
						Log.e("qq", e.toString());
					}
				}
				return 0;
			}
		};

		View main = View.inflate(this, R.layout.main, null);

		int[] arr = new int[] { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3,
				R.drawable.bg4, R.drawable.bg5 };

		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/zmdr/data");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File[] farr = dir.listFiles();
		if (farr.length > 0) {
			String max = farr[0].getAbsolutePath();
			for (File temp : farr) {
				if (temp.getAbsolutePath().compareTo(max) > 0) {
					max = temp.getAbsolutePath();
				}
			}
			File f = new File(max);
			String fname = f.getName();
			if (Static.wallTasks == null) {
				Static.wallTasks = (ArrayList<WallpaperTask>) Tool
						.readObj(fname);
			}
			if (System.currentTimeMillis() - Long.parseLong(fname) > 10L * 60 * 1000) {
				getTasks.execute();
			}
			for (WallpaperTask temp : Static.wallTasks) {
				temp.imgPth = WallpaperTask.getCachedIcon(temp.image);
				if (temp.imgPth != null) {
					cachedTask.add(temp);
				}
			}
			if (cachedTask.size() < 1) {
				useDefault = true;
			}
		} else {
			useDefault = true;
			getTasks.execute();
		}
		if (!useDefault) {
			useDefault = Math.random() < .4;
		}
		if (useDefault) {
			int index = (int) (Math.random() * arr.length);
			main.setBackgroundDrawable(getResources().getDrawable(arr[index]));
		} else {
			int index = (int) (Math.random() * cachedTask.size());
			wtask = cachedTask.get(index);
			try {
				main.setBackground(Drawable.createFromStream(
						new FileInputStream(wtask.imgPth), wtask.imgPth));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			sycImg.execute();

		}
		setContentView(main);
		mLockView = (StarLockView) findViewById(R.id.FxView);
		if (wtask != null && !useDefault) {
			if (wtask.leftprice >= 0.01) {
				mLockView.leftTxt.setText("+" + wtask.leftprice);
			} else {
				mLockView.leftTxt.setText("free");
			}
			if (wtask.rightprice >= 0.01) {
				mLockView.rightTxt.setText("+" + wtask.rightprice);
			} else {
				mLockView.rightTxt.setText("free");
			}
		}
	}

}