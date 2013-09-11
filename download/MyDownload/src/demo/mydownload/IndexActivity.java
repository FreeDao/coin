/**   
* @Title: IndexActivity.java
* @Package demo.mydownload
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-7-31 下午2:36:48
* @version V1.0
*/ 
package demo.mydownload;

import java.util.ArrayList;
import java.util.List;

import demo.mydownload.newdownload.NewDownloadManagerActivity;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/** 
 * @ClassName: IndexActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 陈红建
 * @date 2013-7-31 下午2:36:48
 * 
 */
public class IndexActivity extends ActivityGroup
{

	private ViewPager pager;

	/** (非 Javadoc) 
	* Title: onCreate
	* Description:
	* @param savedInstanceState
	* @see android.app.Activity#onCreate(android.os.Bundle)
	*/ 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_activity);
		pager = (ViewPager) findViewById(R.id.viewPager);
		List<View> mListViews = new ArrayList<View>();
		MyApplcation app = (MyApplcation) getApplication();
		// 初始化四个Tab界面
		mListViews.add(getLocalActivityManager().startActivity(
				"HomePageActivity",
				new Intent(this, MainActivity.class)).getDecorView());
		mListViews.add(getLocalActivityManager().startActivity(
				"ClassifyActivity",
				new Intent(this, NewDownloadManagerActivity.class)).getDecorView());
		pager.setAdapter(new ViewPagerAdapter(mListViews));
		app.setPager(pager);//将ViewPager保存成全局.让主页的ListView可以操作这个对象
		pager.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
		
	}
	/** (非 Javadoc) 
	* Title: finish
	* Description:
	* @see android.app.Activity#finish()
	*/ 
	@Override
	public void finish()
	{
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("是否要退出？") //二次提示
		.setNegativeButton("是", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				//在这里直接将虚拟机退出.如果用finish.需要将下载任务转入后台Service.因为进程会被回收或杀死.
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		})
		.setPositiveButton("否",  new OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		}).show();
	}
	public void onClick(View view) {
		switch (view.getId())
		{
		case R.id.index:
			pager.setCurrentItem(0, false);
			break;
		case R.id.downloadmanager:
			pager.setCurrentItem(1, false);
			break;

		default:
			break;
		}
	}
}

