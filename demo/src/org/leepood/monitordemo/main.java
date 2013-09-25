package org.leepood.monitordemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.startService(new Intent(this,monitorApp.class));
    }
}