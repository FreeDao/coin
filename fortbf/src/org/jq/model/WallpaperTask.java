package org.jq.model;

import android.os.AsyncTask;

public class WallpaperTask extends BaseModel{

	private static final long serialVersionUID = -6175405107683205155L;

	public String name;
	public String image;
	public double leftprice;
	public double rightprice;
	public double maxmoney;
	public double percent;
	
	public String imgPth;
	
	
	public transient AsyncTask<Void, Void, String> bindImage=new AsyncTask<Void, Void, String>(){
		
		protected void onPostExecute(String result) {
			imgPth=result;
		}

		@Override
		protected String doInBackground(Void... params) {
			return getIcon(image);
		};
	};
}
