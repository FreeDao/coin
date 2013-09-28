package org.jq.nbz;

import java.io.File;

import org.jq.model.WallpaperTask;

import util.Static;
import util.Tool;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hust.iprai.wen.TiebaActivity;
import com.stevenhu.lock.StarLockService;

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
				}			}
			Tool.saveObj(System.currentTimeMillis()+"", Static.wallTasks);
			return 0;
		}

		protected void onPostExecute(Integer result) {
			Intent it = new Intent();
			it.setClass(InitActivity.this, TiebaActivity.class);
			startActivity(it);
			finish();
		}
	};

}
