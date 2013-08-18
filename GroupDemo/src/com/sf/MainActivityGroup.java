package com.sf;

import android.app.Activity;

import com.sf.util.AbsActivityGroup;

// �̳�AbsActivityGroup��ʵ����������󷽷�����
public class MainActivityGroup extends AbsActivityGroup{

	// ��һ����Ҫʵ�ֵķ�����ֱ�ӷ���ActivityGroupʵ�����layou���ּ���
	// ע��ò���һ��Ҫ�и�idΪactivity_group_container�Ĳ�����������Activity�Ĳ���
	@Override
	protected int getLayoutResourceId() {
		// ��������ѡ�
		return R.layout.activity_group_bottom5_layout;
		// �������������ѡ������Է��������������
		//return R.layout.activity_group_left5_layout;
	}

	// �ڶ�����Ҫʵ�ֵķ���������layout������ѡ���Ӧ��radioButton��id
	@Override
	protected int[] getRadioButtonIds() {
		return new int[] { R.id.activity_group_radioButton0,
				R.id.activity_group_radioButton1,
				R.id.activity_group_radioButton2,
				R.id.activity_group_radioButton3,
				R.id.activity_group_radioButton4 };
	}

	// ��������Ҫʵ�ֵķ���������һ�������е�radioButton��Ӧ��ͼ�꣬ע��ͼ��ĳߴ�Ҫ�Լ����������ʴ�С
	@Override
	protected int[] getRadioButtonImageIds() {
		return new int[] { R.drawable.icon2, R.drawable.icon2, R.drawable.icon2,
				R.drawable.icon2, R.drawable.icon2, };
	}

	// ���ĸ���Ҫʵ�ֵķ�����radioButton��Ӧ�����֣�Ҳ����ѡ���ǩ�����֣�
	// ��ò�Ҫ̫��������Ҫ�������ļ���������ִ�С����Ӧ����
	@Override
	protected String[] getRadioButtonTexts() {
		return new String[]{"A","B","C","D","E"};
	}

	// �������Ҫʵ�ֵķ���������ÿ��ѡ���Ӧ�ĵ�һ����Activity��ע��Ҫ�̳���AbsSubActivity��
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Activity>[] getClasses() {
		Class<? extends Activity>[] classes = new Class[] { A1Activity.class,
				B1Activity.class, C1Activity.class, D1Activity.class,
				E1Activity.class };
		return classes;
	}

}