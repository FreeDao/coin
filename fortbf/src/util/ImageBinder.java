package util;

import java.io.File;

import org.jq.model.BaseModel;
import org.jq.model.DownloadTask;
import org.jq.model.NetImage;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageBinder extends AsyncTask<String, Void, String>{

	ImageView view;
	NetImage data;
	
	public ImageBinder(ImageView view,NetImage data){
		this.view=view;
		this.data=data;
	}
	
	@Override
	protected String doInBackground(String... params) {
		return BaseModel.getIcon(data.getImgUrl());
	}

	@Override
	protected void onPostExecute(String result) {
		data.SetCachePth(result);
		view.setImageURI(Uri.fromFile(new File(result)));
	}
}
