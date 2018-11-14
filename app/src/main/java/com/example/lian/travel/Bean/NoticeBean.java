package com.example.lian.travel.Bean;

public class NoticeBean {
    private String group_id;//群组id
    private String group_name;//群名
    private String applyer;//申请人
    private String accepter;//接受人
    private String reason;//申请理由
    private int head_portrait;//头像
    private String title;//标题
    private String sort_msg;//简略消息
    private String handle;//处理人

    public NoticeBean(String group_id, String group_name, String applyer, String accepter, String reason, int head_portrait, String title, String sort_msg, String handle) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.applyer = applyer;
        this.accepter = accepter;
        this.reason = reason;
        this.head_portrait = head_portrait;
        this.title = title;
        this.sort_msg = sort_msg;
        this.handle = handle;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(int head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSort_msg() {
        return sort_msg;
    }

    public void setSort_msg(String sort_msg) {
        this.sort_msg = sort_msg;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}
