package com.ding.easeui.widget.chatrow;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.ding.easeui.Constant;
import com.ding.easeui.adapter.EaseMessageAdapter;
import com.ding.easeui.domain.EaseUser;
import com.ding.easeui.domain.IsAtEntity;
import com.ding.easeui.utils.ACache;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.ding.easeui.utils.SingleExampleAt;
import com.ding.easeui.widget.EaseChatMessageList;
import com.ding.easeui.widget.GlideCircleImage;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.ding.easeui.R;
import com.ding.easeui.utils.EaseUserUtils;
import com.hyphenate.util.DateUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public abstract class EaseChatRow extends LinearLayout {
    protected static final String TAG = EaseChatRow.class.getSimpleName();
    String MYTAG = "EaseChatRow_Debugs";

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;

    protected EaseChatMessageList.MessageListItemClickListener itemClickListener;
    String realname;
    String myrealname;
    ACache mCache;

    public EaseChatRow(Context context, EMMessage message, int position,
                       BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        onInflatView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        userAvatarView.setVisibility(View.GONE);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        ackedView = (TextView) findViewById(R.id.tv_ack);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);
        mCache = ACache.get(activity);

        myrealname = PreferenceManager.getInstance().getCurrentRealName();
        realname = mCache.getAsString(message.getFrom() + "_realname");
        onFindViewById();
    }

    /**
     * 根据当前message和position设置控件属性等
     *
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position,
                          EaseChatMessageList.MessageListItemClickListener itemClickListener) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;

        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    private void setUpBaseView() {
        // 设置用户昵称头像，bubble背景等
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message
                        .getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // 两条消息时间离得如果稍长，显示时间
                EMMessage prevMessage = (EMMessage) adapter
                        .getItem(position - 1);
                if (prevMessage != null
                        && DateUtils.isCloseEnough(message.getMsgTime(),
                        prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(
                            message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        userAvatarView.setVisibility(View.GONE);
        // 设置头像和nick，SEND表示是发送方
        if (message.direct() == Direct.SEND) {
            // EaseUserUtils.setUserAvatar(context, EMClient.getInstance()
            // .getCurrentUser(), userAvatarView);
            // 弃用环信头像的方法，采用自己头像的方法
            userAvatarView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Constant.GETHEADIMAG_URL
                            + PreferenceManager.getInstance().getCurrentUserId() + ".png")
                    .transform(new GlideCircleImage(context))
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(userAvatarView);
            // 发送方不显示nick
            // UserUtils.setUserNick(EMChatManager.getInstance().getCurrentUser(),
            // usernickView);
        } else
        // 否则就是接收方
        {
            userAvatarView.setVisibility(View.VISIBLE);
            if (message.getFrom().equals("admin")) {
                Glide.with(context)
                        .load(Constant.GETHEADIMAG_URL + "168.png")
                        .transform(new GlideCircleImage(context))
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(userAvatarView);
            } else {
                Glide.with(context)
                        .load(Constant.GETHEADIMAG_URL + message.getFrom() + ".png")
                        .transform(new GlideCircleImage(context))
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(userAvatarView);
            }
            //先拿到扩展字段
            String realnamefromextra = message.getStringAttribute(
                    "senderrealname", null);
            if (realnamefromextra != null) {
                usernickView.setText(realnamefromextra);
                mCache.put(message.getFrom() + "_realname",
                        realnamefromextra);
            } else {
                EaseUserUtils.setUserNick(message.getFrom(), usernickView);
                boolean isChinesewords = CommonUtils.isChinesewords(usernickView.getText().toString());
//                Log.e(MYTAG + "isNum", isChinesewords + "");
                if (!isChinesewords) {
                    if (realname == null || realname.equals("")) {
                        try {
                            CommonUtils.getNickname(activity, message.getFrom(),
                                    usernickView);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        usernickView.setText(realname);
                    }

                } else {
                    EaseUser user = EaseUserUtils.getUserInfo(message.getFrom());
                    if (user != null) {
                        realname = user.getNick();
//                        Log.e(MYTAG, realname);
                    }
                }
            }
        }

        if (deliveredView != null) {
            if (message.isDelivered()) {
                deliveredView.setVisibility(View.VISIBLE);
            } else {
                deliveredView.setVisibility(View.INVISIBLE);
            }
        }

        if (ackedView != null) {
            if (message.getChatType() == EMMessage.ChatType.Chat) {
                if (message.isAcked()) {
                    if (deliveredView != null) {
                        deliveredView.setVisibility(View.INVISIBLE);
                    }
                    Log.e("ackedView", "已已已已读");
                    ackedView.setVisibility(VISIBLE);
                    ackedView.setText("已读");
                } else {
                    ackedView.setVisibility(VISIBLE);
                    Log.e("ackedView", "未未未未读");
                    ackedView.setTextColor(getResources().getColor(R.color.common_top_bar_blue));
                    ackedView.setText("未读");
                }
            }
        }

        if (adapter instanceof EaseMessageAdapter) {
            if (((EaseMessageAdapter) adapter).isShowAvatar())
                userAvatarView.setVisibility(View.VISIBLE);
            else
                userAvatarView.setVisibility(View.GONE);
            if (usernickView != null) {
                if (((EaseMessageAdapter) adapter).isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (message.direct() == Direct.SEND) {
                if (((EaseMessageAdapter) adapter).getMyBubbleBg() != null)
                    bubbleLayout
                            .setBackgroundDrawable(((EaseMessageAdapter) adapter)
                                    .getMyBubbleBg());
                // else
                // bubbleLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.chatto_bg));
            } else if (message.direct() == Direct.RECEIVE) {
                if (((EaseMessageAdapter) adapter).getOtherBuddleBg() != null)
                    bubbleLayout
                            .setBackgroundDrawable(((EaseMessageAdapter) adapter)
                                    .getOtherBuddleBg());
                // else
                // bubbleLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ease_chatfrom_bg));
            }
        }
    }

    /**
     * 设置消息发送callback
     */
    protected void setMessageSendCallback() {
        if (messageSendCallback == null) {
            messageSendCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }

    /**
     * 设置消息接收callback
     */
    protected void setMessageReceiveCallback() {
        if (messageReceiveCallback == null) {
            messageReceiveCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (percentageView != null) {
                                percentageView.setText(progress + "%");
                            }
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }

    private void setClickListener() {
        if (bubbleLayout != null) {
            bubbleLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (!itemClickListener.onBubbleClick(message)) {
                            // 如果listener返回false不处理这个事件，执行lib默认的处理
                            onBubbleClick();
                        }
                    }
                }
            });

            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onResendClick(message);
                    }
                }
            });
        }

        userAvatarView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    if (message.direct() == Direct.SEND) {
                        itemClickListener.onUserAvatarClick(EMClient
                                .getInstance().getCurrentUser());
                    } else {
                        itemClickListener.onUserAvatarClick(message.getFrom());
                    }
                }
            }
        });
    }

    protected void updateView() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (message.status() == EMMessage.Status.FAIL) {

                    if (message.getError() == EMError.MESSAGE_INCLUDE_ILLEGAL_CONTENT) {
                        Toast.makeText(
                                activity,
                                activity.getString(R.string.send_fail)
                                        + activity
                                        .getString(R.string.error_send_invalid_content),
                                Toast.LENGTH_SHORT).show();
                    } else if (message.getError() == EMError.GROUP_NOT_JOINED) {
                        Toast.makeText(
                                activity,
                                activity.getString(R.string.send_fail)
                                        + activity
                                        .getString(R.string.error_send_not_in_the_group),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                activity,
                                activity.getString(R.string.send_fail)
                                        + activity
                                        .getString(R.string.connect_failuer_toast),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                onUpdateView();
            }
        });

    }

    /**
     * 填充layout
     */
    protected abstract void onInflatView();

    /**
     * 查找chatrow里的控件
     */
    protected abstract void onFindViewById();

    /**
     * 消息状态改变，刷新listview
     */
    protected abstract void onUpdateView();

    /**
     * 设置更新控件属性
     */
    protected abstract void onSetUpView();

    /**
     * 聊天气泡被点击事件
     */
    protected abstract void onBubbleClick();

}
