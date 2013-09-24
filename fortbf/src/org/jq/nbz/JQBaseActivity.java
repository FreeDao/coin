package org.jq.nbz;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

public class JQBaseActivity extends Activity {

	
	public void runDelay(Runnable r,long timeMillis){
		new Handler().postDelayed(r, timeMillis);
	}

	public List<PackageInfo> scanInstalledPackage(){
		PackageManager pm=getPackageManager();
		List<PackageInfo> pinfoList=pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for(PackageInfo info : pinfoList){
			Log.e("qq", info.packageName);
		}
		return pinfoList;
	}
}
