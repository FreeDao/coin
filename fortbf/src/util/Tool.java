package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Tool {

	public static boolean isEmpty(String uuid) {
		return uuid==null||uuid.equals("");
	}
	
	public interface DownCallBack{
		public void progress(Integer progress);
	}
	
	public static String download(String url,DownCallBack callback) {
		String pth = null;
		String fname = "" + url.hashCode();
		String base = Static.loaclPth + "/";
		File aim = new File(base + fname);
		if (aim.exists()) {
			pth = aim.getAbsolutePath();
		} else {
			try {
				URLConnection connection = new URL(url).openConnection();
				InputStream in=connection.getInputStream();
				FileOutputStream out=new FileOutputStream(aim);
				byte[] buffer=new byte[100];
				int total=connection.getContentLength();
				int len=0;
				int done=0;
				while((len=in.read(buffer))>0){
					out.write(buffer, 0, len);
					out.flush();
					done+=len;
					callback.progress(100*done/total);
				}
				in.close();
				out.close();
				pth=aim.getAbsolutePath();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pth;
	}
	
	public static void install(Context con,String pth){
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(pth)), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		con.startActivity(intent);
	}
}
