package com.wust.easeui.adapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.wust.easeui.domain.EaseUser;
import com.wust.easeui.domain.IsAtEntity;
import com.wust.easeui.utils.ACache;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.EaseCommonUtils;
import com.wust.easeui.utils.EaseSmileUtils;
import com.wust.easeui.utils.SingleExampleAt;
import com.wust.easeui.widget.GlideCircleImage;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.wust.easeui.Constant;
import com.wust.easeui.R;
import com.wust.easeui.utils.EaseUserUtils;
import com.hyphenate.util.DateUtils;

/**
 * 会话列表adapter
 */
public class EaseConversationAdapater extends ArrayAdapter<EMConversation> {
    private static final String TAG = "ChatAllHistoryAdapter_DeBugs";
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;
    Context mCt;

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;
    final ACache mCache;

    public EaseConversationAdapater(Context context, int resource,
                                    List<EMConversation> objects) {
        super(context, resource, objects);
        mCt = context;
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
        mCache = ACache.get(context);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView
                    .findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.avatar_1 = (ImageView) convertView
                    .findViewById(R.id.avatar_1);
            holder.avatar_2 = (ImageView) convertView
                    .findViewById(R.id.avatar_2);
            holder.avatar_3 = (ImageView) convertView
                    .findViewById(R.id.avatar_3);
            holder.avatar_4 = (ImageView) convertView
                    .findViewById(R.id.avatar_4);
            holder.rl_groupavatar = (RelativeLayout) convertView
                    .findViewById(R.id.rl_groupavatar);

            holder.avatar3_1 = (ImageView) convertView
                    .findViewById(R.id.avatar3_1);
            holder.avatar3_3 = (ImageView) convertView
                    .findViewById(R.id.avatar3_3);
            holder.avatar3_4 = (ImageView) convertView
                    .findViewById(R.id.avatar3_4);
            holder.rl_groupavatar_3 = (RelativeLayout) convertView
                    .findViewById(R.id.rl_groupavatar_3);

            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView
                    .findViewById(R.id.list_itease_layout);
            convertView.setTag(holder);
        }
        holder.list_itease_layout
                .setBackgroundResource(R.drawable.ease_mm_listitem);
        // 单聊布局
        holder.avatar.setVisibility(View.GONE);
        // 大于或等于四个人的时候的布局
        holder.rl_groupavatar.setVisibility(View.GONE);
        holder.avatar_1.setVisibility(View.GONE);
        holder.avatar_2.setVisibility(View.GONE);
        holder.avatar_3.setVisibility(View.GONE);
        holder.avatar_4.setVisibility(View.GONE);
        // 刚好等于三个人的时候的布局
        holder.rl_groupavatar_3.setVisibility(View.GONE);
        holder.avatar3_1.setVisibility(View.GONE);
        holder.avatar3_3.setVisibility(View.GONE);
        holder.avatar3_4.setVisibility(View.GONE);
        // 获取与此用户/群组的会话
        EMConversation conversation = getItem(position);
        // 获取用户username或者群组groupid
        String username = conversation.conversationId();
        Log.e("conversation-" + position, conversation.getType() + "-"
                + conversation.conversationId());
        if (conversation.getType() == EMConversationType.GroupChat) {
            // 群聊消息，显示群聊的四宫格头像
            EMGroup group = EMClient.getInstance().groupManager()
                    .getGroup(username);
            holder.name
                    .setText(group != null ? group.getGroupName() : username);
            if (group != null) {
                Log.e("group-" + position, group.getAffiliationsCount() + "-"
                        + group.getMembers().size());
                // 显示四个人的头像,他妈的的确没什么好办法，就用这种蠢办法，学弟学妹如果在维护这段代码别喷谢谢。都怪对面那做IOS的妹纸进度好快我好方....
                if (group.getMembers().size() == 1) {
                    holder.avatar.setVisibility(View.VISIBLE);
                    Glide.with(mCt)
                            .load(R.drawable.ease_group_icon)
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_group_icon)
                            .into(holder.avatar);

                } else if (group.getMembers().size() == 2) {
                    holder.rl_groupavatar.setVisibility(View.VISIBLE);
                    holder.avatar_1.setVisibility(View.VISIBLE);
                    holder.avatar_2.setVisibility(View.VISIBLE);

                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getOwner().toString() + ".png")
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar_1);
                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getMembers().get(1).toString() + ".png")
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar_2);

                } else if (group.getMembers().size() == 3) {
                    holder.rl_groupavatar_3.setVisibility(View.VISIBLE);
                    holder.avatar3_1.setVisibility(View.VISIBLE);
                    holder.avatar3_3.setVisibility(View.VISIBLE);
                    holder.avatar3_4.setVisibility(View.VISIBLE);

                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getOwner().toString() + ".png")
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar3_1);
                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getMembers().get(1).toString() + ".png")
                            .transform(new GlideCircleImage(mCt))

                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar3_3);
                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getMembers().get(2).toString() + ".png")
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar3_4);

                } else if (group.getMembers().size() >= 4) {
                    holder.rl_groupavatar.setVisibility(View.VISIBLE);
                    holder.avatar_1.setVisibility(View.VISIBLE);
                    holder.avatar_2.setVisibility(View.VISIBLE);
                    holder.avatar_3.setVisibility(View.VISIBLE);
                    holder.avatar_4.setVisibility(View.VISIBLE);
                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getOwner().toString() + ".png")
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar_1);
                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getMembers().get(1).toString() + ".png")
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar_2);
                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getMembers().get(2).toString() + ".png")
                            .transform(new GlideCircleImage(mCt))

                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar_3);
                    Glide.with(mCt)
                            .load(Constant.GETHEADIMAG_URL
                                    + group.getMembers().get(3).toString() + ".png")
                            .transform(new GlideCircleImage(mCt))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(holder.avatar_4);

                }
            }

        }
//        else if (conversation.getType() == EMConversationType.ChatRoom) {
//            holder.avatar.setImageResource(R.drawable.group_chat_icon);
//            EMChatRoom room = EMClient.getInstance().chatroomManager()
//                    .getChatRoom(username);
//            holder.name.setText(room != null
//                    && !TextUtils.isEmpty(room.getName()) ? room.getName()
//                    : username);
//        }
        else if (conversation.getType() == EMConversationType.Chat)
        // 不是群聊不是聊天室他妈的那肯定就是单聊咯！~
        {
            // EaseUserUtils.setUserAvatar(getContext(), username,
            // holder.avatar);
            // EaseUserUtils.setUserNick(username, holder.name);
            // 将来环信原来显示头像和昵称的方法修改为自己的方法
            holder.avatar.setVisibility(View.VISIBLE);
            String realname = mCache.getAsString(username + "_realname");
            if (realname == null || realname.equals("")) {
                try {
                    CommonUtils.getNickname(mCt, username, holder.name);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                holder.name.setText(realname);
            }
            if (username.equals("admin")) {
                Glide.with(getContext())
                        .load(Constant.GETHEADIMAG_URL + "168.png")
                        .transform(new GlideCircleImage(mCt))
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(holder.avatar);
            } else {
                Glide.with(getContext())
                        .load(Constant.GETHEADIMAG_URL + username + ".png")
                        .transform(new GlideCircleImage(mCt))
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(holder.avatar);
            }
        }

        if (conversation.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(conversation
                    .getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();

            IsAtEntity temp = SingleExampleAt.getInstance().getUnread().get(conversation.conversationId());
            //原来这个就是群号
//            Log.e(TAG, conversation.conversationId());
//不为空就表示有@信息
            if (temp != null) {
                if (!temp.isread()) {
                    String atTips = "[有人@了我] ";
                    String atContent = EaseCommonUtils.getMessageDigest(lastMessage,
                            (this.getContext()));
                    Spannable span = new SpannableString(atTips + atContent);
                    span.setSpan(new TextAppearanceSpan(mCt, R.style.at_text_tips_style), 0, atTips.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.message
                            .setText(EaseSmileUtils.getSmiledText(
                                    getContext(), span), BufferType.SPANNABLE);
//                    holder.message.setTextColor(mCt.getResources().getColor(R.color.holo_orange_light));
                } else {
                    holder.message
                            .setText(EaseSmileUtils.getSmiledText(
                                    getContext(),
                                    EaseCommonUtils.getMessageDigest(lastMessage,
                                            (this.getContext()))), BufferType.SPANNABLE);
                    holder.message.setTextColor(secondaryColor);
                }
            } else {
                holder.message
                        .setText(EaseSmileUtils.getSmiledText(
                                getContext(),
                                EaseCommonUtils.getMessageDigest(lastMessage,
                                        (this.getContext()))), BufferType.SPANNABLE);
                holder.message.setTextColor(secondaryColor);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(
                    lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND
                    && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        // 设置自定义属性
        // holder.name.setTextColor(primaryColor);

        holder.time.setTextColor(timeColor);
        if (primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if (secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    secondarySize);
        if (timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }

    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.conversationId();

                    EMGroup group = EMClient.getInstance().groupManager()
                            .getGroup(username);
                    if (group != null) {
                        // 如果是群名辣么就过滤群名字咯
                        username = group.getGroupName();
                    } else {
                        // 如果是个人名字辣么就过滤个人名字，弃用了原来的方法
                        EaseUser user = EaseUserUtils.getUserInfo(username);
                        // TODO: not support Nick anymore
                        if (user != null && user.getNick() != null) {
                            username = user.getNick();
                        } else {
                            username = mCache.getAsString(username);
                        }
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with
                        // space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            conversationList.clear();
            if (results.values != null) {
                conversationList.addAll((List<EMConversation>) results.values);
            }
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }

    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_itease_layout;
        // 群组头像特殊处理，仿钉钉那种四个人都的样纸---Erick 注
        RelativeLayout rl_groupavatar;
        // 群组的四个头像
        ImageView avatar_1;
        ImageView avatar_2;
        ImageView avatar_3;
        ImageView avatar_4;
        // 群组头像特殊处理，仿钉钉那种四个人都的样纸---Erick 注
        RelativeLayout rl_groupavatar_3;
        // 群组的四个头像
        ImageView avatar3_1;
        ImageView avatar3_3;
        ImageView avatar3_4;

    }
}
