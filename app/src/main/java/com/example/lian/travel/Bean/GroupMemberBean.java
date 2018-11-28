package com.example.lian.travel.Bean;

public class GroupMemberBean {
    private String name;//群组id
    private int head; //头像


    public GroupMemberBean(String name, int head) {
        this.name = name;
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }
}
