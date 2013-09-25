package org.leepood.monitordemo;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class bootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//判断是否是开机发出的广播
		Log.i("action", intent.getAction());
		if(intent.getAction()==Intent.ACTION_BOOT_COMPLETED)
		{
			
		}
		else if(intent.getAction()==monitorApp.ACTION)
		{
//			Log.i("action", "received");
		Bundle bundle=intent.getBundleExtra("bundle");
		HashMap hashmap=(HashMap) bundle.getSerializable("app_info");
		Log.i("hashmap", String.valueOf(hashmap.size()));
		}

	}

}
