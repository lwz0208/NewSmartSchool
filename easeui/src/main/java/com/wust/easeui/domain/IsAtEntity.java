package com.wust.easeui.domain;

import com.hyphenate.chat.EMMessage;

import java.io.Serializable;

/**
 * Created by Erick on 2016/9/21.
 */
public class IsAtEntity implements Serializable {
    EMMessage message;
    boolean isread;

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }

    public boolean isread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }
}
