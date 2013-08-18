package com.sf.util;

import android.app.Activity;
import android.content.Intent;

/** �̳и��༴��ʵ����Activity�Ĺ��� */
public abstract class AbsSubActivity extends Activity{
		
	private AbsSubActivity requestSubActivity;
	
	public AbsSubActivity getRequestSubActivity() {
		return requestSubActivity;
	}

	public void setRequestSubActivity(AbsSubActivity requestSubActivity) {
		this.requestSubActivity = requestSubActivity;
	}

	private Class getTargetClass(Intent intent){
		Class clazz = null;
		try {
			if(intent.getComponent() != null)
			clazz = Class.forName(intent.getComponent().getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	// ��д��startActivity()������
	// �������ø÷���ʱ�����Ŀ��Activity�Ƿ�����Activity�����ò�ͬ�ķ���
	@Override
	public void startActivity(Intent intent) {
		if( getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			if(this.getParent() instanceof AbsActivityGroup){
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup)this.getParent()).launchNewActivity(intent);
			}
		}else{
			super.startActivity(intent);
		}
	}

	// ��д��startActivityForResult()������
	// �������ø÷���ʱ�����Ŀ��Activity�Ƿ�����Activity�����ò�ͬ�ķ���
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if( getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			if(this.getParent() instanceof AbsActivityGroup){
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup) this.getParent())
						.launchNewActivityForResult(this, intent, requestCode);
			}
		}else{
			super.startActivityForResult(intent, requestCode);
		}
	}
		
	/** ���ô˷�����������һ������ */
	public void goback() {
		Class clazz = null;
		try {
			clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(this,clazz);
		((AbsActivityGroup)this.getParent()).launchActivity(intent);
	}
	
	/** ���ô˷�����������һ�����沢�������� */
	public void gobackWithResult(int resultCode, Intent data) {
		Class clazz = null;
		try {
			clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		data.setClass(this, clazz);
		if( requestSubActivity != null){
			requestSubActivity.onActivityResult(
					data.getIntExtra("requestCode", 0), resultCode, data);
		}
		((AbsActivityGroup)this.getParent()).launchActivity(data);	
	}
		
}
