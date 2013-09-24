package org.jq.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import util.Static;
import util.Tool;
import android.content.Context;
import android.telephony.TelephonyManager;

public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String uid = null;

	public static String getDeviceId(Context context) {
		if (!Tool.isEmpty(uid)) {
			return uid;
		}
		StringBuilder deviceId = new StringBuilder();

		try {
			// IMEI£¨imei£©
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			if (!Tool.isEmpty(imei)) {
				deviceId.append(imei);
				return deviceId.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		uid = deviceId.toString();
		return uid;

	}

	public String getIcon(String url) {
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
				int len=0;
				while((len=in.read(buffer))>0){
					out.write(buffer, 0, len);
					out.flush();
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

}
