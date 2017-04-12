package com.wust.newsmartschool.domain;

public class AppoCenterItem
{
    private String room;
    private String oldRoom;
    private String seat;
    private String oldSeat;
    private String action;
    private String oldAction;
    private String time;
    private boolean isClickable;

    public AppoCenterItem() {

    }

    public void setClickable(boolean isClickable)
    {
        this.isClickable = isClickable;
    }

    public boolean isClickable()
    {
        return isClickable;
    }

    public String getRoom()
    {
        return room;
    }

    public String getOldRoom()
    {
        return oldRoom;
    }

    public void setRoom(String room)
    {
        oldRoom = room;
        String[] rooms = {
                "wtf",  "南三(社会科学阅览室)", "南四(工程技术阅览室)", "南五(基础科学阅览室)", "南六(政法经管阅览室)",
                "北三(建筑,艺术阅览室)", "北四(生物,医药阅览室)", "", "期刊阅览室", "", "", "一楼阅览室", "", "三楼阅览室", "四楼阅览室", "五楼阅览室", "六楼阅览室"};
        this.room = rooms[Integer.parseInt(room)];
    }

    public String getSeat()
    {
        return seat;
    }

    public String getOldSeat()
    {
        return oldSeat;
    }

    public void setSeat(String seat)
    {
        oldSeat = seat;
        switch (seat.length())
        {
            case 1:
                this.seat = "00" + seat;
                break;

            case 2:
                this.seat = "0" + seat;
                break;

            default:
                this.seat = seat;
                break;
        }
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        oldAction = action;
        String[] actions = {
                "wtf", "预约成功", "正在使用", "下线成功", "已取消预约", "系统下线", "标记座位", "离开", "重新使用", "标记改为空闲" };
        this.action = actions[Integer.parseInt(action)];
    }

    public String getOldAction()
    {
        return oldAction;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }
}

