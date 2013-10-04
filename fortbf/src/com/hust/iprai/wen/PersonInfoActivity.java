package com.hust.iprai.wen;

import org.jq.model.Detail;
import org.jq.nbz.R;

import util.Static;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PersonInfoActivity extends Activity {

	TextView allin,allout,downin,sinin,wallpaperin,spreadin;
	View refresh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_activity);
		initView();
		refresh();
	}
	
	private void initView(){
		allin=(TextView)findViewById(R.id.allin);
		allout=(TextView)findViewById(R.id.allout);
		downin=(TextView)findViewById(R.id.downin);
		sinin=(TextView)findViewById(R.id.signin);
		wallpaperin=(TextView)findViewById(R.id.wallpaperin);
		spreadin=(TextView)findViewById(R.id.spreadin);
		refresh=findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				refresh();
			}
		});
	}
	
	private void refresh(){
		AsyncTask<Void, Void, Detail> task=new AsyncTask<Void, Void, Detail>(){

			@Override
			protected Detail doInBackground(Void... params) {
				return Static.getDetail.run(new String[]{""});
			}
			
			@Override
			protected void onPostExecute(Detail result) {
				if(result!=null){
					allin.setText(result.allin+"");
					allout.setText(result.allout+"");
					downin.setText(result.downin+"");
					sinin.setText(result.signin+"");
					wallpaperin.setText(result.wallpaperin+"");
					spreadin.setText(result.spreadin+"");
				}
			}
		};
		task.execute();
	}

}
