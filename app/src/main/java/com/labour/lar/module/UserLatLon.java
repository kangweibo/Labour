package com.labour.lar.module;

import com.labour.lar.Constants;

import java.io.Serializable;

//用户经纬度
public class UserLatLon implements Serializable {

    private int userId;
    private String lat;
    private String lon;
    private String createTime;
    private Constants.ROLE role;

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

    public Constants.ROLE getRole() {
        return role;
    }

    public void setRole(Constants.ROLE role) {
        this.role = role;
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
