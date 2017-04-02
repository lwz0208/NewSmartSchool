package com.ding.easeui.widget.chatrow;

import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.ding.easeui.R;
import com.hyphenate.util.EMLog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EaseChatRowVoice extends EaseChatRowFile {

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStutausView;

    public EaseChatRowVoice(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_voice : R.layout.ease_row_sent_voice, this);
    }

    @Override
    protected void onFindViewById() {
        voiceImageView = ((ImageView) findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) findViewById(R.id.tv_length);
        readStutausView = (ImageView) findViewById(R.id.iv_unread_voice);
    }

    @Override
    protected void onSetUpView() {
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if (len > 0) {
            voiceLengthView.setText(voiceBody.getLength() + "\"");
            voiceLengthView.setVisibility(VISIBLE);
        } else {
            voiceLengthView.setVisibility(INVISIBLE);
        }
        if (EaseChatRowVoicePlayClickListener.playMsgId != null
                && EaseChatRowVoicePlayClickListener.playMsgId.equals(message.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.anim.voice_from_icon);
            } else {
                voiceImageView.setImageResource(R.anim.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
            } else {
                voiceImageView.setImageResource(R.drawable.ease_chatto_voice_playing);
            }
        }

        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (message.isListened()) {
                // 隐藏语音未听标志
                readStutausView.setVisibility(INVISIBLE);
            } else {
                readStutausView.setVisibility(VISIBLE);
            }
            EMLog.d(TAG, "it is receive msg");
            if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                progressBar.setVisibility(VISIBLE);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(INVISIBLE);

            }
            return;
        }

        // until here, deal with send voice msg
        handleSendMessage();
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        new EaseChatRowVoicePlayClickListener(message, voiceImageView, readStutausView, adapter, activity).onClick(bubbleLayout);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EaseChatRowVoicePlayClickListener.currentPlayListener != null && EaseChatRowVoicePlayClickListener.isPlaying) {
            // 停止语音播放
            EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }
    }

}
