package org.leepood.monitordemo;

import java.util.HashMap;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class bootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// �ж��Ƿ��ǿ��������Ĺ㲥
		Log.i("action", intent.getAction());
		if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {

		} else if (intent.getAction() == monitorApp.ACTION) {
			// Log.i("action", "received");
			Bundle bundle = intent.getBundleExtra("bundle");
			HashMap hashmap = (HashMap) bundle.getSerializable("app_info");
			Iterator<String> it = hashmap.keySet().iterator();
			while (it.hasNext()) {
				String key=it.next();
				String value=hashmap.get(key).toString();
				Log.i("hashmap", key+":"+value);
			}
		}

	}

}
