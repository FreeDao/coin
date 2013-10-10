package util;

import http.HttpRequester;
import http.HttpRespons;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


import org.jq.model.BaseModel;
import org.jq.model.Detail;
import org.jq.model.DownloadTask;
import org.jq.model.Httpres;
import org.jq.model.PayRequestRecord;
import org.jq.model.SignTask;
import org.jq.model.Version;
import org.jq.model.WallpaperTask;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hust.iprai.wen.home.LikeActivity.DownLoadAdapter;
import com.hust.iprai.wen.home.MarkActivity.SignAdapter;

public class Static {

	public static class share{
		public static boolean isCurrentDownload;
		
		public static DownloadTask currentDownLoad=null;
		public static DownLoadAdapter downLoadAdapter=null;
		
		public static SignTask currentSignTask=null;
		public static SignAdapter signAdapter=null;
		
		public static Httpres device;
		public static Version version;
	}
	
	public interface callBack<T>{
		T run(String... args);
	}
	
	public static String baseUrl="http://192.168.0.104:8000/";
	public static Context appContext;
	public static String loaclPth;
	public static ArrayList<WallpaperTask> wallTasks;
	public static ArrayList<DownloadTask> downTasks;
	public static ArrayList<SignTask> signTasks;
	public static ArrayList<PayRequestRecord> payRequsts=new ArrayList<PayRequestRecord>();;
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
				if(!Tool.isEmpty(httpres.version)){
					try{
						share.version=JSON.parseObject(httpres.version, Version.class);
					}catch (Exception e) {}
				}
				share.device=httpres;
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
			map.put("uname", BaseModel.getDeviceId(appContext));
			map.put("upwd", BaseModel.getDeviceId(appContext));
			map.put("father", URLEncoder.encode(args[2]));
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
	
	public static callBack<Httpres> addDownload=new callBack<Httpres>() {

		@SuppressWarnings("deprecation")
		@Override
		public Httpres run(String... args) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			map.put("packagename", URLEncoder.encode(args[0]));
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"adddownload",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				return httpres;
			} catch (Exception e) {
				Log.e("qq", e.toString());
				return null;
			}
		}
	};
	
	public static callBack<Httpres> addSign=new callBack<Httpres>() {

		@SuppressWarnings("deprecation")
		@Override
		public Httpres run(String... args) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			map.put("packagename", URLEncoder.encode(args[0]));
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"addSign",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				return httpres;
			} catch (Exception e) {
				Log.e("qq", e.toString());
				return null;
			}
		}
	};
	
	public static callBack<Httpres> addWallpaper=new callBack<Httpres>() {

		@SuppressWarnings("deprecation")
		@Override
		public Httpres run(String... args) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			map.put("wallpapername", URLEncoder.encode(args[0]));
			map.put("lorr", URLEncoder.encode(args[1]));// 0:left, 1:right

			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"addWallpaper",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				return httpres;
			} catch (Exception e) {
				Log.e("qq", e.toString());
				return null;
			}
		}
	};
	
	public static callBack<Httpres> addpayrequest=new callBack<Httpres>() {

		@SuppressWarnings("deprecation")
		@Override
		public Httpres run(String... args) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			map.put("paytype", URLEncoder.encode(args[0]));
			map.put("spend", URLEncoder.encode(args[1]));// 0:left, 1:right

			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"addpayrequest",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				return httpres;
			} catch (Exception e) {
				Log.e("qq", e.toString());
				return null;
			}
		}
	};

	public static callBack<ArrayList<PayRequestRecord>> getPayRequestRecord=new callBack<ArrayList<PayRequestRecord>>() {

		@Override
		public ArrayList<PayRequestRecord> run(String... args) {
			ArrayList<PayRequestRecord> res=new ArrayList<PayRequestRecord>();
			HttpRequester request=new HttpRequester();
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			HttpRespons response;
			try {
				response = request.sendGet(baseUrl+"getPayRecord",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				res.addAll(JSON.parseArray(httpres.message, PayRequestRecord.class));
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("qq", e.toString());
			}
			return res;
		}
	};
	public static callBack<Detail> getDetail=new callBack<Detail>() {
		
		//-1 error,0 has a,1 no user
		@Override
		public Detail run(String... args) {
			Detail res=null;
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"checkDevice?uid="+BaseModel.getDeviceId(appContext));
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				res=JSON.parseObject(httpres.message, Detail.class);
			} catch (Exception e) {
				return null;
			}
			return res;
		}
	};
	
	public static callBack<Httpres> addFeedBack=new callBack<Httpres>() {

		@SuppressWarnings("deprecation")
		@Override
		public Httpres run(String... args) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			map.put("txt", URLEncoder.encode(args[0]));
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"addfeedback",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				return httpres;
			} catch (Exception e) {
				Log.e("qq", e.toString());
				return null;
			}
		}
	};
	public static callBack<String> getSpread=new callBack<String>() {
		
		//-1 error,0 has a,1 no user
		@Override
		public String run(String... args) {
			String res=null;
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"getSpread?uid="+BaseModel.getDeviceId(appContext));
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				res=httpres.message;
			} catch (Exception e) {
				return null;
			}
			return res;
		}
	};
	public static callBack<Httpres> addquickin=new callBack<Httpres>() {

		@SuppressWarnings("deprecation")
		@Override
		public Httpres run(String... args) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("uid", BaseModel.getDeviceId(appContext));
			map.put("coin", URLEncoder.encode(args[0]));
			HttpRequester request=new HttpRequester();
			try {
				HttpRespons response=request.sendGet(baseUrl+"addquickin",map);
				Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
				return httpres;
			} catch (Exception e) {
				Log.e("qq", e.toString());
				return null;
			}
		}
	};
//	public static void reinit(){
//		reg=new callBack<Httpres>() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public Httpres run(String... args) {
//				HashMap<String, String> map=new HashMap<String, String>();
//				map.put("uid", BaseModel.getDeviceId(appContext));
//				map.put("uname", URLEncoder.encode(args[0]));
//				map.put("upwd", URLEncoder.encode(args[1]));
//				HttpRequester request=new HttpRequester();
//				try {
//					HttpRespons response=request.sendGet(baseUrl+"addDevice",map);
//					Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
//					return httpres;
//				} catch (Exception e) {
//					Log.e("qq", e.toString());
//					return null;
//				}
//			}
//		};
//		
//		getDownLoadTask=new callBack<ArrayList<DownloadTask>>() {
//
//			@Override
//			public ArrayList<DownloadTask> run(String... args) {
//				ArrayList<DownloadTask> res=new ArrayList<DownloadTask>();
//				HttpRequester request=new HttpRequester();
//				HashMap<String, String> map=new HashMap<String, String>();
//				map.put("uid", BaseModel.getDeviceId(appContext));
//				HttpRespons response;
//				try {
//					response = request.sendGet(baseUrl+"getDownTask",map);
//					Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
//					res.addAll(JSON.parseArray(httpres.message, DownloadTask.class));
//				} catch (IOException e) {
//					e.printStackTrace();
//					Log.e("qq", e.toString());
//				}
//				return res;
//			}
//		};
//		
//		getSignTask=new callBack<ArrayList<SignTask>>() {
//
//			@Override
//			public ArrayList<SignTask> run(String... args) {
//				ArrayList<SignTask> res=new ArrayList<SignTask>();
//				HttpRequester request=new HttpRequester();
//				HashMap<String, String> map=new HashMap<String, String>();
//				map.put("uid", BaseModel.getDeviceId(appContext));
//				HttpRespons response;
//				try {
//					response = request.sendGet(baseUrl+"getSignTask",map);
//					Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
//					res.addAll(JSON.parseArray(httpres.message, SignTask.class));
//				} catch (IOException e) {
//					e.printStackTrace();
//					Log.e("qq", e.toString());
//				}
//				return res;
//			}
//		};
//		getWallpaperTask=new callBack<ArrayList<WallpaperTask>>() {
//
//			@Override
//			public ArrayList<WallpaperTask> run(String... args) {
//				ArrayList<WallpaperTask> res=new ArrayList<WallpaperTask>();
//				HttpRequester request=new HttpRequester();
//				HashMap<String, String> map=new HashMap<String, String>();
//				map.put("uid", BaseModel.getDeviceId(appContext));
//				HttpRespons response;
//				try {
//					response = request.sendGet(baseUrl+"getWallPaperTask",map);
//					Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
//					res.addAll(JSON.parseArray(httpres.message, WallpaperTask.class));
//				} catch (IOException e) {
//					e.printStackTrace();
//					Log.e("qq", e.toString());
//				}
//				return res;
//			}
//		};
//		
//		addWallpaper=new callBack<Httpres>() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public Httpres run(String... args) {
//				HashMap<String, String> map=new HashMap<String, String>();
//				map.put("uid", BaseModel.getDeviceId(appContext));
//				map.put("wallpapername", URLEncoder.encode(args[0]));
//				map.put("lorr", URLEncoder.encode(args[1]));// 0:left, 1:right
//
//				HttpRequester request=new HttpRequester();
//				try {
//					HttpRespons response=request.sendGet(baseUrl+"addWallpaper",map);
//					Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
//					return httpres;
//				} catch (Exception e) {
//					Log.e("qq", e.toString());
//					return null;
//				}
//			}
//		};
//		
//		addSign=new callBack<Httpres>() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public Httpres run(String... args) {
//				HashMap<String, String> map=new HashMap<String, String>();
//				map.put("uid", BaseModel.getDeviceId(appContext));
//				map.put("packagename", URLEncoder.encode(args[0]));
//				HttpRequester request=new HttpRequester();
//				try {
//					HttpRespons response=request.sendGet(baseUrl+"addSign",map);
//					Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
//					return httpres;
//				} catch (Exception e) {
//					Log.e("qq", e.toString());
//					return null;
//				}
//			}
//		};
//		
//		addDownload=new callBack<Httpres>() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public Httpres run(String... args) {
//				HashMap<String, String> map=new HashMap<String, String>();
//				map.put("uid", BaseModel.getDeviceId(appContext));
//				map.put("packagename", URLEncoder.encode(args[0]));
//				HttpRequester request=new HttpRequester();
//				try {
//					HttpRespons response=request.sendGet(baseUrl+"adddownload",map);
//					Httpres httpres=JSON.parseObject(response.getContent(), Httpres.class);
//					return httpres;
//				} catch (Exception e) {
//					Log.e("qq", e.toString());
//					return null;
//				}
//			}
//		};
//		
//	}
}
