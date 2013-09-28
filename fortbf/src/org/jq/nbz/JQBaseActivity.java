package org.jq.nbz;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

public class JQBaseActivity extends Activity {

	
	public void runDelay(Runnable r,long timeMillis){
		new Handler().postDelayed(r, timeMillis);
	}

	public List<PackageInfo> scanInstalledPackage(){
		PackageManager pm=getPackageManager();
		List<PackageInfo> pinfoList=pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		return pinfoList;
	}
}
