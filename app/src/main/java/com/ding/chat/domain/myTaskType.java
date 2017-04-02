package com.ding.chat.domain;

/**
 * Created by Erick on 2016/10/27.
 */
public class myTaskType {
    int taskID;
    String taskName;

    public myTaskType(int id, String name) {
        this.taskID = id;
        this.taskName = name;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getTaskName() {
        return taskName;
    }
}
