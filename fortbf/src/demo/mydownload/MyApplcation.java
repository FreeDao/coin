/**   
* @Title: MyApplcation.java
* @Package demo.myframe.contentvalue
* @Description: TODO(用一句话描述该文件做�?��)
* @author haicuan139
* @date 2013-3-21 下午4:27:34
* @version V1.0
*/ 
package demo.mydownload;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.support.v4.view.ViewPager;

/** 
 * @ClassName: MyApplcation
 * @Description: 公共环境
 * @author 陈红�?
 * @date 2013-3-21 下午4:27:34
 * 
 */
public class MyApplcation extends Application
{

	
	private DownloadMovieItem stopOrStartDownloadMovieItem; //需要下载的任务(上一个版本实用)
	private DownloadMovieItem startDownloadMovieItem; //需要下载的任务
	private DownloadMovieItem downloadSuccess;
	private ViewPager pager;
	private List<DownloadMovieItem> downloadItems = new ArrayList<DownloadMovieItem>(); //下载队列


	public List<DownloadMovieItem> getDownloadItems()
	{
		return downloadItems;
	}

	public void setDownloadItems(List<DownloadMovieItem> downloadItems)
	{
		this.downloadItems = downloadItems;
	}

	public DownloadMovieItem getStartDownloadMovieItem()
	{
		return startDownloadMovieItem;
	}

	public void setStartDownloadMovieItem(DownloadMovieItem startDownloadMovieItem)
	{
		this.startDownloadMovieItem = startDownloadMovieItem;
	}

	public DownloadMovieItem getStopOrStartDownloadMovieItem()
	{
		return stopOrStartDownloadMovieItem;
	}

	public void setStopOrStartDownloadMovieItem(
			DownloadMovieItem stopOrStartDownloadMovieItem)
	{
		this.stopOrStartDownloadMovieItem = stopOrStartDownloadMovieItem;
	}

	public DownloadMovieItem getDownloadSuccess()
	{
		return downloadSuccess;
	}

	public void setDownloadSuccess(DownloadMovieItem downloadSuccess)
	{
		this.downloadSuccess = downloadSuccess;
	}

	public ViewPager getPager()
	{
		return pager;
	}

	public void setPager(ViewPager pager)
	{
		this.pager = pager;
	}




}

