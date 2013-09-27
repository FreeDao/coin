package org.jq.model;

import android.os.AsyncTask;

public class DownloadTask extends BaseModel{

	private static final long serialVersionUID = 4586400159230078146L;

	public String appname;
	public String packagename;
	public String icon;
	public String apk;
	public double price;
	
	public String iconPth;
	public int downpercent=0;
	public String apkPth;
	public long playtime=10L;
	public AsyncTask<Void, Void, String> bindImage=new AsyncTask<Void, Void, String>(){
		
		protected void onPostExecute(String result) {
			iconPth=result;
		}

		@Override
		protected String doInBackground(Void... params) {
			return getIcon(icon);
		};
	};
}
