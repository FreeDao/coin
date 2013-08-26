package com.hust.iprai.wen;

import org.jq.nbz.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hust.iprai.wen.home.LikeActivity;
import com.hust.iprai.wen.home.MarkActivity;

public class HomeActivity extends ActivityGroup {
	
	private FrameLayout mContent;
	private Button mButtonLike;
	private Button mButtonMark;
	private static final String CURRENT_PAGE = "current_page";
	private static final String HOME_LIKE_ID = "like";
	private static final String HOME_MARK_ID = "mark"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		mContent = (FrameLayout) findViewById(R.id.content);
		mButtonLike = (Button) findViewById(R.id.home_bt_like);
		mButtonMark = (Button) findViewById(R.id.home_bt_mark);
		mButtonLike.setOnClickListener(MyClickListener);
		mButtonMark.setOnClickListener(MyClickListener);
		
		switchPages(R.id.home_bt_like);
	}
	
	public void addView(String id, Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		mContent.removeAllViews();
		mContent.addView(getLocalActivityManager().startActivity(id, intent).getDecorView());
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(CURRENT_PAGE, getLocalActivityManager().getCurrentId());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String currentPage = savedInstanceState.getString(CURRENT_PAGE);
		if ((currentPage != null) && (currentPage.equals(HOME_MARK_ID))) {
			switchPages(R.id.home_bt_mark);
		}
	}

	protected void switchPages(int viewId) {
		switch(viewId) {
		case R.id.home_bt_like:
			addView(HOME_LIKE_ID, LikeActivity.class);
			mButtonLike.setBackgroundResource(R.drawable.home_topbar_bt);
			//mButtonLike.setImageResource(R.drawable.home_bt_like_on);
			mButtonMark.setBackgroundDrawable(null);
			//mButtonMark.setImageResource(R.drawable.home_bt_mark);
			break;
		case R.id.home_bt_mark:
			addView(HOME_MARK_ID, MarkActivity.class);
			mButtonMark.setBackgroundResource(R.drawable.home_topbar_bt);
			//mButtonMark.setImageResource(R.drawable.home_bt_mark_on);
			mButtonLike.setBackgroundDrawable(null);
			//mButtonLike.setImageResource(R.drawable.home_bt_like);
			break;
		default:
			break;
		}
	}
	
	private View.OnClickListener MyClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			switchPages(v.getId());
		}
	};
	
	

}

























