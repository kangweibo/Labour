package com.labour.lar.module;

import java.io.Serializable;

//用户经纬度
public class UserLatLon implements Serializable {

    private int userId;
    private String lat;
    private String lon;
    private String createTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserLatLon{" +
                "userId=" + userId +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
