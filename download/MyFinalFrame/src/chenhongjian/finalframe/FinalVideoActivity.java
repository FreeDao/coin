/**   
* @Title: FinalVideoActivity.java
* @Package chenhongjian.finalframe
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-7-27 下午3:54:41
* @version V1.0
*/ 
package chenhongjian.finalframe;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

/** 
 * @ClassName: FinalVideoActivity
 * @Description: 播放器
 * @author 陈红建
 * @date 2013-7-27 下午3:54:41
 * 
 */
public class FinalVideoActivity extends Activity
{

	
	
	private VideoView mVideoView;
	private ProgressDialog dialog;
//	public static String path = "http://main.gslb.ku6.com/s1/80uqkDikmUBzX2Yr/1314766895538/5e7c098acfbabe89bc0ae1bfabf72b30/1374914193727/v507/48/89/67052453ccf61909887213647f195712-f4v-h264-aac-1584-32-31207.0-6323589-1314766431607-4caed29261639ab75a9d65b3b63892da-1-00-00-00.f4v";
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.videoview);
		Intent i = getIntent();;
		String path = i.getStringExtra("path");
		if(TextUtils.isEmpty(path)) {
			Toast.makeText(getApplicationContext(),"请指定播放地址!(FinalVideoActivity.path)", Toast.LENGTH_SHORT).show();
			finish();
		}else {
			dialog = ProgressDialog.show(this, "提示", "加载中..");
			mVideoView = (VideoView) findViewById(R.id.surface_view);
			mVideoView.setVideoPath(path);
			mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
			mVideoView.setOnCompletionListener(new OnCompletionListener()
			{
				
				@Override
				public void onCompletion(MediaPlayer media)
				{
					//播放完成时
					Toast.makeText(getApplicationContext(),"播放完毕!", Toast.LENGTH_SHORT).show();
				}
			});
			
			mVideoView.setOnBufferingUpdateListener(new OnBufferingUpdateListener()
			{
				
				@Override
				public void onBufferingUpdate(MediaPlayer media, int current)
				{
					//当进度刷新的时候
				}
			});
			mVideoView.setOnPreparedListener(new OnPreparedListener()
			{
				
				@Override
				public void onPrepared(MediaPlayer media)
				{
					//准备好时
					dialog.dismiss();
					media.start();
				}
			});
			mVideoView.setMediaController(new MediaController(this));
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mVideoView != null)
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
		super.onConfigurationChanged(newConfig);
	}
}

