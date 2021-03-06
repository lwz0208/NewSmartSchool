package com.wust.easeui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wust.easeui.controller.EaseUI;
import com.wust.easeui.domain.EaseUser;
import com.wust.easeui.R;

public class EaseUserUtils {

    static EaseUI.EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * 根据username获取相应user
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username,
                                     ImageView imageView) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                // 正常的string路径
                Glide.with(context).load(user.getAvatar())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar)
                    .into(imageView);
        }
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                Log.e("setUserNick", user.getNick());
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

}
