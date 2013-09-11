/**   
 * @Title: DownloadTask.java
 * @Package demo.mydownload.newdownload
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 陈红建
 * @date 2013-8-1 上午9:25:23
 * @version V1.0
 */
package demo.mydownload.newdownload;

import java.io.File;

import net.tsz.afinal.FinalDBChen;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import demo.mydownload.ContentValue;
import demo.mydownload.DownloadFile;
import demo.mydownload.DownloadMovieItem;
import demo.mydownload.MyApplcation;
import demo.mydownload.R;

/**
 * @ClassName: DownloadTask
 * @Description:管理下载任务
 * @author 陈红建
 * @date 2013-8-1 上午9:25:23
 * 
 */
public class DownloadTask implements ContentValue
{

	private Context mContext;
	private View view; //条目的View
	private DownloadMovieItem down; //下载任务
	private boolean comeDb;
	private FinalDBChen db;
	/**
	 * Title: Description:
	 */
	public DownloadTask(Context mContext, View view, DownloadMovieItem down , boolean comeDb)
	{
		this.mContext = mContext;
		this.view = view;
		this.down = down;
		this.comeDb = comeDb;
		ImageView del = (ImageView) view.findViewById(R.id.delete_movie);
		del.setOnClickListener(new DeleteClick());
		 db = new FinalDBChen(mContext, mContext.getDatabasePath(DBNAME).getAbsolutePath());
		gotoDownload(down , view);
	}
	
	public void gotoDownload( DownloadMovieItem down , View view)
	{
			String url = down.getDownloadUrl();
			String path = down.getFilePath();
			if(comeDb) {
				//如果数据来自数据库,将按钮背景设置为开始
				//添加单机事件
				Button bt = (Button) view.findViewById(R.id.stop_download_bt);
				bt.setBackgroundResource(R.drawable.start);
				bt.setOnClickListener(new MyOnClick(DOWNLOAD_DB, down, bt));
			}else {
				//直接下载
				down.setDownloadFile(new DownloadFile().startDownloadFileByUrl(url, path,new CallBackFuc(view, down)));
			}
	}

	public OnDeleteTaskListener getOnDeleteTaskListener()
	{
		return onDeleteTaskListener;
	}

	public void setOnDeleteTaskListener(OnDeleteTaskListener onDeleteTaskListener)
	{
		this.onDeleteTaskListener = onDeleteTaskListener;
	}

	public class MyOnClick implements OnClickListener
	{
		private int state;
		private DownloadMovieItem downItem;
		private boolean clickState = false;
		private Button button;
		private TextView current_progress;

		/**
		 * Title: Description:
		 */
		public MyOnClick(int state, DownloadMovieItem downItem,
				Button button)
		{
			this.state = state;
			this.downItem = downItem;
			this.button = button;
		}

		/**
		 * (非 Javadoc) Title: onClick Description:
		 * 
		 * @param v
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v)
		{
			String name = downItem.getMovieName();

			switch (state)
			{
			case DOWNLOAD_STATE_SUCCESS:
				// 下载完成
				Toast.makeText(mContext, name + ":开始播放!", Toast.LENGTH_SHORT)
						.show();
				break;
			case DOWNLOAD_STATE_FAIL:
				// 下载失败,与 开始可以执行同一个逻辑
				Toast.makeText(mContext, name + "：重新开始这个任务", Toast.LENGTH_SHORT)
						.show();
				button.setVisibility(View.INVISIBLE);
				current_progress.setTextColor(Color
						.parseColor("#23b5bc"));
				current_progress.setText("等待中");
				gotoDownload( down , view);
				break;
			case DOWNLOAD_STATE_START:
				if (clickState)
				{
					// 如果是第一次点击,默认状态下是 开始状态.点击之后暂停这个任务
					gotoDownload( down , view);
					Toast.makeText(mContext, name + ":开始下载",
							Toast.LENGTH_SHORT).show();
					if (button != null)
					{
						// button.setBackgroundResource(R.drawable.stop);
						button.setVisibility(View.INVISIBLE);
						current_progress.setTextColor(Color
								.parseColor("#23b5bc"));
						current_progress.setText("等待中");
					}
					clickState = false;
				}
				else
				{
					down.getDownloadFile().stopDownload();
					Toast.makeText(mContext, name + ":暂停", Toast.LENGTH_SHORT)
							.show();
					if (button != null)
					{
						button.setBackgroundResource(R.drawable.start);
					}
					clickState = true;
				}
				break;
			case DOWNLOAD_DB:
				//点击之后开启下载任务
				String url = down.getDownloadUrl();
				String path = down.getFilePath();
				down.setDownloadFile(new DownloadFile().startDownloadFileByUrl(url, path,new CallBackFuc(view, down)));
				break;
			default:
				break;
			}
		}

		public TextView getCurrent_progress()
		{
			return current_progress;
		}

		public void setCurrent_progress(TextView current_progress)
		{
			this.current_progress = current_progress;
		}

	}
	
	private OnDeleteTaskListener onDeleteTaskListener;
	public interface OnDeleteTaskListener{
		//当点击删除时执行的回调
		public void onDelete(View taskView , DownloadMovieItem down);
	}
	//删除一个指定的View
	class DeleteClick implements OnClickListener{

//		/** 
//		 * Title:
//		 * Description:
//		 */
//		public DeleteClick(View view , LinearLayout lin)
//		{
//			
//		}
		/** (非 Javadoc) 
		* Title: onClick
		* Description:
		* @param v
		* @see android.view.View.OnClickListener#onClick(android.view.View)
		*/ 
		@Override
		public void onClick(View v)
		{
			if( onDeleteTaskListener != null) {
				onDeleteTaskListener.onDelete(view, down);
			}
		}
		
	}
	class CallBackFuc extends AjaxCallBack<File>{

		private ProgressBar p;
		private TextView kb;
		private TextView totalSize;
		private int cu;
		private Button stop_download_bt;
		private TextView current_progress;
		private TextView movie_name_item;
		private View view;
		private DownloadMovieItem down;
		
		/** 
		 * Title:
		 * Description:
		 */
		public CallBackFuc(View view , DownloadMovieItem down)
		{
			this.down = down;
			this.view = view;
			
			if (view != null)
			{
				p = (ProgressBar) view
						.findViewById(R.id.download_progressBar);
				kb = (TextView) view
						.findViewById(R.id.movie_file_size);
				totalSize = (TextView) view
						.findViewById(R.id.totalsize);
				stop_download_bt = (Button) view
						.findViewById(R.id.stop_download_bt);
				stop_download_bt
						.setBackgroundResource(R.drawable.stop);
				current_progress = (TextView) view
						.findViewById(R.id.current_progress);
				movie_name_item = (TextView) view.findViewById(R.id.movie_name_item);
				stop_download_bt.setVisibility(View.INVISIBLE);
				
				stop_download_bt
						.setBackgroundResource(R.drawable.stop);
				movie_name_item.setText(down.getMovieName());
				current_progress.setTextColor(Color
						.parseColor("#23b5bc"));
				current_progress.setText("等待中");
				MyOnClick mc = new MyOnClick(
						DOWNLOAD_STATE_START, down,
						stop_download_bt);
				mc.setCurrent_progress(current_progress);
				stop_download_bt.setOnClickListener(mc);
			}
			else
			{
				System.out.println("View为空");
			}
		}

		/**
		 * (非 Javadoc) Title: onStart Description:
		 * 
		 * @see net.tsz.afinal.http.AjaxCallBack#onStart()
		 */
		@Override
		public void onStart()
		{
			System.out.println(down.getMovieName() + ":开始下载");
			Toast.makeText(mContext,
					down.getMovieName() + "：开始下载",
					Toast.LENGTH_SHORT).show();
			//将数据库中的下载状态改为下载中
//			ContentValues values = new ContentValues();
//			values.put("downloadState", DOWNLOAD_STATE_START+"");
//			db.updateValue(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()}, values);
			down.setDownloadState(DOWNLOAD_STATE_START);
			db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()}, down);
			
			//发送开始下载的广播
			Intent i = new Intent();
			i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_START);
			i.setAction(DOWNLOAD_TYPE);
			MyApplcation app = (MyApplcation) mContext.getApplicationContext();
			app.setDownloadSuccess(down);
			mContext.sendBroadcast(i);
			super.onStart();
		}

		/**
		 * (非 Javadoc) Title: onLoading Description:
		 * 
		 * @param count
		 * @param current
		 * @see net.tsz.afinal.http.AjaxCallBack#onLoading(long,
		 *      long)
		 */
		@Override
		public void onLoading(long count, long current)
		{

			int cus = 0;
			if (current > cu)
			{
				cus = (int) (current - cu);
				cu = (int) current;// 得到上一秒的进度
			}
			String m = Formatter.formatFileSize(mContext, cus)
					+ "/s";
			// 获得当前进度百分比
			int pc = (int) ((current * 100) / count);
			
			if (view != null)
			{

				
				String currentSize = Formatter.formatFileSize(
						mContext, current);
				current_progress.setText(pc + "%");
				down.setPercentage(pc + "%");//设置百分比
				down.setProgressCount(count);//设置总大小进度条中使用
				down.setCurrentProgress(current);
				String tsize =  Formatter.formatFileSize(
						mContext, count);//总大小
				down.setFileSize(tsize);//设置总大小字符串
				totalSize.setText(currentSize +"/"+tsize);
				kb.setText(m);
				if (kb.getVisibility() == View.INVISIBLE)
				{
					kb.setVisibility(View.VISIBLE);
				}

				p.setMax((int) count);
				p.setProgress((int) current);
				// 如果按钮被隐藏了.将其显示
				if (stop_download_bt.getVisibility() == View.INVISIBLE)
				{
					stop_download_bt
							.setVisibility(View.VISIBLE);
					stop_download_bt.setText("");
					stop_download_bt
							.setBackgroundResource(R.drawable.stop);
				}
				//将数据保存到数据库中
				down.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
				db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()}, down);
				
			}
			
		}

		/**
		 * (非 Javadoc) Title: onSuccess Description:
		 * 
		 * @param t
		 * @see net.tsz.afinal.http.AjaxCallBack#onSuccess(java.lang.Object)
		 */
		@Override
		public void onSuccess(File t)
		{
			System.out.println(down.getMovieName() + "：下载完成");
			Toast.makeText(mContext,
					down.getMovieName() + "：下载完成",
					Toast.LENGTH_SHORT).show();
			if (view != null)
			{
				kb.setVisibility(View.INVISIBLE);
				current_progress.setText("下载完成");
				stop_download_bt
						.setBackgroundResource(R.drawable.start);
				stop_download_bt
						.setOnClickListener(new MyOnClick(
								DOWNLOAD_STATE_SUCCESS, down,
								stop_download_bt));
			}
			// 更新数据库的状态为下载完成
//			ContentValues values = new ContentValues();
//			values.put("downloadState", DOWNLOAD_STATE_SUCCESS+"");
//			db.updateValue(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()}, values);
			down.setDownloadState(DOWNLOAD_STATE_SUCCESS);
			db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()}, down);
			
			//发送下载完成的广播
			Intent i = new Intent();
			i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_SUCCESS);
			i.setAction(DOWNLOAD_TYPE);
			MyApplcation app = (MyApplcation) mContext.getApplicationContext();
			app.setDownloadSuccess(down);
			mContext.sendBroadcast(i);
			super.onSuccess(t);
		}

		/**
		 * (非 Javadoc) Title: onFailure Description:
		 * 
		 * @param t
		 * @param strMsg
		 * @see net.tsz.afinal.http.AjaxCallBack#onFailure(java.lang.Throwable,
		 *      java.lang.String)
		 */
		@Override
		public void onFailure(Throwable t, String strMsg)
		{
		
			
			System.out.println("下载失败：" + strMsg);
			// 更新数据库的状态为下载失败
			if(TextUtils.isEmpty(down.getFileSize())) {
				//下载失败的情况设置总大小为0.0B,如果因为网络原因导致.没有执行OnLoading方法
				down.setFileSize("0.0B");
			}
			down.setDownloadState(DOWNLOAD_STATE_FAIL);
			db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()}, down);
			
			if (!TextUtils.isEmpty(strMsg)
					&& strMsg.contains("416"))
			{
				//发送下载失败的广播
				//如果是已经下载完成了,发送一个下载完成的广播
				Intent i = new Intent();
				i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_SUCCESS);
				i.setAction(DOWNLOAD_TYPE);
				MyApplcation app = (MyApplcation) mContext.getApplicationContext();
				app.setDownloadSuccess(down);
				mContext.sendBroadcast(i);
				// 当文件已经下载完成时.又重复对此任务进行了下载操作.FinalHttp不会带着完整的文件头去请求服务器.这样会缠身搞一个416的错误
				// response status error code:416
				// 通过判断失败原因中是否包含416来决定文件是否是已经下载完成的状态
				// 也可以通过 ：判断要下载的文件是否存在.得到文件的总长度与请求服务器返回的count长度
				// .就是在onLoading中回调的count总长度进行比较.如果本地文件大于等于服务器文件的总厂说明这个文件已经下载完成.
				// 将按钮图片显示并设置状态为已经播放的状态
				String m = Formatter.formatFileSize(mContext,new File(down.getFilePath()).length());
				if (view != null)
				{

					kb.setVisibility(View.INVISIBLE);
					// 得到文件的总长度
					totalSize.setText(m);
					current_progress.setText("下载完成");
					//将进度条的值设为满状态
					p.setMax(100);
					p.setProgress(100);
					
					stop_download_bt
							.setVisibility(View.VISIBLE);
					stop_download_bt
							.setBackgroundResource(R.drawable.start);
					stop_download_bt
							.setOnClickListener(new MyOnClick(
									DOWNLOAD_STATE_SUCCESS,
									down,  stop_download_bt));
				}

			}
			else
			{
				if (view != null)
				{
					// 隐藏KB/S
					kb.setVisibility(View.INVISIBLE);
					if (stop_download_bt.getVisibility() == View.INVISIBLE)
					{
						stop_download_bt
								.setVisibility(View.VISIBLE);
					}
					stop_download_bt
							.setBackgroundResource(R.drawable.button_bg_retry);
					stop_download_bt.setText("重试");
					stop_download_bt.setTextColor(Color
							.parseColor("#333333"));
					current_progress.setTextColor(Color
							.parseColor("#f39801"));
					current_progress.setText("下载失败");
					MyOnClick c = new MyOnClick(
							DOWNLOAD_STATE_FAIL, down,
							stop_download_bt);
					c.setCurrent_progress(current_progress);
					stop_download_bt
							.setOnClickListener(c);
					//发送下载失败的广播
					Intent i = new Intent();
					i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_FAIL);
					i.setAction(DOWNLOAD_TYPE);
					MyApplcation app = (MyApplcation) mContext.getApplicationContext();
					app.setDownloadSuccess(down);
					mContext.sendBroadcast(i);
					
				}
				Toast.makeText(
						mContext,
						down.getMovieName()
								+ "：下载失败!可能是网络超时或内存卡空间不足",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
