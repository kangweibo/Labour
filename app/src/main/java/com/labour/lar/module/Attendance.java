package com.labour.lar.module;

import java.io.Serializable;

public class Attendance implements Serializable {
    private int id;//": id
    private String usertype;//": employee
    private String userid;//: 1
    private String clockdate;//: "2020-08-18",
    private String clockintime;// "08:00:00",
    private String clockouttime;// "18:00:00",
    private String clockingeo;// "114.4840338,38.04284795",
    private String clockoutgeo;// "",
    private String clockinmemo;//  null,
    private String clockoutmemo;//  null,
    private String created_at; //"2020-08-18 06:36:01",
    private String updated_at; //"2020-08-20 03:30:26",
    private String employee_id;//": 1
    private String staff_id;//: null
    private String manager_id;//": null
    private String inaddr;//: "河北省石家庄市桥西区中山街道中山西路44号华银大厦写字楼(中山西路)"
    private String outaddr;//": null

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getClockdate() {
        return clockdate;
    }

    public void setClockdate(String clockdate) {
        this.clockdate = clockdate;
    }

    public String getClockintime() {
        return clockintime;
    }

    public void setClockintime(String clockintime) {
        this.clockintime = clockintime;
    }

    public String getClockouttime() {
        return clockouttime;
    }

    public void setClockouttime(String clockouttime) {
        this.clockouttime = clockouttime;
    }

    public String getClockingeo() {
        return clockingeo;
    }

    public void setClockingeo(String clockingeo) {
        this.clockingeo = clockingeo;
    }

    public String getClockoutgeo() {
        return clockoutgeo;
    }

    public void setClockoutgeo(String clockoutgeo) {
        this.clockoutgeo = clockoutgeo;
    }

    public String getClockinmemo() {
        return clockinmemo;
    }

    public void setClockinmemo(String clockinmemo) {
        this.clockinmemo = clockinmemo;
    }

    public String getClockoutmemo() {
        return clockoutmemo;
    }

    public void setClockoutmemo(String clockoutmemo) {
        this.clockoutmemo = clockoutmemo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getInaddr() {
        return inaddr;
    }

    public void setInaddr(String inaddr) {
        this.inaddr = inaddr;
    }

    public String getOutaddr() {
        return outaddr;
    }

    public void setOutaddr(String outaddr) {
        this.outaddr = outaddr;
    }
}
