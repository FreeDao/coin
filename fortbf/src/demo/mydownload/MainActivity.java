package demo.mydownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDBChen;

import org.jq.nbz.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity
{

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{

			//拿到下载状态
			int state = intent.getIntExtra(DOWNLOAD_TYPE, 0);
			//得到下载完成的对象
			DownloadMovieItem down = getMyApp().getDownloadSuccess();
			int position = down.getPosition();//对应的位置
			System.out.println("对应的位置为："+position);
			switch (state)
			{
			case DOWNLOAD_STATE_DELETE:
				//任务删除,可以重新下载
//				Toast.makeText(getApplicationContext(),"接到了删除的广播!", Toast.LENGTH_SHORT).show();
				if( adapter != null) {
					adapter.getItems().get(position).setDownloadState(DOWNLOAD_STATE_DELETE);
					adapter.notifyDataSetChanged();
				}
				break;
			case DOWNLOAD_STATE_SUCCESS:
				//下载完成,可以进行完成之后的操作
//				Toast.makeText(getApplicationContext(),"接到了下载完成的广播!", Toast.LENGTH_SHORT).show();
				if(adapter != null) {
					adapter.getItems().get(position).setDownloadState(DOWNLOAD_STATE_SUCCESS);
					adapter.notifyDataSetChanged();
				}
				break;
			case DOWNLOAD_STATE_FAIL:
				//下载失败
				if(adapter != null) {
					adapter.getItems().get(position).setDownloadState(DOWNLOAD_STATE_FAIL);
					adapter.notifyDataSetChanged();
				}
				break;
			case DOWNLOAD_STATE_START:
				//开始下载
				if(adapter != null) {
					adapter.getItems().get(position).setDownloadState(DOWNLOAD_STATE_START);
					adapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		}
	};
	private ListView listView;
	private MyAdapter adapter;
	private FinalDBChen db;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setmContext(MainActivity.this);
		initView();
		//注册广播
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(DOWNLOAD_TYPE);
		registerReceiver(mBroadcastReceiver, myIntentFilter);
		
		db = new FinalDBChen(getmContext(),getDatabasePath(DBNAME).getAbsolutePath());
		List<DownloadMovieItem> ds = db.findItemsByWhereAndWhereValue(null, null, DownloadMovieItem.class, TABNAME_DOWNLOADTASK, null);
		if(ds.size() != 0) {
			if(adapter != null) {
			for (DownloadMovieItem downloadMovieItem : ds)
			{
				//拿到数据库中下载的条目,与当前列表进行匹配.然后改变其下载状态
					List<DownloadMovieItem> items = adapter.getItems();
					for (DownloadMovieItem item : items)
					{
						if(downloadMovieItem.getMovieName().equals(item.getMovieName())) {
							//如果找到了这条记录.将这条记录的 状态更新
							item.setDownloadState(downloadMovieItem.getDownloadState());
						}
					}
				}
			adapter.notifyDataSetChanged();//更新这个列表
			}
			
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
		//创建一个数据库
		new FinalDBChen(getmContext(),DBNAME, new DownloadMovieItem(), TABNAME_DOWNLOADTASK, null);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new MyAdapter(initTestData());
		listView.setAdapter(adapter);
	}
	
	/** 
	* @Title: initTestData
	* @Description: 
	* @param @return
	* @return List<DownloadMovieItem>
	* @author 陈红建
	* @throws 
	*/ 
	private List<DownloadMovieItem> initTestData()
	{
		List<DownloadMovieItem> items = new ArrayList<DownloadMovieItem>();
		for (int i = 0; i < 4; i++)
		{
			DownloadMovieItem d = new DownloadMovieItem();
			String url  = "http://down.qq.com/cof/dltools/XD_V1.11.40.3_XFDL_signed.exe";//测试链接
			String path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),i+".mp4").getAbsolutePath();//写死的格式为.MP4
			d.setDownloadUrl(url);
			d.setFilePath(path);
			d.setDownloadState(DOWNLOAD_STATE_WATTING);//设置默认状态
			d.setMovieName("test"+i);
			items.add(d);
		}
		return items;
	}

	class MyAdapter extends MyBaseAdapter{
		
		private List<DownloadMovieItem> items;
		/** 
		 * Title:
		 * Description:
		 */
		public MyAdapter(List<DownloadMovieItem> items)
		{
			this.setItems(items);
		}
		/** (非 Javadoc) 
		* Title: getCount
		* Description:
		* @return
		* @see demo.mydownload.MyBaseAdapter#getCount()
		*/ 
		@Override
		public int getCount()
		{
			return getItems().size();
		}
		/** (非 Javadoc) 
		* Title: getView
		* Description:
		* @param position
		* @param convertView
		* @param parent
		* @return
		* @see demo.mydownload.MyBaseAdapter#getView(int, android.view.View, android.view.ViewGroup)
		*/ 
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			//未做内存优化处理
			DownloadMovieItem down = items.get(position);
			View view = null;
			ViewHolder holder = null;
			if(convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else {
				
				view = View.inflate(getmContext(), R.layout.list_main_item, null);
				holder = new ViewHolder();
				holder.bt = (Button) view.findViewById(R.id.download_bt);
				holder.download_name = (TextView) view.findViewById(R.id.download_name);
				view.setTag(holder);
			}
			holder.download_name.setText(down.getMovieName());
			int state = down.getDownloadState();
			holder.bt.setOnClickListener(new DownloadClick(down,holder.bt,position));
			switch (state)
			{
			case DOWNLOAD_STATE_DELETE:
				//可下载
				holder.bt.setClickable(true);
				holder.bt.setText("下载");
				break;
			case DOWNLOAD_STATE_SUCCESS:
				//播放打开等等操作
				holder.bt.setClickable(true);
				holder.bt.setText("打开");
				break;
			case DOWNLOAD_STATE_WATTING:
				holder.bt.setClickable(true);
				holder.bt.setText("下载");
				break;
			case DOWNLOAD_STATE_FAIL:
				//下载失败
				holder.bt.setClickable(true);
				holder.bt.setText("下载失败");
				break;
			case DOWNLOAD_STATE_DOWNLOADING:
				//下载中
				holder.bt.setClickable(false);
				holder.bt.setText("下载中");
				break;
			case DOWNLOAD_STATE_START:
				//开始下载状态
				holder.bt.setClickable(false);
				holder.bt.setText("下载中");
				break;
			default:
				break;
			}
			return view;
		}
		public List<DownloadMovieItem> getItems()
		{
			return items;
		}
		public void setItems(List<DownloadMovieItem> items)
		{
			this.items = items;
		}
		
	}
	static class ViewHolder{
		Button bt;
		TextView download_name;
	}
	class DownloadClick implements OnClickListener{

		private DownloadMovieItem down;
		private Button bt;
		private int position;
		/** 
		 * Title:
		 * Description:
		 */
		public DownloadClick(DownloadMovieItem down , Button bt,int position)
		{
			this.bt = bt;
			this.down = down;
			this.position = position;
		}
		/** (非 Javadoc) 
		* Title: onClick
		* Description:
		* @param v
		* @see android.view.View.OnClickListener#onClick(android.view.View)
		*/ 
		@Override
		public void onClick(View v)
		{
		
			System.out.println("点击了："+down.getPosition());
			int state = down.getDownloadState();
			switch (state)
			{
			case DOWNLOAD_STATE_DELETE:
				//可下载
				toDownload();
				break;
			case DOWNLOAD_STATE_SUCCESS:
				//播放打开等等操作
				Toast.makeText(getApplicationContext(),"打开了："+down.getMovieName(), Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_STATE_WATTING:
				toDownload();
				break;
			case DOWNLOAD_STATE_FAIL:
				//打开下载管理界面
				getMyApp().getPager().setCurrentItem(1, false);
				break;
			default:
				break;
			}
		}
		public void toDownload()
		{
			System.out.println(down.getMovieName()+":DOWNLOAD_STATE_START"+":toDownload");
			down.setPosition(position);
			down.setDownloadState(DOWNLOAD_STATE_START);
			getMyApp().setStartDownloadMovieItem(down);
			sendBroadcast(new Intent().setAction("download"));
			bt.setClickable(false);
			bt.setText("下载中");
			//将最初的数据保存到数据库.主要是为了 让数据库中的数据第一反应与所操作的状态一致,因为在后面的下载任务中.在线程中更改数据库状态
			//如果在线程未开始且程序退出的情况.那么这个状态会不正确 
			List<DownloadMovieItem> ls = db.findItemsByWhereAndWhereValue("movieName", down.getMovieName(), DownloadMovieItem.class, TABNAME_DOWNLOADTASK, null);
			if(ls.size() == 0 ) {
				//说明没有此条数据
				db.insertObject(down, TABNAME_DOWNLOADTASK);
			}else {
				//更新这个状态
				db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "movieName=?", new String[] {down.getMovieName()}, down);
			}
		}
		
	}
	/**
	 * (非 Javadoc) Title: onClick Description:
	 * 
	 * @param view
	 * @see demo.mydownload.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view)
	{
	}
}
