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
    private String projectname;//:  "南花园C座"
    private String staff_num;//:  成员人数
    private String classteam_num;//:  班组数

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
}
