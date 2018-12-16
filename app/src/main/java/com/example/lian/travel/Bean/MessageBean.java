package com.example.lian.travel.Bean;

public class MessageBean {
    private String id;//群组id
    private int head_portrait;//头像
    private String group_name;//组名
    private String sort_msg;//简略消息
    private String last_msg;//最后一条消息
    private String time;//时间
    private String owner;//群主
    private String unread_number;//未读消息数量

    public MessageBean(String id, int head_portrait, String group_name, String sort_msg, String last_msg, String time, String owner, String unread_number) {
        this.id = id;
        this.head_portrait = head_portrait;
        this.group_name = group_name;
        this.sort_msg = sort_msg;
        this.last_msg = last_msg;
        this.time = time;
        this.owner = owner;
        this.unread_number = unread_number;
    }

    public String getUnread_number() {
        return unread_number;
    }

    public void setUnread_number(String unread_number) {
        this.unread_number = unread_number;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(int head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getSort_msg() {
        return sort_msg;
    }

    public void setSort_msg(String sort_msg) {
        this.sort_msg = sort_msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
