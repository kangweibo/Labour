package com.labour.lar.module;

import java.io.Serializable;

/**
 * 班组
 */
public class Classteam implements Serializable {
    private int id;//": 999999999,班组id
    private int operteam_id;//": 999999999,作业队id
    private String name;//": "unknown",班组名称
    private String memo;//": null,班组备注
    private String created_at;//": "2020-05-26T01:55:06.000Z",
    private String updated_at;//": "2020-05-26T01:55:06.000Z",
    private String operteamname;//作业队
    private String staff_num;//作业队成员数
    private String classteam_num;//作业队班组数
    private String all_num;//作业队总人数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOperteam_id() {
        return operteam_id;
    }

    public void setOperteam_id(int operteam_id) {
        this.operteam_id = operteam_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getOperteamname() {
        return operteamname;
    }

    public void setOperteamname(String operteamname) {
        this.operteamname = operteamname;
    }

    public String getStaff_num() {
        return staff_num;
    }

    public void setStaff_num(String staff_num) {
        this.staff_num = staff_num;
    }

    public String getClassteam_num() {
        return classteam_num;
    }

    public void setClassteam_num(String classteam_num) {
        this.classteam_num = classteam_num;
    }

    public String getAll_num() {
        return all_num;
    }

    public void setAll_num(String all_num) {
        this.all_num = all_num;
    }
}
