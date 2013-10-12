package com.hust.iprai.wen;

import org.jq.model.Detail;
import org.jq.zmdr.R;

import util.Static;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PersonInfoActivity extends Activity {

	TextView did,allin,allout,downin,sinin,wallpaperin,spreadin,quickin,spreadDetail,money;
	View refresh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_activity);
		initView();
		refresh();
	}
	
	private void initView(){
		did=(TextView)findViewById(R.id.did);
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
					did.setText("总收支          桌面达人ID:"+(840046209l+result.did));
					money.setText(result.money+"元");
					allin.setText(result.allin+"元");
					allout.setText(result.allout+"元");
					downin.setText(result.downin+"元");
					sinin.setText(result.signin+"元");
					wallpaperin.setText(result.wallpaperin+"元");
					spreadin.setText(result.spreadin+"元");
					quickin.setText(result.quickin+"元");
					spreadDetail.setText("推广用户"+result.spreadnum+"人，其中"+result.activenum+"人激活");
				}
			}
		};
		task.execute();
	}

}
