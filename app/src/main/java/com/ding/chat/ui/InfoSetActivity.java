package com.ding.chat.ui;

import com.ding.chat.DemoHelper;
import com.ding.chat.DemoModel;
import com.ding.chat.R;
import com.ding.easeui.widget.EaseSwitchButton;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InfoSetActivity extends BaseActivity implements OnClickListener
{

	/**
	 * 设置新消息通知布局
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * 设置声音布局
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * 设置震动布局
	 */
	private RelativeLayout rl_switch_vibrate;

	private EaseSwitchButton notifiSwitch;
	private EaseSwitchButton soundSwitch;
	private EaseSwitchButton vibrateSwitch;

	private TextView textview1;
	private TextView textview2;

	private DemoModel settingsModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_set);

		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);

		notifiSwitch = (EaseSwitchButton) findViewById(R.id.switch_notification);
		soundSwitch = (EaseSwitchButton) findViewById(R.id.switch_sound);
		vibrateSwitch = (EaseSwitchButton) findViewById(R.id.switch_vibrate);

		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);

		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);

		settingsModel = DemoHelper.getInstance().getModel();

		// 震动和声音总开关，来消息时，是否允许此开关打开
		// the vibrate and sound notification are allowed or not?
		if (settingsModel.getSettingMsgNotification())
		{
			notifiSwitch.openSwitch();
		} else
		{
			notifiSwitch.closeSwitch();
		}

		// 是否打开声音
		// sound notification is switched on or not?
		if (settingsModel.getSettingMsgSound())
		{
			soundSwitch.openSwitch();
		} else
		{
			soundSwitch.closeSwitch();
		}

		// 是否打开震动
		// vibrate notification is switched on or not?
		if (settingsModel.getSettingMsgVibrate())
		{
			vibrateSwitch.openSwitch();
		} else
		{
			vibrateSwitch.closeSwitch();
		}

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_switch_notification:
			if (notifiSwitch.isSwitchOpen())
			{
				notifiSwitch.closeSwitch();
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);

				settingsModel.setSettingMsgNotification(false);
			} else
			{
				notifiSwitch.openSwitch();
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				settingsModel.setSettingMsgNotification(true);
			}
			break;
		case R.id.rl_switch_sound:
			if (soundSwitch.isSwitchOpen())
			{
				soundSwitch.closeSwitch();
				settingsModel.setSettingMsgSound(false);
			} else
			{
				soundSwitch.openSwitch();
				settingsModel.setSettingMsgSound(true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (vibrateSwitch.isSwitchOpen())
			{
				vibrateSwitch.closeSwitch();
				settingsModel.setSettingMsgVibrate(false);
			} else
			{
				vibrateSwitch.openSwitch();
				settingsModel.setSettingMsgVibrate(true);
			}
			break;
		default:
			break;
		}
	}

	public void back(View view)
	{
		finish();
	}

}
