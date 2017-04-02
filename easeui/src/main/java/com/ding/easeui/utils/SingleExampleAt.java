package com.ding.easeui.utils;


import com.ding.easeui.domain.IsAtEntity;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Erick on 2016/9/21.
 */
public class SingleExampleAt {
    /**
     * String记录的是群号
     * IsAtEntity记录的是艾特的信息
     * 逻辑：当
     */
    Map<String, IsAtEntity> unread;

    private SingleExampleAt() {
        unread = new HashMap<>();
    }

    private static final SingleExampleAt instance = new SingleExampleAt();

    public static SingleExampleAt getInstance() {
        return instance;
    }

    public Map<String, IsAtEntity> getUnread() {
        return unread;
    }

    public void setUnread(Map<String, IsAtEntity> unread) {
        this.unread = unread;
    }
}
