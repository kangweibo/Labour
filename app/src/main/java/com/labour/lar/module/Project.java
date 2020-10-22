package com.labour.lar.module;

import java.io.Serializable;

public class Project implements Serializable {

    private int id;//: 1,
    private int ent_id;//: 1,
    private String name;//: "南花园C座",
    private String supervisor;//: "建设工程咨询监理有限公司",
    private String construction;//: "建筑工程局有限公司",
    private String designer;//: "工程设计有限公司",
    private String projectfunction;//: "写字楼",
    private String buildaera;//: "288000平方米",
    private String budget;//: 合同总额,
    private String created_at;//: "2020-05-13T01:36:29.000Z",
    private String updated_at;//: "2020-05-13T01:36:29.000Z",
    private String startdate;//: "2020-05-13T01:36:29.000Z",
    private String enddate;//: "2020-05-13T01:36:29.000Z",
    private String entname;//: "方正通信建设有限公司"
    private String manager_num;//成员人数
    private String operteam_num;//作业队数量
    private String all_num;//项目总人数
    private String pm;//项目经理名称
    private String duration;//工期
    private String onjobnum;//上岗人数
    private String ondutynum;//在岗人数
    private String totalworkday;//	累计工时
    private String totalsalary;//	发放总额
    private String onjobnum_xmb;// 项目部上岗人数
    private String ondutynum_xmb;// 项目部在岗人数
    private String totalworkday_xmb;// 项目部累计工时
    private String totalsalary_xmb;// 项目部发放总额
    private int clockinfence;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(int ent_id) {
        this.ent_id = ent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getProjectfunction() {
        return projectfunction;
    }

    public void setProjectfunction(String projectfunction) {
        this.projectfunction = projectfunction;
    }

    public String getBuildaera() {
        return buildaera;
    }

    public void setBuildaera(String buildaera) {
        this.buildaera = buildaera;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
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

    public String getEntname() {
        return entname;
    }

    public void setEntname(String entname) {
        this.entname = entname;
    }

    public String getManager_num() {
        return manager_num;
    }

    public void setManager_num(String manager_num) {
        this.manager_num = manager_num;
    }

    public String getOperteam_num() {
        return operteam_num;
    }

    public void setOperteam_num(String operteam_num) {
        this.operteam_num = operteam_num;
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

    public String getTotalworkday() {
        return totalworkday;
    }

    public void setTotalworkday(String totalworkday) {
        this.totalworkday = totalworkday;
    }

    public String getTotalsalary() {
        return totalsalary;
    }

    public void setTotalsalary(String totalsalary) {
        this.totalsalary = totalsalary;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOnjobnum_xmb() {
        return onjobnum_xmb;
    }

    public void setOnjobnum_xmb(String onjobnum_xmb) {
        this.onjobnum_xmb = onjobnum_xmb;
    }

    public String getOndutynum_xmb() {
        return ondutynum_xmb;
    }

    public void setOndutynum_xmb(String ondutynum_xmb) {
        this.ondutynum_xmb = ondutynum_xmb;
    }

    public String getTotalworkday_xmb() {
        return totalworkday_xmb;
    }

    public void setTotalworkday_xmb(String totalworkday_xmb) {
        this.totalworkday_xmb = totalworkday_xmb;
    }

    public String getTotalsalary_xmb() {
        return totalsalary_xmb;
    }

    public void setTotalsalary_xmb(String totalsalary_xmb) {
        this.totalsalary_xmb = totalsalary_xmb;
    }

    public int getClockinfence() {
        return clockinfence;
    }

    public void setClockinfence(int clockinfence) {
        this.clockinfence = clockinfence;
    }
}
