package com.hust.iprai.wen.home;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jq.model.DownloadTask;
import org.jq.nbz.JQBaseActivity;
import org.jq.nbz.R;
import org.jq.nbz.RunPackageActivity;
import org.jq.nbz.SpreadActivity;

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
import android.widget.Toast;

public class LikeActivity extends JQBaseActivity {

	ProgressBar loading;
	ListView listview;
	DownLoadAdapter adapter;
	View app1,app2,app3,app4,spread;
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
		app1=findViewById(R.id.app1);
		app2=findViewById(R.id.app2);
		app3=findViewById(R.id.app3);
		app4=findViewById(R.id.app4);
		spread=findViewById(R.id.spread);
		OnClickListener appListener=new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(LikeActivity.this, "敬请期待", 0).show();
			}
		};
		app1.setOnClickListener(appListener);
		app2.setOnClickListener(appListener);
		app3.setOnClickListener(appListener);
		app4.setOnClickListener(appListener);
		spread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent();
				it.setClass(getParent(), SpreadActivity.class);
				startActivity(it);
			}
		});
	}

	public void setData() {
		adapter = new DownLoadAdapter();
		Static.share.downLoadAdapter=adapter;
		listview.setAdapter(adapter);
		loading.setVisibility(View.GONE);
	}

	public class DownLoadAdapter extends BaseAdapter {
		ArrayList<DownloadTask> dataArr=new ArrayList<DownloadTask>();
		public DownLoadAdapter() {
			refresh();
		}

		public void refresh() {
			dataArr.clear();
			List<PackageInfo> infoArr = scanInstalledPackage();
			for (DownloadTask task : Static.downTasks) {
				boolean installed=false;
				for (PackageInfo temp : infoArr) {
					if (temp.packageName.equals(task.packagename)) {
						installed=true;
						break;
					}
				}
				if(!installed){
					dataArr.add(task);
				}
				
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
			final DownloadTask data = dataArr.get(arg0);
			View v =null;//= arg1;
			if (v == null) {
				v = View.inflate(LikeActivity.this, R.layout.download_item,
						null);
			}
			TextView name = (TextView) v.findViewById(R.id.apkname);
			TextView info = (TextView) v.findViewById(R.id.info);
			ImageView icon = (ImageView) v.findViewById(R.id.appicon);
			final Button btn = (Button) v.findViewById(R.id.downbtn);
			final Button play =(Button)v.findViewById(R.id.play);
			if(data.downpercent<100){
				btn.setAlpha(1);
				play.setVisibility(View.GONE);
			}else{
				play.setVisibility(View.VISIBLE);
				btn.setAlpha(0);
			}
			final ProgressBar progress = (ProgressBar) v
					.findViewById(R.id.download_progressBar);
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
							data.downpercent=values[0];
							progress.setProgress(data.downpercent);
						}

						@Override
						protected void onPostExecute(String result) {
							data.apkPth=result;
							data.downpercent=100;
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
					Static.share.currentDownLoad=data;
					Static.share.isCurrentDownload=true;
					Intent it=new Intent();
					it.setClass(LikeActivity.this, RunPackageActivity.class);
					startActivityForResult(it, RunPackageActivity.TYPE_DOWNLOAD);
				}
			});
			info.setText("下载体验可得" + data.price * .5 + "元");
			name.setText(data.appname);
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
