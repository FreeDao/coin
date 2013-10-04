package org.jq.nbz;

import util.Static;
import util.Tool;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SpreadActivity extends Activity {

	TextView url;
	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spread);
		initView();
		refresh();
	}

	private void initView() {
		url=(TextView)findViewById(R.id.url);
		btn=(Button)findViewById(R.id.copy);
		btn.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE); 
				clip.setText(url.getText());
				Toast.makeText(SpreadActivity.this, "¸´ÖÆ³É¹¦", 0).show();
			}
		});
	}

	public void refresh(){
		AsyncTask<Void, Void, String> task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... arg0) {
				return Static.getSpread.run(new String[]{""});
			}
			
			@Override
			protected void onPostExecute(String result) {
				url.setText(Tool.makeSpreadUrl(result));
			}
			
		};
		task.execute();
	}

}
