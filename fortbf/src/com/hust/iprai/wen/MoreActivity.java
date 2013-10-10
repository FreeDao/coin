package com.hust.iprai.wen;

import org.jq.model.Httpres;
import org.jq.zmdr.R;

import util.Static;
import util.Tool;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MoreActivity extends Activity {

	EditText txt;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_activity);
		initView();
	}

	private void initView() {
		txt = (EditText) findViewById(R.id.txt);
		btn = (Button) findViewById(R.id.submit);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String str = txt.getText().toString();
				if (!Tool.isEmpty(str)) {
					txt.setText("");
					Toast.makeText(MoreActivity.this, "感谢您的反馈！我们会尽快处理", 0)
							.show();
					addFeedback(str);
				}
			}
		});
	}

	protected void addFeedback(final String txt) {
		AsyncTask<Void, Void, Httpres> task=new AsyncTask<Void, Void, Httpres>(){
			@Override
			protected Httpres doInBackground(Void... arg0) {
				return Static.addFeedBack.run(new String[]{txt});
			}
		};
		task.execute();
	}

	
}
