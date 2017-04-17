package com.wust.newsmartschool.domain;


public class RMessage {
    private String messageId;
    private String  messageTitle;
    private String  messageContent;
    private String  messageTime;
    private String  messagePic;
    private String  messageResource; //信息来源
    private int clickNum;
    public int getClickNum() {
        return clickNum;
    }
    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }
    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getMessageTitle() {
        return messageTitle;
    }
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
    public String getMessageContent() {
        return messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
    public String getMessageTime() {
        return messageTime;
    }
    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
    public String getMessagePic() {
        return messagePic;
    }
    public void setMessagePic(String messagePic) {
        this.messagePic = messagePic;
    }
    public String getMessageResource() {
        return messageResource;
    }
    public void setMessageResource(String messageResource) {
        this.messageResource = messageResource;
    }
}

