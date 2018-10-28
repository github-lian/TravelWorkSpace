package com.example.lian.travel.Bean;

public class MessageBean {
    private String head_portrait;//头像
    private String group_name;//组名
    private String sort_msg;//简略消息
    private String time;//时间

    public MessageBean(String head_portrait, String group_name, String sort_msg, String time) {
        this.head_portrait = head_portrait;
        this.group_name = group_name;
        this.sort_msg = sort_msg;
        this.time = time;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
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
