package com.example.lian.travel.Bean;

public class MessageBean {
    private String id;//群组id
    private int head_portrait;//头像
    private String group_name;//组名
    private String sort_msg;//简略消息
    private String time;//时间
    private String owner;

    public MessageBean(String id, int head_portrait, String group_name, String sort_msg, String time, String owner) {
        this.id = id;
        this.head_portrait = head_portrait;
        this.group_name = group_name;
        this.sort_msg = sort_msg;
        this.time = time;
        this.owner = owner;
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
