package com.sf;

import android.app.Activity;

import com.sf.util.AbsActivityGroup;

// 继承AbsActivityGroup并实现其五个抽象方法即可
public class MainActivityGroup extends AbsActivityGroup{

	// 第一个需要实现的方法，直接返回ActivityGroup实现类的layou布局即可
	// 注意该布局一定要有个id为activity_group_container的布局用来放子Activity的布局
	@Override
	protected int getLayoutResourceId() {
		// 横向排列选项卡
		return R.layout.activity_group_bottom5_layout;
		// 如果是纵向排列选项卡，可以返回下面这个布局
		//return R.layout.activity_group_left5_layout;
	}

	// 第二个需要实现的方法，返回layout布局下选项卡对应的radioButton的id
	@Override
	protected int[] getRadioButtonIds() {
		return new int[] { R.id.activity_group_radioButton0,
				R.id.activity_group_radioButton1,
				R.id.activity_group_radioButton2,
				R.id.activity_group_radioButton3,
				R.id.activity_group_radioButton4 };
	}

	// 第三个需要实现的方法，上面一个方法中的radioButton对应的图标，注意图标的尺寸要自己调整到合适大小
	@Override
	protected int[] getRadioButtonImageIds() {
		return new int[] { R.drawable.icon2, R.drawable.icon2, R.drawable.icon2,
				R.drawable.icon2, R.drawable.icon2, };
	}

	// 第四个需要实现的方法，radioButton对应的文字，也就是选项卡标签的文字，
	// 最好不要太长，否则要到布局文件里调整文字大小到适应界面
	@Override
	protected String[] getRadioButtonTexts() {
		return new String[]{"A","B","C","D","E"};
	}

	// 第五个需要实现的方法，返回每个选项卡对应的第一个子Activity（注意要继承自AbsSubActivity）
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Activity>[] getClasses() {
		Class<? extends Activity>[] classes = new Class[] { A1Activity.class,
				B1Activity.class, C1Activity.class, D1Activity.class,
				E1Activity.class };
		return classes;
	}

}