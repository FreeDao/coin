package org.jq.nbz;

import java.util.ArrayList;

import org.jq.model.PayRequestRecord;
import org.jq.zmdr.R;

import util.Static;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PayRecordActivity extends Activity {

	ProgressBar loading;
	Button refresh;
	ListView listView;
	PayRecodeAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_record);
		initView();
		refresh();
	}

	public void initView(){
		loading=(ProgressBar) findViewById(R.id.progressBar1);
		refresh=(Button) findViewById(R.id.refresh);
		listView=(ListView) findViewById(R.id.listview);
		adapter=new PayRecodeAdapter();
		listView.setAdapter(adapter);
		
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				refresh();
			}
		});
	}
	
	private void refresh(){
		AsyncTask<Void, Void, ArrayList<PayRequestRecord>> asy=new AsyncTask<Void, Void, ArrayList<PayRequestRecord>>(){
			
			@Override
			protected void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
			}
			
			@Override
			protected ArrayList<PayRequestRecord> doInBackground(Void... arg0) {
				return Static.getPayRequestRecord.run(new String[]{""});
			}
			
			@Override
			protected void onPostExecute(ArrayList<PayRequestRecord> result) {
				Static.payRequsts=result;
				adapter.notifyDataSetChanged();
				loading.setVisibility(View.GONE);
			}
		};
		asy.execute();
	}
	
	class PayRecodeAdapter extends BaseAdapter{

	
		
		@Override
		public int getCount() {
			return Static.payRequsts.size();
		}

		@Override
		public Object getItem(int arg0) {
			return Static.payRequsts.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			PayRequestRecord data=Static.payRequsts.get(arg0);
			View v=View.inflate(getParent(), R.layout.pay_request_record_item, null);
			ImageView icon=(ImageView) v.findViewById(R.id.appicon);
			TextView info=(TextView) v.findViewById(R.id.info);
			TextView time=(TextView)v.findViewById(R.id.time);
			
			if(data.status.equals("wait")){
				icon.setImageResource(R.drawable.waiting);
			}else{
				icon.setImageResource(R.drawable.dealed);
			}
			info.setText(data.info);
			time.setText(data.time+"    "+data.status);
			return v;
		}
		
	}
}
