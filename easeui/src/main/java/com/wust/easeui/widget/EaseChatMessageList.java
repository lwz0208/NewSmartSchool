package com.wust.easeui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wust.easeui.domain.IsAtEntity;
import com.wust.easeui.utils.EaseCommonUtils;
import com.wust.easeui.utils.SingleExampleAt;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.wust.easeui.R;
import com.wust.easeui.adapter.EaseMessageAdapter;
import com.wust.easeui.widget.chatrow.EaseCustomChatRowProvider;

import java.util.ArrayList;
import java.util.List;

public class EaseChatMessageList extends RelativeLayout {

    protected static final String TAG = "EaseChatMessageList_Debugs";
    protected ListView listView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    //    protected TextView clicktoat;
    protected Context context;
    protected EMConversation conversation;
    protected int chatType;
    protected String toChatUsername;
    protected EaseMessageAdapter messageAdapter;
    protected boolean showUserNick;
    protected boolean showAvatar;
    protected Drawable myBubbleBg;
    protected Drawable otherBuddleBg;

    public EaseChatMessageList(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseStyle(context, attrs);
        init(context);
    }

    public EaseChatMessageList(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_chat_message_list,
                this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        listView = (ListView) findViewById(R.id.list);
//        clicktoat = (TextView) findViewById(R.id.clicktoat);
    }

    /**
     * init widget
     *
     * @param toChatUsername
     * @param chatType
     * @param customChatRowProvider
     */
    public void init(String toChatUsername, int chatType,
                     EaseCustomChatRowProvider customChatRowProvider) {
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;

        conversation = EMClient
                .getInstance()
                .chatManager()
                .getConversation(toChatUsername,
                        EaseCommonUtils.getConversationType(chatType), true);
        messageAdapter = new EaseMessageAdapter(context, toChatUsername,
                chatType, listView);
        messageAdapter.setShowAvatar(showAvatar);
        messageAdapter.setShowUserNick(showUserNick);
        messageAdapter.setMyBubbleBg(myBubbleBg);
        messageAdapter.setOtherBuddleBg(otherBuddleBg);
        messageAdapter.setCustomChatRowProvider(customChatRowProvider);

        // 设置adapter显示消息
        listView.setAdapter(messageAdapter);

        /**和@定位相关的东东*/
        List<EMMessage> unreadmessages = new ArrayList<>();
        unreadmessages.addAll(conversation.getAllMessages());
        IsAtEntity temp = SingleExampleAt.getInstance().getUnread().get(toChatUsername);
        if (temp != null) {
            if (!temp.isread()) {
                //开始遍历这个对话所有的message
                for (int atposition = 0; atposition < unreadmessages.size(); atposition++) {
                    //拿到扩展字段
                    String from_realname = unreadmessages.get(atposition).getStringAttribute(
                            "from_realname", null);
                    String to_id = unreadmessages.get(atposition).getStringAttribute(
                            "to_id", null);
                    Log.e(TAG, from_realname + "---" + to_id);
                    if (to_id != null && from_realname != null) {
                        if (to_id.contains(EMClient.getInstance().getCurrentUser())) {
                            if (unreadmessages.get(atposition).getMsgId().equals(temp.getMessage().getMsgId())) {
                                messageAdapter.refreshSeekTo(conversation.getMessagePosition(temp.getMessage()));
                                break;
                            }
                        }
                    }
                }
            } else {
                refreshSelectLast();
            }
        } else {
            refreshSelectLast();
        }

    }


    protected void parseStyle(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.EaseChatMessageList);
        showAvatar = ta.getBoolean(
                R.styleable.EaseChatMessageList_msgListShowUserAvatar, true);
        myBubbleBg = ta
                .getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
        otherBuddleBg = ta
                .getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
        showUserNick = ta.getBoolean(
                R.styleable.EaseChatMessageList_msgListShowUserNick, false);
        ta.recycle();
    }

    /**
     * 刷新列表
     */
    public void refresh() {
        if (messageAdapter != null) {
            messageAdapter.refresh();
        }
    }

    /**
     * 刷新列表，并且跳至最后一个item
     */
    public void refreshSelectLast() {
        if (messageAdapter != null) {
            messageAdapter.refreshSelectLast();
        }
    }

    /**
     * 刷新页面,并跳至给定position
     *
     * @param position
     */
    public void refreshSeekTo(int position) {
        if (messageAdapter != null) {
            messageAdapter.refreshSeekTo(position);
        }
    }

    /**
     * 获取listview
     *
     * @return
     */
    public ListView getListView() {
        return listView;
    }

    /**
     * 获取SwipeRefreshLayout
     *
     * @return
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public EMMessage getItem(int position) {
        return messageAdapter.getItem(position);
    }

    /**
     * 设置是否显示用户昵称
     *
     * @param showUserNick
     */
    public void setShowUserNick(boolean showUserNick) {
        this.showUserNick = showUserNick;
    }

    public boolean isShowUserNick() {
        return showUserNick;
    }

    public interface MessageListItemClickListener {
        void onResendClick(EMMessage message);

        /**
         * 控件有对气泡做点击事件默认实现，如果需要自己实现，return true。
         * 当然也可以在相应的chatrow的onBubbleClick()方法里实现点击事件
         *
         * @param message
         * @return
         */
        boolean onBubbleClick(EMMessage message);

        void onBubbleLongClick(EMMessage message);

        void onUserAvatarClick(String username);

    }

    /**
     * 设置list item里控件的点击事件
     *
     * @param listener
     */
    public void setItemClickListener(MessageListItemClickListener listener) {
        if (messageAdapter != null) {
            messageAdapter.setItemClickListener(listener);
        }
    }

    /**
     * 设置自定义chatrow提供者
     *
     * @param rowProvider
     */
    public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider) {
        if (messageAdapter != null) {
            messageAdapter.setCustomChatRowProvider(rowProvider);
        }
    }
}
