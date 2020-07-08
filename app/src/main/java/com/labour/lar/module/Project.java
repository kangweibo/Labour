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
    private String budget;//: null,
    private String created_at;//: "2020-05-13T01:36:29.000Z",
    private String updated_at;//: "2020-05-13T01:36:29.000Z",
    private String entname;//: "方正通信建设有限公司"
    private String manager_num;//成员人数
    private String operteam_num;//作业队数量
    private String all_num;//项目总人数
    private String pm;//项目经理名称

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
}
