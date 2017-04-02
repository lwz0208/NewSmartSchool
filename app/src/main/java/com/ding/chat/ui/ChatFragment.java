package com.ding.chat.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.ding.chat.DemoApplication;
import com.ding.easeui.domain.IsAtEntity;
import com.ding.chat.domain.UserInfoEntity;
import com.ding.easeui.utils.PreferenceManager;
import com.ding.easeui.utils.SingleExampleAt;
import com.ding.easeui.EaseConstant;
import com.ding.easeui.widget.EaseChatPrimaryMenu;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.ding.chat.DemoHelper;
import com.ding.chat.R;
import com.ding.chat.domain.EmojiconExampleGroupData;
import com.ding.chat.domain.RobotUser;
import com.ding.chat.widget.ChatRowVoiceCall;
import com.ding.easeui.Constant;
import com.ding.easeui.ui.EaseChatFragment;
import com.ding.easeui.ui.EaseChatFragment.EaseChatFragmentListener;
import com.ding.easeui.widget.chatrow.EaseChatRow;
import com.ding.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.ding.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

public class ChatFragment extends EaseChatFragment implements
        EaseChatFragmentListener {

    // 避免和基类定义的常量可能发生的冲突，常量从11开始定义
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;

    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    //需要监听的输入框，如果有@符号就跳转到选人界面
    private EditText editText;
    //存储当前输入框的聊天内容
    String temp;
    //全局的保存userId和realname
    String chooseuserId;
    String choosename;
    JSONArray to_id;

    /**
     * 是否为环信小助手
     */
    private boolean isRobot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        to_id = new JSONArray();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = DemoHelper.getInstance()
                    .getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }

        if (chatType != EaseConstant.CHATTYPE_SINGLE) {
            messageList.setShowUserNick(true);
            editText = (EditText) (((EaseChatPrimaryMenu) inputMenu.getChatPrimaryMenuView()).getEditextView());

            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        String addText = s.toString().substring(start, start + count);
                        if (addText.toString().equals("@")) {
                            startActivityForResult((new Intent(getContext(),
                                            ChooseAtPersonActivity.class).putExtra("groupid", toChatUsername)),
                                    Constant.CHOOSE_NOTICEPEOPLE);
                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    temp = s.toString();
                }
            });
        }
        super.setUpView();
        // 设置标题栏点击事件
        titleBar.setLeftLayoutClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(),
                            MainActivity.class);
                    startActivity(intent);
                }
                getActivity().finish();
            }
        });
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu())
                .addEmojiconGroup(EmojiconExampleGroupData.getData());
    }

    @Override
    protected void registerExtendMenuItem() {
        // demo这里不覆盖基类已经注册的item,item点击listener沿用基类的
        super.registerExtendMenuItem();
        // 增加扩展item
        inputMenu.registerExtendMenuItem(R.string.attach_video,
                R.drawable.em_chat_video_selector, ITEM_VIDEO,
                extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_file,
                R.drawable.em_chat_file_selector, ITEM_FILE,
                extendMenuItemClickListener);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call,
                    R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL,
                    extendMenuItemClickListener);
            inputMenu.registerExtendMenuItem(R.string.attach_video_call,
                    R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL,
                    extendMenuItemClickListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // 复制消息
                    clipboard.setText(((EMTextMessageBody) contextMenuMessage
                            .getBody()).getMessage());
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // 删除消息
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;
                case ContextMenuActivity.RESULT_CODE_FORWARD: // 转发消息
                    Intent intent = new Intent(getActivity(),
                            ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: // 发送选中的视频
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(),
                                "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils
                                    .createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(),
                                    duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: // 发送选中的文件
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(getContext(), uri);
                        }
                    }
                    break;

                default:
                    break;
            }
            if (requestCode == Constant.CHOOSE_NOTICEPEOPLE) {
                choosename = data.getStringExtra("choosename");
                chooseuserId = data.getStringExtra("chooseuserId");
                if (!TextUtils.isEmpty(chooseuserId) && !TextUtils.isEmpty(choosename)) {
                    Log.e(TAG, choosename.toString() + "---" + chooseuserId.toString());
                    editText.setText(temp + choosename);
                    Editable etext = editText.getText();
                    Selection.setSelection(etext, etext.length());
                    //将选的人压进去
                    to_id.put(chooseuserId);
                }
            }
        }
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        //每一个message都加上自己的真实姓名，这样在接收方就可以拿出来显示了。
        message.setAttribute("sendrealname", PreferenceManager.getInstance().getCurrentRealName());
        if (isRobot) {
            // 设置消息扩展属性
            message.setAttribute("em_robot_message", isRobot);
        }
        if (message.getBody().toString().contains("@") && choosename != null && chooseuserId != null) {
            message.setAttribute("from_realname", PreferenceManager.getInstance().getCurrentRealName());
            message.setAttribute("to_id", to_id);
            Log.i(TAG, to_id.toString());
        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        // 设置自定义listview item提供者
        return new CustomChatRowProvider();
    }

    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager()
                    .getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            startActivityForResult((new Intent(getActivity(),
                    GroupDetailsActivity.class).putExtra("groupId",
                    toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
            startActivityForResult(new Intent(getActivity(),
                    ChatRoomDetailsActivity.class).putExtra("roomId",
                    toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    // 头像点击事件
    @Override
    public void onAvatarClick(String username) {
        // 跳转到自己的用户信息展示页面
        if (!username.equals("admin"))
            startActivity(new Intent(getContext(), DeptMemInfoActivity2.class)
                    .putExtra("userId", username));
    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        // 消息框点击事件，demo这里不做覆盖，如需覆盖，return true
        String targetType = message.getStringAttribute("targetType", "");
        if (targetType.contains("inform")) {
            startActivity(new Intent(getActivity(), NoticeDetailActivity_new.class).putExtra("ItemFrom", "WorkFragNoticeActivity").putExtra("KEY_CLICK_NOTICE", message.getStringAttribute("target", "")));
        } else if (targetType.contains("jflow")) {
            startActivity(new Intent(getActivity(), WorkFragApplyActivity.class).putExtra("webid_detail", message.getStringAttribute("target", "")));
        } else if (targetType.contains("meeting")) {
            String titleStr = message.getStringAttribute("title", "");
            titleStr = titleStr.substring(3, titleStr.length());
            startActivity(new Intent(getActivity(), MeetingDetailActivity.class).putExtra("meetingtitle", titleStr).putExtra("meetingID", message.getStringAttribute("target", "")).putExtra("meetingStatus", "0"));
        } else if (targetType.contains("task")) {
            startActivity(new Intent(getActivity(), TaskDetailActivity.class).putExtra("taskID", message.getStringAttribute("target", "")));
        } else if (targetType.equals("comment")) {
            String content = message.getStringAttribute("target", "");
            String[] content_split = content.split(",");
            Intent i;
            switch (content_split[0]) {
                case "task":
                    i = new Intent(getActivity(), TaskDetailActivity.class).putExtra("taskID", content_split[1]);
                    break;
                case "inform":
                    i = new Intent(getActivity(), WorkFragNoticeActivity.class);
                    break;
                default:
                    i = new Intent(getActivity(), MainActivity.class);
                    break;
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().startActivity(i);
        }

        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // 消息框长按
        startActivityForResult((new Intent(getActivity(),
                        ContextMenuActivity.class)).putExtra("message", message),
                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO: // 视频
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: // 一般文件
                // demo这里是通过系统api选择文件，实际app中最好是做成qq那种选择发送文件
                //我只想说，呵呵哒~
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL: // 音频通话
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL: // 视频通话
                startVideoCall();
                break;

            default:
                break;
        }
        // 不覆盖已有的点击事件
        return false;
    }

    /**
     * 选择文件
     */
    protected void selectFileFromLocal() {

//        Intent intent = null;
//        if (Build.VERSION.SDK_INT < 19) { // 19以后这个api不可用，demo这里简单处理成图库选择图片
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        } else {
//            intent = new Intent(
//                    Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
        //sdk大于19之后明明可以用的。我傲娇了，我有小情绪了。。。TMD，宝宝心里苦但宝宝不说~
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 拨打语音电话
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT)
                    .show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class)
                    .putExtra("username", toChatUsername).putExtra(
                            "isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * 拨打视频电话
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT)
                    .show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class)
                    .putExtra("username", toChatUsername).putExtra(
                            "isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements
            EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            // 音、视频通话发送、接收共4种
            return 4;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                // 语音通话类型
                if (message.getBooleanAttribute(
                        Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL
                            : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(
                        Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    // 视频通话
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL
                            : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position,
                                            BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // 语音通话, 视频通话
                if (message.getBooleanAttribute(
                        Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)
                        || message.getBooleanAttribute(
                        Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    return new ChatRowVoiceCall(getActivity(), message,
                            position, adapter);
                }
                //如果不是单聊，辣么就检测一下群消息和已读未读的@消息
                if (chatType != EaseConstant.CHATTYPE_SINGLE) {
                    Log.e(TAG, "chatType != EaseConstant.CHATTYPE_SINGLE");
                    IsAtEntity temp = SingleExampleAt.getInstance().getUnread().get(message.getTo());
                    if (temp != null)
                        if (!temp.isread()) {
                            //拿到扩展字段
                            String from_realname = message.getStringAttribute(
                                    "from_realname", null);
                            String to_id = message.getStringAttribute(
                                    "to_id", null);
                            Log.e(TAG, from_realname + "---" + to_id);
                            if (to_id != null && from_realname != null) {
                                if (to_id.contains(EMClient.getInstance().getCurrentUser())) {
                                    Log.e(TAG, "到最里面了");
                                    Toast.makeText(getContext(), from_realname + "@了你", Toast.LENGTH_SHORT).show();
                                    IsAtEntity isAtEntity = new IsAtEntity();
                                    isAtEntity.setMessage(message);
                                    isAtEntity.setIsread(true);
                                    Map<String, IsAtEntity> unread = new HashMap<>();
                                    unread.put(message.getTo(), isAtEntity);
                                    SingleExampleAt.getInstance().setUnread(unread);
//                                    messageList.getclicktoat().setVisibility(View.GONE);
                                }
                            }
                        } else {
                            //不管已读未读的@消息都弄为已读
                            IsAtEntity isAtEntity = new IsAtEntity();
                            isAtEntity.setMessage(message);
                            isAtEntity.setIsread(true);
                            Map<String, IsAtEntity> unread = new HashMap<>();
                            unread.put(message.getTo(), isAtEntity);
                            SingleExampleAt.getInstance().setUnread(unread);
                        }
                }

            }

            return null;
        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
