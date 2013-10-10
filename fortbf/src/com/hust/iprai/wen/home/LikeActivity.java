package com.hust.iprai.wen.home;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jq.model.DownloadTask;
import org.jq.model.MockTask;
import org.jq.nbz.JQBaseActivity;
import org.jq.zmdr.R;
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
import android.text.Html;
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

import com.dlnetwork.Dianle;
import com.miji.MijiConnect;
import com.newqm.sdkoffer.QuMiConnect;
import com.winad.android.offers.AdManager;

public class LikeActivity extends JQBaseActivity {

	ProgressBar loading;
	ListView listview;
	DownLoadAdapter adapter;
	//View app1, app2, app3, app4, spread;

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
//		app1 = findViewById(R.id.app1);
//		app2 = findViewById(R.id.app2);
//		app3 = findViewById(R.id.app3);
//		app4 = findViewById(R.id.app4);
//		spread = findViewById(R.id.spread);
//		OnClickListener appListener = new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO show apps
//				if (arg0 == app1) {
//					MijiConnect.getInstance().showOffers();
//				} else if (arg0 == app2) {
//					Dianle.showOffers(LikeActivity.this);
//				} else if (arg0 == app3) {
//					AdManager.showAdOffers(LikeActivity.this);
//				} else if (arg0 == app4) {
//					QuMiConnect.getQumiConnectInstance().showOffers(null);
//				} else {
//					Toast.makeText(LikeActivity.this, "敬请期待", 0).show();
//				}
//			}
//		};
//		app1.setOnClickListener(appListener);
//		app2.setOnClickListener(appListener);
//		app3.setOnClickListener(appListener);
//		app4.setOnClickListener(appListener);
//		spread.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent it = new Intent();
//				it.setClass(getParent(), SpreadActivity.class);
//				startActivity(it);
//			}
//		});
	}

	public void setData() {
		adapter = new DownLoadAdapter();
		Static.share.downLoadAdapter = adapter;
		listview.setAdapter(adapter);
		loading.setVisibility(View.GONE);
	}

	public class DownLoadAdapter extends BaseAdapter {
		ArrayList<Object> dataArr = new ArrayList<Object>();

		public DownLoadAdapter() {
			refresh();
		}

		public void refresh() {
			dataArr.clear();
			List<PackageInfo> infoArr = scanInstalledPackage();
			for (DownloadTask task : Static.downTasks) {
				boolean installed = false;
				for (PackageInfo temp : infoArr) {
					if (temp.packageName.equals(task.packagename)) {
						installed = true;
						break;
					}
				}
				if (!installed) {
					dataArr.add(task);
				}
			}
			mock();
		}

		public void mock(){
			MockTask spread=new MockTask();
			spread.txt="推广朋友赚1.5元";
			spread.drawable=R.drawable.tuiguang;
			spread.listener=new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent it = new Intent();
					it.setClass(getParent(), SpreadActivity.class);
					startActivity(it);
				}
			};
			MockTask app1=new MockTask();
			app1.txt="快速通道1";
			app1.drawable=R.drawable.mijifen;
			app1.listener=new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MijiConnect.getInstance().showOffers();
				}
			};
			
			MockTask app2=new MockTask();
			app2.txt="快速通道2";
			app2.drawable=R.drawable.dianle;
			app2.listener=new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Dianle.showOffers(LikeActivity.this);
				}
			};
			MockTask app3=new MockTask();
			app3.txt="快速通道3";
			app3.drawable=R.drawable.yinggao;
			app3.listener=new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AdManager.showAdOffers(LikeActivity.this);
				}
			};
			
			MockTask app4=new MockTask();
			app4.txt="快速通道4";
			app4.drawable=R.drawable.qumi;
			app4.listener=new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					QuMiConnect.getQumiConnectInstance().showOffers(null);
				}
			};
			dataArr.add(0, spread);
			dataArr.add(app1);
			dataArr.add(app2);
			dataArr.add(app3);
			dataArr.add(app4);
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
			View v = null;// = arg1;
			Object obj = dataArr.get(arg0);
			if (obj instanceof DownloadTask) {
				final DownloadTask data = (DownloadTask) obj;
				if (v == null) {
					v = View.inflate(LikeActivity.this, R.layout.download_item,
							null);
				}
				TextView name = (TextView) v.findViewById(R.id.apkname);
				TextView info = (TextView) v.findViewById(R.id.info);
				ImageView icon = (ImageView) v.findViewById(R.id.appicon);
				final Button btn = (Button) v.findViewById(R.id.downbtn);
				final Button play = (Button) v.findViewById(R.id.play);
				if (data.downpercent < 100) {
					btn.setAlpha(1);
					play.setVisibility(View.GONE);
				} else {
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
						Static.share.currentDownLoad = data;
						Static.share.isCurrentDownload = true;
						Intent it = new Intent();
						it.setClass(LikeActivity.this, RunPackageActivity.class);
						startActivityForResult(it,
								RunPackageActivity.TYPE_DOWNLOAD);
					}
				});
				String html = "安装和签到总赚<h3><font color=\"#ee0000\">"
						+ data.price + "</h3>元";
				// info.setText("安装和签到总赚" + data.price+ "元");
				info.setText(Html.fromHtml(html));
				name.setText(data.appname);
				if (data.iconPth == null) {
					new ImageBinder(icon, data).execute();
				} else {
					icon.setImageURI(Uri.fromFile(new File(data.iconPth)));
				}
			}else if(obj instanceof MockTask){
				final MockTask data=(MockTask) obj;
				v=View.inflate(LikeActivity.this, R.layout.mock_item,
						null);
				TextView name = (TextView) v.findViewById(R.id.apkname);
				ImageView icon = (ImageView) v.findViewById(R.id.appicon);
				icon.setImageResource(data.drawable);
				name.setText(data.txt);
				v.setOnClickListener(data.listener);
			}
			return v;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("qq", "post now!");
	}
}
