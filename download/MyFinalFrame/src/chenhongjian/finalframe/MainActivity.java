/**   
* @Title: MainActivity.java
* @Package chenhongjian.finalframe
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-7-26 下午2:48:38
* @version V1.0
*/ 
package chenhongjian.finalframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/** 
 * @ClassName: MainActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 陈红建
 * @date 2013-7-26 下午2:48:38
 * 
 */
public class MainActivity extends Activity
{
	/** (非 Javadoc) 
	* Title: onCreate
	* Description:
	* @param icicle
	* @see chenhongjian.finalframe.FinalVideoActivity#onCreate(android.os.Bundle)
	*/ 
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		String path = "http://main.gslb.ku6.com/s1/80uqkDikmUBzX2Yr/1314766895538/5e7c098acfbabe89bc0ae1bfabf72b30/1374914193727/v507/48/89/67052453ccf61909887213647f195712-f4v-h264-aac-1584-32-31207.0-6323589-1314766431607-4caed29261639ab75a9d65b3b63892da-1-00-00-00.f4v";
		Intent i = new Intent(this , FinalVideoActivity.class);
		i.putExtra("path", path);
		startActivity(i);
	}
}

