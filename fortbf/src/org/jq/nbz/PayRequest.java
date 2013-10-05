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

import com.dlnetwork.Dianle;
import com.dlnetwork.GetTotalMoneyListener;
import com.dlnetwork.SpendMoneyListener;
import com.miji.MijiConnect;
import com.miji.MijiNotifier;
import com.miji.MijiSpendPointsNotifier;
import com.winad.android.offers.AdManager;
import com.winad.android.offers.parameter.SpendScoreListener;

public class PayRequest extends Activity implements MijiNotifier,
		MijiSpendPointsNotifier, GetTotalMoneyListener, SpendMoneyListener, SpendScoreListener {

	Spinner spi;
	EditText message;
	Button commit, refresh, changeMoney;
	TextView intro, currentMoney, currentCoin;
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
		intro = (TextView) v.findViewById(R.id.intro);
		currentMoney = (TextView) v.findViewById(R.id.currentmoney);
		refresh = (Button) v.findViewById(R.id.refresh);
		changeMoney = (Button) v.findViewById(R.id.changetomoney);
		currentCoin = (TextView) v.findViewById(R.id.currentcoin);
		ArrayAdapter<PayType> adapter = new ArrayAdapter<PayType>(this,
				android.R.layout.simple_spinner_item, PayType.typeArr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi.setAdapter(adapter);
		spi.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				payType = PayType.typeArr[arg2];
				intro.setText(payType.intro);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				message.setText("请填写对应账号:");
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
				if (message.getText() == null || message.getText().length() < 2) {
					Toast.makeText(getParent(), "请填写账号信息~", 0).show();
					return;
				}
				if (Static.share.device.devmoney < payType.needMoney) {
					Toast.makeText(getParent(), "余额不足哦~", 0).show();
					return;
				}
				AsyncTask<Void, Void, Httpres> task = new AsyncTask<Void, Void, Httpres>() {

					@Override
					protected Httpres doInBackground(Void... params) {
						return Static.addpayrequest.run(new String[] {
								payType.toString() + "_"
										+ message.getText().toString(),
								payType.needMoney + "" });
					}

					@Override
					protected void onPostExecute(Httpres result) {
						if (result.code == 0) {
							refreshMoney();
							Toast.makeText(getParent(), "申请成功", 1).show();
						} else {
							Toast.makeText(getParent(), result.message, 1)
									.show();
						}
					}
				};
				task.execute();
			}
		});
		changeMoney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				changeMoney.setEnabled(false);
				changeAll();
			}
		});
	}

	public void refreshMoney() {
		countCoin();
		AsyncTask<Void, Void, Httpres> task = new AsyncTask<Void, Void, Httpres>() {

			@Override
			protected Httpres doInBackground(Void... params) {
				Static.checkNet.run(new String[] { "" });
				return Static.share.device;
			}

			@Override
			protected void onPostExecute(Httpres result) {
				if (result != null) {
					double dou = (double) Math.round(result.devmoney * 100) / 100;
					currentMoney.setText(dou + "￥");
				}
			}
		};
		task.execute();
	}

	int[] apps = new int[4];
	Handler handler=new Handler();
	public double addAllCoin() {
		double allCoin = 0;
		for (double d : apps) {
			allCoin += d;
		}
		final double res=allCoin;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				currentCoin.setText(res+ "金币");
			}
		});
		return allCoin;
	}

	//TODO
	public void countCoin() {
		MijiConnect.getInstance().getPoints(this);
		Dianle.getTotalMoney(getParent(), this);
		apps[2]=AdManager.getPoints(getParent());
		addAllCoin();
	}

	//TODO
	public void changeAll() {
		if(addAllCoin()<1){
			changeMoney.setEnabled(true);
			return;
		}
		if (apps[0] > 0) {
			MijiConnect.getInstance().spendPoints((int) apps[0], this);
		}
		if (apps[1] > 0) {
			Dianle.spendMoney(getParent(), apps[1], this);
		}
		if (apps[2] > 0) {
			AdManager.spendPoints(this, getParent(), apps[2]);
		}
		if (apps[3] > 0) {

		}
	}

	public void onOneChanged(){
		boolean allZero=true;
		for(int i:apps){
			allZero=allZero&&(i==0);
		}
		if(allZero){
			changeMoney.setEnabled(true);
			refreshMoney();
		}
	}
	
	//1
	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		apps[0] = arg1;
		addAllCoin();
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		Toast.makeText(getParent(), "快速通道1金币获取失败", 0).show();
	}

	@Override
	public void getSpendPointsResponse(String arg0, final int arg1) {
		
		AsyncTask<Void, Void, Httpres> task=new AsyncTask<Void, Void, Httpres>(){

			@Override
			protected Httpres doInBackground(Void... arg0) {
				return Static.addquickin.run(new String[]{""+apps[0]});
			}
			
			@Override
			protected void onPostExecute(Httpres result) {
				apps[0]=0;
				addAllCoin();
				onOneChanged();
			}
		};
		task.execute();
	}

	@Override
	public void getSpendPointsResponseFailed(String arg0) {
		Toast.makeText(getParent(), "快速通道1金币兑换失败", 0).show();
	}

	//2
	@Override
	public void getTotalMoneyFailed(String arg0) {
		Toast.makeText(getParent(), "快速通道2金币获取失败", 0).show();
	}

	@Override
	public void getTotalMoneySuccessed(String arg0, long arg1) {
		apps[1] = (int) arg1;
		addAllCoin();
	}

	@Override
	public void spendMoneyFailed(String arg0) {
		Toast.makeText(getParent(), "快速通道2金币兑换失败", 0).show();
	}

	@Override
	public void spendMoneySuccess(final long count) {
		AsyncTask<Void, Void, Httpres> task=new AsyncTask<Void, Void, Httpres>(){

			@Override
			protected Httpres doInBackground(Void... arg0) {
				return Static.addquickin.run(new String[]{""+apps[1]});
			}
			
			@Override
			protected void onPostExecute(Httpres result) {
				apps[1]=0;
				addAllCoin();
				onOneChanged();
			}
		};
		task.execute();
	}

	//3
	@Override
	public void ConsumptionLose(String arg0) {
		Toast.makeText(getParent(), "快速通道3金币兑换失败", 0).show();
	}

	@Override
	public void ConsumptionSuccess(int arg0) {
		AsyncTask<Void, Void, Httpres> task=new AsyncTask<Void, Void, Httpres>(){

			@Override
			protected Httpres doInBackground(Void... arg0) {
				return Static.addquickin.run(new String[]{""+apps[2]});
			}
			
			@Override
			protected void onPostExecute(Httpres result) {
				apps[2]=0;
				addAllCoin();
				onOneChanged();
			}
		};
		task.execute();		
	}
}
