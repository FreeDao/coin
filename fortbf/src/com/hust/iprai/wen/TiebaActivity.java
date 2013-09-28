package com.hust.iprai.wen;

import org.jq.nbz.R;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

public class TiebaActivity extends TabActivity implements
		CompoundButton.OnCheckedChangeListener {

	private static final String HOME_TAB = "home_tab";
	private static final String MENTION_TAB = "mention_tab";
	private static final String PERSON_TAB = "person_tab";
	private static final String MORE_TAB = "more_tab";

	private Intent mHomeIntent = null;
	private Intent mMentionIntent = null;
	private Intent mPersonIntent = null;
	private Intent mMoreIntent = null;

	private TabHost mTabHost = null;

	private TextView mMessageTipsMention = null;
	private TextView mMessageTipsPerson = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintabs_activity);

		mTabHost = getTabHost();
		initIntents();
		initTips();
		initRadios();
		setupIntents();
	}
	
	private void initIntents() {
		mHomeIntent = new Intent(this, HomeActivity.class);
		mMentionIntent = new Intent(this, MentionActivity.class);
		mPersonIntent = new Intent(this, PersonInfoActivity.class);
		mMoreIntent = new Intent(this, MoreActivity.class);
	}
	
	private void initTips() {
		mMessageTipsMention = (TextView) findViewById(R.id.message_mention);
		mMessageTipsPerson = (TextView) findViewById(R.id.message_person);
		mMessageTipsMention.setText("2");
		mMessageTipsPerson.setText("4");
		mMessageTipsMention.setVisibility(View.GONE);
		mMessageTipsPerson.setVisibility(View.GONE);
	}

	private void initRadios() {
		((RadioButton) findViewById(R.id.radio_home))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_mention))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_person_info))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_more))
				.setOnCheckedChangeListener(this);
	}

	private void setupIntents() {
		((RadioButton) findViewById(R.id.radio_home)).setChecked(true);
		mTabHost.addTab(buildTabSpec(HOME_TAB, mHomeIntent));
		mTabHost.addTab(buildTabSpec(MENTION_TAB, mMentionIntent));
		mTabHost.addTab(buildTabSpec(PERSON_TAB, mPersonIntent));
		mTabHost.addTab(buildTabSpec(MORE_TAB, mMoreIntent));
		mTabHost.setCurrentTabByTag(HOME_TAB);
	}

	private TabHost.TabSpec buildTabSpec(String tag, Intent intent) {
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setContent(intent).setIndicator("",
				getResources().getDrawable(R.drawable.icon));
		return tabSpec;
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_home:
				mTabHost.setCurrentTabByTag(HOME_TAB);
				break;
			case R.id.radio_mention:
				mTabHost.setCurrentTabByTag(MENTION_TAB);
				break;
			case R.id.radio_person_info:
				mTabHost.setCurrentTabByTag(PERSON_TAB);
				break;
			case R.id.radio_more:
				mTabHost.setCurrentTabByTag(MORE_TAB);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN)
				&& (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
			quitDialog();
		}
		return super.dispatchKeyEvent(event);
	}

	private void quitDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.alerm_title)
				.setIcon(null)
				.setCancelable(false)
				.setMessage(R.string.alert_quit_confirm)
				.setPositiveButton(R.string.alert_yes_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								TiebaActivity.this.finish();
							}
						})
				.setNegativeButton(R.string.alert_no_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}
}
