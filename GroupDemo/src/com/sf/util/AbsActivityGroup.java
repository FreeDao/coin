package com.sf.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.sf.R;

/** �̳и��ಢʵ����������󷽷����� */
public abstract class AbsActivityGroup extends ActivityGroup{
	
	/** ԴIntent */
	protected Intent fromIntent;
	
	/** ����ģ����ת��Ŀ��Intent */
	protected Intent targetIntent = new Intent();
	
//	/** Group�ı���ؼ� */ 
//	protected TextView topBarTitleTextView;
//	
//	/** Group�ı������� */ 
//	protected String topBarName = "";
	
	/** ����������Activity�Ĳ��� */
	private LinearLayout container = null;
	
	/** ѡ� */
	private RadioGroup radioGroup = null;
	
	/** ѡ�б�ǩ��ID */
	private int radioGroupCheckId;
	
	/** �л���ǩ�����ı�־λ */
	private boolean changedFlag;
	
	/** ѡ������б�ǩ */
	private RadioButton[] radioButtons = null;
	
	/** ѡ����б�ǩ��ID */
	private int[] radioButtonIds = null;
	
	/** ѡ����б�ǩ��ͼ��ID */
	private int[] radioButtonImageIds;
	
	/** ѡ����б�ǩ������ */
	private String[] radioButtonTexts;
	
	/** ��ǩID��Ӧ�ĳ�ʼActivity���� */
	private Map<Integer,Class<? extends Activity>> classes = new HashMap<Integer,Class<? extends Activity>>();
	
	/** ��ǩID��Ӧ�ĵ�ǰActivity���� */
	public Map<Integer,Class<? extends Activity>> currentClasses = new HashMap<Integer,Class<? extends Activity>>();;
	
	/**
	 * ��������ʵ�ֵ��趨���ֵķ�����Activity�Ĳ��ֵ�id����Ϊactivity_group_container��
	 * ѡ���id����Ϊactivity_group_radioGroup
	 */
	protected abstract int getLayoutResourceId();
	
	/** ����������Ҫʵ�ֵĻ�ȡѡ����б�ǩ��ID�ķ��� */ 
	protected abstract int[] getRadioButtonIds();
	
	/** ����������Ҫʵ�ֵĻ�ȡѡ����б�ǩ��ͼ��ķ��� */ 
	protected abstract int[] getRadioButtonImageIds();
	
	/** ����������Ҫʵ�ֵĻ�ȡѡ����б�ǩ�����ֵķ��� */ 
	protected abstract String[] getRadioButtonTexts();
	
	/** ����������Ҫʵ�ֵĻ�ȡѡ����б�ǩID��Ӧ�ĳ�ʼActivity�ķ��� */ 
	public abstract Class<? extends Activity>[] getClasses();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());         
        // ��ȡԴIntent
		fromIntent = getIntent();
		// �趨ԭʼ����
        setData();
        // ��ʼ���ؼ�
        initWidgetStatic();
    }
			
	/** �趨����Դ�ķ��� */ 
	protected  void setData(){
		radioButtonIds = getRadioButtonIds();
		radioButtonImageIds = getRadioButtonImageIds();
		radioButtonTexts = getRadioButtonTexts();
		for(int i=0;i<radioButtonIds.length;i++){
			classes.put(radioButtonIds[i], getClasses()[i]);
			currentClasses.put(radioButtonIds[i], getClasses()[i]);
		}
	}
	
	/** ��ʼ���ؼ� */ 
	protected void initWidgetStatic(){
		container = (LinearLayout) findViewById(R.id.activity_group_container);
		radioGroup = (RadioGroup) findViewById(R.id.activity_group_radioGroup);
		radioButtons = new RadioButton[radioButtonIds.length];
		for(int i=0;i<radioButtons.length;i++){
			radioButtons[i] = (RadioButton) findViewById(radioButtonIds[i]);
			if(radioButtonImageIds != null){
				radioButtons[i].setText(radioButtonTexts[i]);
				Drawable drawable = getResources().getDrawable(radioButtonImageIds[i]);		
				radioButtons[i].setCompoundDrawablesWithIntrinsicBounds(null,
						drawable, null, null);
			}
			radioButtons[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!changedFlag){
						targetIntent.setClass(AbsActivityGroup.this,
								classes.get(radioGroupCheckId));
						launchActivity(targetIntent);
					}
					changedFlag = false;
				}
			});
		}

		// ��ѡ��趨����
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
						.getCurrentActivity().getClass());
				setTargetIntent(checkedId);	
				launchActivity(targetIntent);
				radioGroupCheckId = checkedId;
				changedFlag = true;
			}
		});	
		
		// ��ʼ������
		radioGroupCheckId = getCheckedRadioButtonId();
		setTargetIntent(radioGroupCheckId);
		launchNewActivity(targetIntent);
	}
	
	/** �趨Ŀ��Intent�ķ��� */
	protected void setTargetIntent(int checkedId){
		targetIntent.setClass(AbsActivityGroup.this, currentClasses.get(checkedId));
	}
	
	/** ActivityGroup�����µ���Activity�ķ���(�����µ�) */ 
	public void launchNewActivity(Intent intent) {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
					intent.getComponent().getShortClassName()+ getCheckedRadioButtonId(),
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
	}
	
	/** ActivityGroup�����µ���Activity�ķ���(�����µ�) */ 
	public void launchNewActivityForResult(AbsSubActivity requestSubActivity,
			Intent intent, int requestCode) {
		intent.putExtra("requestCode", requestCode);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				intent.getComponent().getShortClassName()+getCheckedRadioButtonId(), 
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
		((AbsSubActivity)getCurrentActivity()).setRequestSubActivity(requestSubActivity);
	}
	
	/** ActivityGroup������Activity�ķ���(�ȿ���û�У��������ԭ���ģ����򴴽��µ�) */ 
	public void launchActivity(Intent intent) {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				intent.getComponent().getShortClassName()+getCheckedRadioButtonId(), 
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView());
	}
	
	/** ����������ڻ�ȡ��ǰActivityGroup��ѡ����µĵ�ѡ��ť��ID */  
	public int getCheckedRadioButtonId(){
		return radioGroup.getCheckedRadioButtonId();
	}
	
//	/** ����������ڻ�ȡ��ǰActivityGroup��topBarName */ 
//	public String getTopBarName(){
//		return topBarName;
//	}

}
