package com.labour.lar.module;

import java.io.Serializable;
import java.util.List;

public class Linkman implements Serializable {

    private int nodeId;//: 1, 节点id
    private int pid;//: 1,父节点id

    private int id;//: 1,
    private String name;//: "测试项目",
    private String phone;//: "测试项目",
    private int level;//: 1,

    private List<Linkman> managers;
    private List<Linkman> operteams;
    private List<Linkman> staffs;
    private List<Linkman> classteams;
    private List<Linkman> employees;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Linkman> getManagers() {
        return managers;
    }

    public void setManagers(List<Linkman> managers) {
        this.managers = managers;
    }

    public List<Linkman> getOperteams() {
        return operteams;
    }

    public void setOperteams(List<Linkman> operteams) {
        this.operteams = operteams;
    }

    public List<Linkman> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Linkman> staffs) {
        this.staffs = staffs;
    }

    public List<Linkman> getClassteams() {
        return classteams;
    }

    public void setClassteams(List<Linkman> classteams) {
        this.classteams = classteams;
    }

    public List<Linkman> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Linkman> employees) {
        this.employees = employees;
    }
}
