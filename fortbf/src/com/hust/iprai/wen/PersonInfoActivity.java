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

	TextView allin,allout,downin,sinin,wallpaperin,spreadin,quickin,spreadDetail,money;
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
		quickin=(TextView)findViewById(R.id.quickin);
		spreadDetail=(TextView)findViewById(R.id.spreaddetail);
		money=(TextView)findViewById(R.id.money);
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
					money.setText(result.money+"");
					allin.setText(result.allin+"");
					allout.setText(result.allout+"");
					downin.setText(result.downin+"");
					sinin.setText(result.signin+"");
					wallpaperin.setText(result.wallpaperin+"");
					spreadin.setText(result.spreadin+"");
					quickin.setText(result.quickin+"");
					spreadDetail.setText("推广用户"+result.spreadnum+"人，其中"+result.activenum+"人激活");
				}
			}
		};
		task.execute();
	}

}
