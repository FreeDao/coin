package org.jq.nbz;

import org.jq.model.Httpres;
import org.jq.model.PayType;

import util.Static;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PayRequest extends Activity {

	Spinner spi;
	EditText message;
	Button commit,refresh;
	TextView intro,currentMoney;
	PayType payType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		refreshMoney();
	}

	public void initView() {
		View v = View.inflate(getParent(), R.layout.activity_pay_request, null);
		setContentView(v);
		spi = (Spinner) v.findViewById(R.id.spinner1);
		message = (EditText) v.findViewById(R.id.message);
		commit = (Button) v.findViewById(R.id.ok);
		intro=(TextView)v.findViewById(R.id.intro);
		currentMoney=(TextView)v.findViewById(R.id.currentmoney);
		refresh=(Button)v.findViewById(R.id.refresh);
		
		ArrayAdapter<PayType> adapter = new ArrayAdapter<PayType>(this,
				android.R.layout.simple_spinner_item, PayType.typeArr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi.setAdapter(adapter);
		spi.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				payType=PayType.typeArr[arg2];
				intro.setText(payType.intro);	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				message.setText("«ÎÃÓ–¥∂‘”¶’À∫≈:");
			}
		});
		
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				refresh.setEnabled(false);
				refreshMoney();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						refresh.setEnabled(true);
					}
				}, 10000);
			}
		});
		
		commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(message.getText()==null||message.getText().length()<2){
					Toast.makeText(getParent(), "«ÎÃÓ–¥’À∫≈–≈œ¢~", 0).show();
					return;
				}
				if(Static.share.device.devmoney<payType.needMoney){
					Toast.makeText(getParent(), "”‡∂Ó≤ª◊„≈∂~", 0).show();
					return;
				}
				AsyncTask<Void, Void, Httpres> task=new AsyncTask<Void, Void, Httpres>(){

					@Override
					protected Httpres doInBackground(Void... params) {
						return Static.addpayrequest.run(new String[]{payType.toString()+"_"+message.getText().toString(),payType.needMoney+""});
					}
					
					@Override
					protected void onPostExecute(Httpres result) {
						if(result.code==0){
							refreshMoney();
							Toast.makeText(getParent(), "…Í«Î≥…π¶", 1).show();
						}else{
							Toast.makeText(getParent(), result.message, 1).show();
						}
					}
				};
				task.execute();
			}
		});
	}

	public void refreshMoney(){
		AsyncTask<Void, Void, Httpres> task=new AsyncTask<Void, Void, Httpres>(){

			@Override
			protected Httpres doInBackground(Void... params) {
				Static.checkNet.run(new String[]{""});
				return Static.share.device;
			}
			
			@Override
			protected void onPostExecute(Httpres result) {
				if(result!=null){
					double dou=(double)Math.round(result.devmoney*100)/100;
					currentMoney.setText(dou+"£§");
				}
			}
		};
		task.execute();
	}
}
