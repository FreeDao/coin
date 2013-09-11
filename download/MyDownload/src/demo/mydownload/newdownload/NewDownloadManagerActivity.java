/**   
* @Title: NewDownloadManager.java
* @Package demo.mydownload.newdownload
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-7-29 下午2:57:33
* @version V1.0
*/ 
package demo.mydownload.newdownload;

import java.io.File;
import java.util.List;

import net.tsz.afinal.FinalDBChen;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import demo.mydownload.BaseActivity;
import demo.mydownload.DownloadFile;
import demo.mydownload.DownloadMovieItem;
import demo.mydownload.R;
import demo.mydownload.newdownload.DownloadTask.OnDeleteTaskListener;

/** 
 * @ClassName: NewDownloadManager
 * @Description: 新的下载管理界面
 * @author 陈红建
 * @date 2013-7-29 下午2:57:33
 * 
 */
public class NewDownloadManagerActivity extends BaseActivity 
{
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			//获得需要下载的对象
			DownloadMovieItem down = getMyApp().getStartDownloadMovieItem();
			View view = getLayoutInflater().inflate(R.layout.list_download_item, null);
			down.setDownloadState(DOWNLOAD_STATE_START);
			listview_lin.addView(view);
			//开启下载任务
			new DownloadTask(getmContext(), view, down, false).setOnDeleteTaskListener(new DeleteTask(listview_lin));
		}
	};
	private LinearLayout listview_lin;
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
		setContentView(R.layout.new_activity_download_manager);
		setmContext(NewDownloadManagerActivity.this);
		initView();
		//注册广播
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("download");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
		//遍历数据库得到已有的数据
		FinalDBChen db = new FinalDBChen(getmContext(),getDatabasePath(DBNAME).getAbsolutePath());
		List<DownloadMovieItem> ds = db.findItemsByWhereAndWhereValue(null, null, DownloadMovieItem.class, TABNAME_DOWNLOADTASK, null);
		System.out.println("数据库中已经存在的数据;"+ds.size());
		if( ds.size() != 0) {
			//如果数据库中有数据
			//直接初始化
			for (DownloadMovieItem downloadMovieItem : ds)
			{
//				//遍历所有下载对象
				View view = getLayoutInflater().inflate(R.layout.list_download_item, null);
				listview_lin.addView(view);
				ProgressBar dp = (ProgressBar) view.findViewById(R.id.download_progressBar);//得到进度条
				TextView t = (TextView) view.findViewById(R.id.movie_name_item);
				t.setText(downloadMovieItem.getMovieName());//设置名字
				//设置当前进度百分比
				String stsize = downloadMovieItem.getFileSize();//设置当前进度,和总大小
				TextView cp = (TextView) view.findViewById(R.id.current_progress);
				TextView tsize = (TextView) view.findViewById(R.id.totalsize);
				cp.setText(downloadMovieItem.getPercentage());
				long currentSize = downloadMovieItem.getCurrentProgress();
				String ts = Formatter.formatFileSize(
						getmContext(), currentSize);
				if(stsize.contains("null")) {
					stsize = "0.00B";//如果是因为某种特定的情况未开始下载.将总大小设置为0.0
				}
				tsize.setText(ts+"/"+stsize);
				Button bt = (Button) view.findViewById(R.id.stop_download_bt);
				//设置当前进度
				long count = downloadMovieItem.getProgressCount();
				dp.setMax((int) count);
				dp.setProgress((int) currentSize);
				if(bt.getVisibility() == View.INVISIBLE) {
					//如果是隐藏状态
					bt.setVisibility(View.VISIBLE);
				}
				new DownloadTask(getmContext(), view, downloadMovieItem , true).setOnDeleteTaskListener(new DeleteTask(listview_lin));
				
			}
			
		}
	}
	
	class DeleteTask implements OnDeleteTaskListener{

		
		private LinearLayout lin;
		/** 
		 * Title:
		 * Description:
		 */
		public DeleteTask(LinearLayout lin)
		{
			this.lin = lin;
		}
		/** (非 Javadoc) 
		* Title: onDelete
		* Description:
		* @param taskView
		* @param down
		* @see demo.mydownload.newdownload.DownloadTask.OnDeleteTaskListener#onDelete(android.view.View, demo.mydownload.DownloadMovieItem)
		*/ 
		@Override
		public void onDelete(final View taskView, final DownloadMovieItem down)
		{
			new AlertDialog.Builder(getmContext())
			.setTitle("提示")
			.setMessage("删除"+down.getMovieName()+"?") //二次提示
			.setNegativeButton("取消", new OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{

				}
			})
			.setPositiveButton("确定",  new OnClickListener()
			{

				public void onClick(DialogInterface dialog, int which)
				{
					lin.removeView(taskView);
					//停止这个任务
					DownloadFile d = down.getDownloadFile();
					if(d != null) {
						d.stopDownload();
					}
					//删除本地文件
					File df = new File(down.getFilePath());
					if(df.exists()) {
						//如果文件存在
						df.delete();
					}
					
					//删除数据库中的内容
					new FinalDBChen(getmContext(), DBNAME).deleteItem(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()});
					//发送一个删除文件的广播.让主页的下载按钮变为可下载
					Intent i = new Intent();
					i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_DELETE);
					i.setAction(DOWNLOAD_TYPE);
					getMyApp().setDownloadSuccess(down);
					sendBroadcast(i);
				}
			}).show();
		}
		
	}
	/** (非 Javadoc) 
	* Title: initView
	* Description:
	* @see demo.mydownload.BaseActivity#initView()
	*/ 
	@Override
	public void initView()
	{
		super.initView();
		listview_lin = (LinearLayout) findViewById(R.id.download_listview_lin);
	}
	/** (非 Javadoc) 
	* Title: onClick
	* Description:
	* @param view
	* @see demo.mydownload.BaseActivity#onClick(android.view.View)
	*/ 
	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		
	}
}

