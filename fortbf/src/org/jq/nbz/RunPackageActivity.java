package org.jq.nbz;

import java.util.List;

import org.jq.model.Httpres;

import util.Static;
import util.Tool;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class RunPackageActivity extends Activity {

	public static int TYPE_DOWNLOAD = 0;
	public static int TYPE_SIGN = 1;

	ImageButton playBtn;
	boolean isListening = false;
	long time = 0;
	long starttime = 0;
	Thread t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_package);
		initView();
		if (t != null) {
			try {
				t.stop();
			} finally {
				t = null;
			}
		}
		time = 0;
		starttime = 0;
		isListening = false;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isListening", isListening);
		outState.putLong("time", time);
		outState.putLong("starttime", starttime);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		isListening = savedInstanceState.getBoolean("isListening", false);
		time = savedInstanceState.getLong("time", 0);
		starttime = savedInstanceState.getLong("starttime", 0);

	}

	public void startListen() {
		isListening = true;
		final ActivityManager am = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		t = new Thread() {
			public void run() {
				while (isListening) {
					List<RunningAppProcessInfo> apps = am
							.getRunningAppProcesses();
					for (RunningAppProcessInfo info : apps) {
						if (info.processName
								.equals(Static.share.currentDownLoad.packagename)) {
							if (time == 0) {
								starttime = System.currentTimeMillis();
							}
							time += 5;
							break;
						}
					}
					Log.e("qq", "check: "
							+ Static.share.currentDownLoad.packagename);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};
		t.start();
	}

	private void initView() {
		playBtn = (ImageButton) findViewById(R.id.play);
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Tool.install(RunPackageActivity.this,
						Static.share.currentDownLoad.apkPth);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isListening) {
			isListening = false;
			long time1 = time * 1000;
			long time2 = starttime > 0 ? System.currentTimeMillis() - starttime
					: 0;
			if (Math.abs(time2 - time1) < 10000) {
				if (Math.max(time1, time2) >= Static.share.currentDownLoad.playtime * 1000) {
					Static.downTasks.remove(Static.share.currentDownLoad);
					Static.share.downLoadAdapter.refresh();
					Static.share.downLoadAdapter.notifyDataSetChanged();
					addDownload.execute(Static.share.currentDownLoad.packagename);
				}
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (t == null) {
			startListen();
		}
	}
	
	AsyncTask<String, Void, Httpres> addDownload=new AsyncTask<String, Void, Httpres>(){
		
		@Override
		protected Httpres doInBackground(String... params) {
			return Static.addDownload.run(params[0]);
		}
		
		protected void onPostExecute(Httpres result) {
			if(result.code==0){
				Toast.makeText(getApplicationContext(), "成功获得奖励", 0).show();
				finish();
			}
		}
		
	};
}
