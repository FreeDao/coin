package com.hust.iprai.wen.home;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jq.model.SignTask;
import org.jq.nbz.JQBaseActivity;
import org.jq.nbz.R;
import org.jq.nbz.RunPackageActivity;

import util.ImageBinder;
import util.Static;
import util.Tool;
import util.Tool.DownCallBack;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MarkActivity extends JQBaseActivity {

	ProgressBar loading;
	ListView listview;
	SignAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.like_activity);
		initView();
		setData();
	}

	public void initView() {
		loading = (ProgressBar) findViewById(R.id.loading);
		listview = (ListView) findViewById(R.id.listview);
	}

	public void setData() {
		adapter = new SignAdapter();
		Static.share.signAdapter = adapter;
		listview.setAdapter(adapter);
		loading.setVisibility(View.GONE);
	}

	public class SignAdapter extends BaseAdapter {
		ArrayList<SignTask> dataArr = new ArrayList<SignTask>();

		public SignAdapter() {
			refresh();
		}

		public void refresh() {
			dataArr.clear();
			List<PackageInfo> infoArr = scanInstalledPackage();
			for (SignTask task : Static.signTasks) {
				boolean installed = false;
				for (PackageInfo temp : infoArr) {
					if (temp.packageName.equals(task.packagename)) {
						installed = true;
						break;
					}
				}
				task.hasInstalled = installed;
				dataArr.add(task);

			}
		}

		@Override
		public int getCount() {
			return dataArr.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataArr.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final SignTask data = dataArr.get(arg0);
			View v = null;// = arg1;
			if (v == null) {
				v = View.inflate(MarkActivity.this, R.layout.sign_item, null);
			}
			TextView name = (TextView) v.findViewById(R.id.apkname);
			TextView info = (TextView) v.findViewById(R.id.info);
			ImageView icon = (ImageView) v.findViewById(R.id.appicon);
			final Button btn = (Button) v.findViewById(R.id.downbtn);
			final Button play = (Button) v.findViewById(R.id.play);
			final ProgressBar progress = (ProgressBar) v
					.findViewById(R.id.download_progressBar);
			if (!data.hasInstalled) {
				if (data.downpercent < 100) {
					btn.setAlpha(1);
					play.setVisibility(View.GONE);
				} else {
					play.setVisibility(View.VISIBLE);
					btn.setAlpha(0);
				}
			} else {
				play.setVisibility(View.VISIBLE);
				btn.setAlpha(0);
				progress.setAlpha(0);
			}

			progress.setMax(100);
			progress.setProgress(data.downpercent);
			btn.setText("下载");
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
							return Tool.download(data.apk, call);
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
							play.setVisibility(View.VISIBLE);
							btn.setAlpha(0);
						}
					};
					down.execute();
				}

			});
			play.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Static.share.currentSignTask = data;
					Static.share.isCurrentDownload = false;
					Intent it = new Intent();
					it.setClass(MarkActivity.this, RunPackageActivity.class);
					startActivityForResult(it, RunPackageActivity.TYPE_DOWNLOAD);
				}
			});
			info.setText("第2、5天签到有奖励！");
			if (data.intro != null) {
				name.setText(data.appname+" "+data.intro);
			} else {
				name.setText(data.appname);
			}
			if (data.iconPth == null) {
				new ImageBinder(icon, data).execute();
			} else {
				icon.setImageURI(Uri.fromFile(new File(data.iconPth)));
			}

			return v;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("qq", "post now!");
	}
}
