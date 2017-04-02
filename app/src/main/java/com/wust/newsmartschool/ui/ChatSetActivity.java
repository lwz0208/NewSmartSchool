package com.wust.newsmartschool.ui;

import com.wust.newsmartschool.DemoHelper;
import com.wust.newsmartschool.DemoModel;
import com.wust.newsmartschool.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.wust.easeui.widget.EaseSwitchButton;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatSetActivity extends BaseActivity implements OnClickListener
{

	/**
	 * 设置扬声器布局
	 */
	private RelativeLayout rl_switch_speaker;
	private RelativeLayout rl_switch_delete_msg_when_exit_group;
	private RelativeLayout rl_switch_auto_accept_group_invitation;

	private EaseSwitchButton speakerSwitch;
	private EaseSwitchButton switch_delete_msg_when_exit_group;
	private EaseSwitchButton switch_auto_accept_group_invitation;

	private TextView textview1;
	private TextView textview2;

	private DemoModel settingsModel;
	private EMOptions chatOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_set);

		rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);
		rl_switch_delete_msg_when_exit_group = (RelativeLayout) findViewById(R.id.rl_switch_delete_msg_when_exit_group);
		rl_switch_auto_accept_group_invitation = (RelativeLayout) findViewById(R.id.rl_switch_auto_accept_group_invitation);

		speakerSwitch = (EaseSwitchButton) findViewById(R.id.switch_speaker);
		switch_delete_msg_when_exit_group = (EaseSwitchButton) findViewById(R.id.switch_delete_msg_when_exit_group);
		switch_auto_accept_group_invitation = (EaseSwitchButton) findViewById(R.id.switch_auto_accept_group_invitation);

		rl_switch_speaker.setOnClickListener(this);
		rl_switch_delete_msg_when_exit_group.setOnClickListener(this);
		rl_switch_auto_accept_group_invitation.setOnClickListener(this);

		settingsModel = DemoHelper.getInstance().getModel();
		chatOptions = EMClient.getInstance().getOptions();

		// 是否打开扬声器
		// the speaker is switched on or not?
		if (settingsModel.getSettingMsgSpeaker())
		{
			speakerSwitch.openSwitch();
		} else
		{
			speakerSwitch.closeSwitch();
		}

		// delete messages when exit group?
		if (settingsModel.isDeleteMessagesAsExitGroup())
		{
			switch_delete_msg_when_exit_group.openSwitch();
		} else
		{
			switch_delete_msg_when_exit_group.closeSwitch();
		}

		if (settingsModel.isAutoAcceptGroupInvitation())
		{
			switch_auto_accept_group_invitation.openSwitch();
		} else
		{
			switch_auto_accept_group_invitation.closeSwitch();
		}

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_switch_speaker:
			if (speakerSwitch.isSwitchOpen())
			{
				speakerSwitch.closeSwitch();
				settingsModel.setSettingMsgSpeaker(false);
			} else
			{
				speakerSwitch.openSwitch();
				settingsModel.setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_delete_msg_when_exit_group:
			if (switch_delete_msg_when_exit_group.isSwitchOpen())
			{
				switch_delete_msg_when_exit_group.closeSwitch();
				settingsModel.setDeleteMessagesAsExitGroup(false);
				chatOptions.setDeleteMessagesAsExitGroup(false);
			} else
			{
				switch_delete_msg_when_exit_group.openSwitch();
				settingsModel.setDeleteMessagesAsExitGroup(true);
				chatOptions.setDeleteMessagesAsExitGroup(true);
			}
			break;
		case R.id.rl_switch_auto_accept_group_invitation:
			if (switch_auto_accept_group_invitation.isSwitchOpen())
			{
				switch_auto_accept_group_invitation.closeSwitch();
				settingsModel.setAutoAcceptGroupInvitation(false);
				chatOptions.setAutoAcceptGroupInvitation(false);
			} else
			{
				switch_auto_accept_group_invitation.openSwitch();
				settingsModel.setAutoAcceptGroupInvitation(true);
				chatOptions.setAutoAcceptGroupInvitation(true);
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
