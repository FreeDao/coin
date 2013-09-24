package com.hust.iprai.wen.home;

import java.io.File;

import org.jq.model.DownloadTask;
import org.jq.nbz.JQBaseActivity;
import org.jq.nbz.R;

import util.ImageBinder;
import util.Static;
import util.Tool;
import util.Tool.DownCallBack;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LikeActivity extends JQBaseActivity {
	
	ProgressBar loading;
	ListView listview;
	DownLoadAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.like_activity);
		initView();
		setData();
	}
	
	public void initView(){
		loading=(ProgressBar) findViewById(R.id.loading);
		listview=(ListView)findViewById(R.id.listview);
	}
	
	public void setData(){
		adapter=new DownLoadAdapter();
		listview.setAdapter(adapter);
		loading.setVisibility(View.GONE);
	}
	
	class DownLoadAdapter extends BaseAdapter{
		
		
		@Override
		public int getCount() {
			return Static.downTasks.size();
		}

		@Override
		public Object getItem(int arg0) {
			return Static.downTasks.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final DownloadTask data=Static.downTasks.get(arg0);
			View v=arg1;
			if(v==null){
				v=View.inflate(LikeActivity.this, R.layout.download_item, null);
			}
			TextView name=(TextView) v.findViewById(R.id.apkname);
			TextView info=(TextView)v.findViewById(R.id.info);
			ImageView icon=(ImageView)v.findViewById(R.id.appicon);
			final Button btn=(Button)v.findViewById(R.id.downbtn);
			final ProgressBar progress=(ProgressBar)v.findViewById(R.id.download_progressBar);
			progress.setMax(100);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					btn.setEnabled(false);
					btn.setText("下载中");
					AsyncTask<Void, Integer, String> down=new AsyncTask<Void, Integer, String>(){

						@Override
						protected String doInBackground(Void... params) {
							DownCallBack call=new DownCallBack() {
								
								@Override
								public void progress(Integer progress) {
									publishProgress(progress);
								}
							};
							return Tool.download(data.apk, call);
						}
						
						@Override
						protected void onProgressUpdate(Integer... values) {
							progress.setProgress(values[0]);
						}
						
						@Override
						protected void onPostExecute(String result) {
							progress.setProgress(100);
							btn.setText("立即体验");
						}
					};
					down.execute();
				}
				
			});
			info.setText("下载体验可得"+data.price*.5+"元");
			name.setText(data.appname);
			if(data.iconPth==null){
				new ImageBinder(icon, data).execute();
			}else{
				icon.setImageURI(Uri.fromFile(new File(data.iconPth)));
			}
			return v;
		}
		
	}
}
