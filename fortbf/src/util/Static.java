package util;

import http.HttpRequester;
import http.HttpRespons;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.jq.model.BaseModel;
import org.jq.model.DownloadTask;
import org.jq.model.Httpres;
import org.jq.model.SignTask;
import org.jq.model.WallpaperTask;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;

public class Static {

	
	public interface callBack<T>{
		T run(String... args);
	}
	
	public static String baseUrl="http://192.168.1.10:8000/";
	public static Context appContext;
	public static String loaclPth;
	public static ArrayList<WallpaperTask> wallTasks;
	public static ArrayList<DownloadTask> downTasks;
	public static ArrayList<SignTask> signTasks;
	public static callBack<Integer> checkNet=new callBack<Integer>() {
		
		//-1 error,0 has a,1 no user
		@Override
		public Integer run(String... args) {
			int res=-1;
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"checkDevice?uid="+BaseModel.getDeviceId(appContext));
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				res=httpres.code;
			} catch (Exception e) {
				return -1;
			}
			return res;
		}
	};

	public static callBack<Httpres> reg=new callBack<Httpres>() {

		@SuppressWarnings("deprecation")
		@Override
		public Httpres run(String... args) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			map.put("uname", URLEncoder.encode(args[0]));
			map.put("upwd", URLEncoder.encode(args[1]));
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"addDevice",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				return httpres;
			} catch (Exception e) {
				Log.e("qq", e.toString());
				return null;
			}
		}
	};
	
	public static callBack<ArrayList<DownloadTask>> getDownLoadTask=new callBack<ArrayList<DownloadTask>>() {

		@Override
		public ArrayList<DownloadTask> run(String... args) {
			ArrayList<DownloadTask> res=new ArrayList<DownloadTask>();
			HttpRequester request=new HttpRequester();
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			HttpRespons response;
			try {
				response = request.sendGet(baseUrl+"getDownTask",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				res.addAll(JSON.parseArray(httpres.message, DownloadTask.class));
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("qq", e.toString());
			}
			return res;
		}
	};
	
	public static callBack<ArrayList<SignTask>> getSignTask=new callBack<ArrayList<SignTask>>() {

		@Override
		public ArrayList<SignTask> run(String... args) {
			ArrayList<SignTask> res=new ArrayList<SignTask>();
			HttpRequester request=new HttpRequester();
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			HttpRespons response;
			try {
				response = request.sendGet(baseUrl+"getSignTask",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				res.addAll(JSON.parseArray(httpres.message, SignTask.class));
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("qq", e.toString());
			}
			return res;
		}
	};
	
	public static callBack<ArrayList<WallpaperTask>> getWallpaperTask=new callBack<ArrayList<WallpaperTask>>() {

		@Override
		public ArrayList<WallpaperTask> run(String... args) {
			ArrayList<WallpaperTask> res=new ArrayList<WallpaperTask>();
			HttpRequester request=new HttpRequester();
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			HttpRespons response;
			try {
				response = request.sendGet(baseUrl+"getWallPaperTask",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				res.addAll(JSON.parseArray(httpres.message, WallpaperTask.class));
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("qq", e.toString());
			}
			return res;
		}
	};
}
