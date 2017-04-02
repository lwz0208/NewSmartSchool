package com.wust.easeui.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.wust.easeui.R;
import com.wust.easeui.utils.EaseSmileUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

public class MyChatRowLittleCard extends EaseChatRow {

    private TextView lc_title;
    private TextView lc_content;
    private TextView contentView;
    private ImageView preview_image;
    private TextView lc_tag;

    public MyChatRowLittleCard(Context context, EMMessage message, int position,
                               BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(
                message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_littlecard
                        : R.layout.ease_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        lc_title = (TextView) findViewById(R.id.lc_title);
        lc_content = (TextView) findViewById(R.id.lc_content);
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
        preview_image = (ImageView) findViewById(R.id.preview_image);
        lc_tag = (TextView) findViewById(R.id.lc_tag);
    }

    @Override
    public void onSetUpView() {
        if (message.direct() == EMMessage.Direct.SEND) {
            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            Spannable span = EaseSmileUtils.getSmiledText(context,
                    txtBody.getMessage());
//         设置内容
            contentView.setText(span, BufferType.SPANNABLE);
        } else {
            Log.e(MYTAG, message.getStringAttribute("title", ""));
//            Log.e(MYTAG, message.getStringAttribute("content", ""));
//            Log.e(MYTAG, message.getStringAttribute("tag", ""));
//            Log.e(MYTAG, message.getStringAttribute("targetType", ""));
//            Log.e(MYTAG, message.getStringAttribute("target", ""));
            String titleStr = message.getStringAttribute("title", "");
            String contentStr = message.getStringAttribute("content", "");
            lc_title.setText(titleStr);
            //如果推过来的是任务改变，那么内容和标题就现实一样的，因为content里的内容是用户看不懂的。
            if (message.getStringAttribute("targetType", "").equals("task_modify"))
                lc_content.setText(titleStr);
            else
                lc_content.setText(contentStr);
            lc_tag.setText(message.getStringAttribute("tag", ""));
            if (message.getStringAttribute("targetType", "").contains("inform")) {
                Glide.with(context).load(R.drawable.workfragment_notice).placeholder(R.drawable.group_notice_empty).into(preview_image);
            } else if (message.getStringAttribute("targetType", "").contains("jflow")) {
                Glide.with(context).load(R.drawable.workfragment_workline).placeholder(R.drawable.group_notice_empty).into(preview_image);
            } else if (message.getStringAttribute("targetType", "").contains("meeting")) {
                Glide.with(context).load(R.drawable.workfragment_meeting).placeholder(R.drawable.group_notice_empty).into(preview_image);
            } else if (message.getStringAttribute("targetType", "").contains("task")) {
                Glide.with(context).load(R.drawable.workfragment_duty).placeholder(R.drawable.group_notice_empty).into(preview_image);
            }

        }
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(GONE);
                    statusView.setVisibility(VISIBLE);
                    // 发送消息
                    // sendMsgInBackground(message);
                    break;
                case SUCCESS: // 发送成功
                    progressBar.setVisibility(GONE);
                    statusView.setVisibility(GONE);
                    break;
                case FAIL: // 发送失败
                    progressBar.setVisibility(GONE);
                    statusView.setVisibility(VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    progressBar.setVisibility(VISIBLE);
                    statusView.setVisibility(GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == ChatType.Chat) {
                try {
                    EMClient.getInstance()
                            .chatManager()
                            .ackMessageRead(message.getFrom(),
                                    message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {

    }

}
