package com.labour.lar.module;

import java.io.Serializable;
import java.util.List;

public class Department implements Serializable {

    private int nodeId;//: 1, 节点id
    private int pid;//: 1,父节点id

    private int id;//: 1,
    private String name;//: "测试项目",
    private int level;//: 1,

    private List<Department> children;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
    }
}
