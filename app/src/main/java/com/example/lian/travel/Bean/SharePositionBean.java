package com.example.lian.travel.Bean;

public class SharePositionBean {
    private double latitude;//经度
    private double longitude;//维度
    private int icon;//头像
    private String title;//标题

    public SharePositionBean(double latitude, double longitude, int icon, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
