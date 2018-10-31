package com.example.lian.travel.Bean;

public class NoticeBean {
    private int head_portrait;//头像
    private String title;//标题
    private String sort_msg;//简略消息
    private String handle;//处理人

    public NoticeBean(int head_portrait, String title, String sort_msg, String handle) {
        this.head_portrait = head_portrait;
        this.title = title;
        this.sort_msg = sort_msg;
        this.handle = handle;
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
