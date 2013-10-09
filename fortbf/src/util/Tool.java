package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Tool {

	public static boolean isEmpty(String uuid) {
		return uuid==null||uuid.equals("");
	}
	
	public interface DownCallBack{
		public void progress(Integer progress);
	}
	public static String download(String url,DownCallBack callback){
		return download(url, callback, false);
	}
	public static String download(String url,DownCallBack callback,boolean force) {
		String pth = null;
		String fname = "" + url.hashCode();
		String base = Static.loaclPth + "/";
		File aim = new File(base + fname);
		if (aim.exists()&&!force) {
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
	
	public static void saveObj(String fname,Object obj){
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/zmdr/data");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File f=new File(dir.getAbsolutePath()+"/"+fname);
		try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(obj);
			out.close();
		} catch (Exception e) {
			Log.e("qq", e.toString());
		} 
		
	}
	
	public static Object readObj(String fname){
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/zmdr/data");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File f=new File(dir.getAbsolutePath()+"/"+fname);
		
		Object res=null;
		try {
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(f));
			res=in.readObject();
			in.close();
		} catch (Exception e) {
			Log.e("qq", e.toString());
		} 
		
		return res;
	}
	
	public static String makeSpreadUrl(String fid){
		String res=Static.baseUrl+"spread/"+fid+".apk";
		return res;
	}
	
	

}
