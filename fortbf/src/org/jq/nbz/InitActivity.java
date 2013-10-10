package org.jq.nbz;

import java.io.File;

import org.jq.model.DownloadTask;
import org.jq.model.WallpaperTask;
import org.jq.zmdr.R;

import util.Static;
import util.Tool;
import util.Tool.DownCallBack;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dlnetwork.Dianle;
import com.hust.iprai.wen.TiebaActivity;
import com.miji.MijiConnect;
import com.newqm.sdkoffer.QuMiConnect;
import com.stevenhu.lock.StarLockService;
import com.winad.android.offers.AdManager;

public class InitActivity extends JQBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		Static.appContext = getApplicationContext();
		makeDir();
		checkNet.execute();
		// scanInstalledPackage();
		startService(new Intent(InitActivity.this, StarLockService.class));
		initThired();
	}

	// TODO adding apps
	public void initThired() {
		MijiConnect.requestConnect(this);

		Dianle.initDianleContext(this, "165974c7ee1d6b8943939423f1b9d740");
		Dianle.setCustomActivity("org.jq.nbz" + ".MDLActivity");
		Dianle.setCustomService("org.jq.nbz" + ".MDLService");

		AdManager.init(this);
		AdManager.setPointUnit(this, "金币");

		QuMiConnect.ConnectQuMi(this, "06eef2c7185e517d", "1382849c52e31709");
	}

	public void makeDir() {
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/zmdr");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		Static.loaclPth = dir.getAbsolutePath();
	}

	AsyncTask<Void, Void, Integer> checkNet = new AsyncTask<Void, Void, Integer>() {

		@Override
		protected Integer doInBackground(Void... params) {
			return Static.checkNet.run("");
		}

		protected void onPostExecute(Integer result) {
			if (result < 0) {
				Toast.makeText(InitActivity.this, "无法连接服务器，请检查网络", 1).show();
				runDelay(new Runnable() {

					@Override
					public void run() {
						finish();
					}
				}, 2000);
			} else if (Static.share.version != null
					&& (!Static.share.version.version
							.equals(getAppVersionName(InitActivity.this)))) {
				Log.e("qq", Static.share.version.version + ":"
						+ getAppVersionName(InitActivity.this));
				showUpdate();
			} else if (result == 0) {
				getTasks.execute();
			} else if (result == 1) {
				Intent it = new Intent();
				it.setClass(InitActivity.this, RegActivity.class);
				startActivity(it);
				finish();
			}
		};
	};

	AsyncTask<Void, Void, Integer> getTasks = new AsyncTask<Void, Void, Integer>() {

		@Override
		protected Integer doInBackground(Void... params) {
			Static.downTasks = Static.getDownLoadTask.run("");
			Static.signTasks = Static.getSignTask.run("");
			Static.wallTasks = Static.getWallpaperTask.run("");
			for (WallpaperTask task : Static.wallTasks) {
				try {
					task.bindImage.execute();
				} catch (Exception e) {
					Log.e("qq", e.toString());
				}
			}
			Tool.saveObj(System.currentTimeMillis() + "", Static.wallTasks);
			return 0;
		}

		protected void onPostExecute(Integer result) {
			Intent it = new Intent();
			it.setClass(InitActivity.this, TiebaActivity.class);
			startActivity(it);
			finish();
		}
	};

	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	protected void showUpdate() {
		final DownloadTask data=new DownloadTask();
		data.apk=Static.share.version.url;
		View v=View.inflate(InitActivity.this, R.layout.download_item, null);
		TextView name = (TextView) v.findViewById(R.id.apkname);
		TextView info = (TextView) v.findViewById(R.id.info);
		ImageView icon = (ImageView) v.findViewById(R.id.appicon);
		final Button btn = (Button) v.findViewById(R.id.downbtn);
		name.setText("桌面达人 v"+Static.share.version.version);
		info.setText("小手一抖，话费到手");
		icon.setImageResource(R.drawable.ic_launcher);
		final ProgressBar progress = (ProgressBar) v
				.findViewById(R.id.download_progressBar);
		progress.setMax(100);
		progress.setProgress(data.downpercent);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn.setEnabled(false);
				btn.setText("下载中");
				AsyncTask<Void, Integer, String> down = new AsyncTask<Void, Integer, String>() {

					@Override
					protected String doInBackground(Void... params) {
						DownCallBack call = new DownCallBack() {

							@Override
							public void progress(Integer progress) {
								publishProgress(progress);
							}
						};
						return Tool.download(data.apk, call,true);
					}

					@Override
					protected void onProgressUpdate(Integer... values) {
						data.downpercent = values[0];
						progress.setProgress(data.downpercent);
					}

					@Override
					protected void onPostExecute(String result) {
						data.apkPth = result;
						data.downpercent = 100;
						progress.setProgress(data.downpercent);
						Tool.install(InitActivity.this, data.apkPth);
					}
				};
				down.execute();
			}

		});
		Builder alert=new AlertDialog.Builder(this).setTitle("有新版本啦！").setView(v);
		alert.setCancelable(false);
		alert.show();
	}
}
