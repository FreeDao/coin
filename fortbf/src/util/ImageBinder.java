package util;

import java.io.File;

import org.jq.model.BaseModel;
import org.jq.model.DownloadTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageBinder extends AsyncTask<String, Void, String>{

	ImageView view;
	DownloadTask data;
	
	public ImageBinder(ImageView view,DownloadTask data){
		this.view=view;
		this.data=data;
	}
	
	@Override
	protected String doInBackground(String... params) {
		return BaseModel.getIcon(data.icon);
	}

	@Override
	protected void onPostExecute(String result) {
		data.iconPth=result;
		view.setImageURI(Uri.fromFile(new File(result)));
	}
}
