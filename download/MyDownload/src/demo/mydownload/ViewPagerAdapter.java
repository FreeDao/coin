/**   
* @Title: ViewPagerAdapter.java
* @Package com.cloud.coupon.adapter
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-7-2 下午4:58:55
* @version V1.0
*/ 
package demo.mydownload;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/** 
 * @ClassName: ViewPagerAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 陈红建
 * @date 2013-7-2 下午4:58:55
 * 
 */
public class ViewPagerAdapter extends PagerAdapter
{

	public List<View> mListViews;

	public ViewPagerAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mListViews.get(arg1));
	}
	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mListViews.get(arg1), 0);
		return mListViews.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}
}

