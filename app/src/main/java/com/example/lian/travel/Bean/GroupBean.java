package com.example.lian.travel.Bean;


//Group群组类
public class GroupBean {
    private String id;//群组id
    private int head; //头像
    private String title; //信息标题
    private String message; //信息内容
    private String population; //群组当前人数

    public GroupBean(String id, int head, String title, String message, String population) {
        this.id = id;
        this.head = head;
        this.title = title;
        this.message = message;
        this.population = population;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }
}
