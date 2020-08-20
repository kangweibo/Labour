package com.labour.lar.module;

import java.io.Serializable;

/**
 * 作业队
 */
public class Operteam implements Serializable {
    private int id;//: 1,
    private int project_id;// 项目id
    private String name;//: "作业队1",
    private String memo;//:
    private String duty;//
    private String created_at;//:  "2020-05-13T03:02:29.000Z",
    private String updated_at;//:  "2020-05-13T03:02:29.000Z",
    private String startdate;//: "2020-05-13T01:36:29.000Z",
    private String enddate;//: "2020-05-13T01:36:29.000Z",
    private String projectname;//:  "南花园C座"
    private String staff_num;//:  成员人数
    private String classteam_num;//:  班组数
    private String all_num;//项目总人数
    private String pm;//作业队长名称
    private String ondutynum;//上岗人数
    private String onjobnum;//在岗人数
    private String duration;//工期
    private String budget;//预算金额

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
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

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
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

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
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

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getOndutynum() {
        return ondutynum;
    }

    public void setOndutynum(String ondutynum) {
        this.ondutynum = ondutynum;
    }

    public String getOnjobnum() {
        return onjobnum;
    }

    public void setOnjobnum(String onjobnum) {
        this.onjobnum = onjobnum;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
