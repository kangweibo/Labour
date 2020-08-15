package com.labour.lar.module;

import com.labour.lar.Constants;

import java.io.Serializable;

public class User implements Serializable {
    private int id;//: 4,
    private String name;//": "大师1",
    private String phone;//": "13730129660",
    /**
     * empoyee：工人
     * staff：作业队成员
     * manager：项目部成员
     */
    private String prole;//": "manager",角色

    private Ent ent;//企业

    //项目部: 项目-->企业
    private String projectname;//": "unknown"
    private Project project;

    //作业队: 作业队-->项目-->企业
    private Operteam operteam;
    private String operteamname;//": "unknown";

    //工人： 班组-->作业队-->项目-->企业
    private Classteam classteam;
    private String classteamname;//": "unknown"

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

    public String getProle() {
        return prole;
    }

    public Constants.ROLE getRole(){
        if(prole.equals("employee")){
            return Constants.ROLE.employee;
        } else if(prole.equals("staff")){
            return Constants.ROLE.staff;
        }
        return Constants.ROLE.manager;
    }

    public void setProle(String prole) {
        this.prole = prole;
    }

    public Ent getEnt() {
        return ent;
    }

    public void setEnt(Ent ent) {
        this.ent = ent;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Classteam getClassteam() {
        return classteam;
    }

    public void setClassteam(Classteam classteam) {
        this.classteam = classteam;
    }

    public Operteam getOperteam() {
        return operteam;
    }

    public void setOperteam(Operteam operteam) {
        this.operteam = operteam;
    }

    public String getOperteamname() {
        return operteamname;
    }

    public void setOperteamname(String operteamname) {
        this.operteamname = operteamname;
    }

    public String getClassteamname() {
        return classteamname;
    }

    public void setClassteamname(String classteamname) {
        this.classteamname = classteamname;
    }

    //企业
    public static class Ent implements Serializable {
        private int id;//": 999999999,企业id
        private String name;//": "unknown",企业名称
        private String linkman;//": null,联系人
        private String linktel;//": null,联系电话
        private int admin_user_id;//": 999999999,
        private String created_at;//": "2020-05-26T01:50:18.000Z",
        private String updated_at;//": "2020-05-26T01:50:18.000Z"

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

        public String getLinkman() {
            return linkman;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public String getLinktel() {
            return linktel;
        }

        public void setLinktel(String linktel) {
            this.linktel = linktel;
        }

        public int getAdmin_user_id() {
            return admin_user_id;
        }

        public void setAdmin_user_id(int admin_user_id) {
            this.admin_user_id = admin_user_id;
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
    }

    /**
     * 项目
     */
    public static class Project implements Serializable {
        private int id;//": 999999999,项目部id
        private int ent_id;//": 999999999,企业id
        private String name;//": "unknown",项目名称
        private String supervisor;//": null,建设单位
        private String construction;//": null,监理单位
        private String designer;//": null,设计单位
        private String projectfunction;//": null,项目功能
        private String buildaera;//": null,面积
        private String budget;//": null,预算
        private String created_at;//": "2020-05-26T01:51:10.000Z",
        private String updated_at;//": "2020-05-26T01:51:10.000Z",
        private Ent ent;//企业

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

        public Ent getEnt() {
            return ent;
        }

        public void setEnt(Ent ent) {
            this.ent = ent;
        }
    }

    //作业队
    public static class Operteam implements Serializable {
        private int id;//": 999999999,作业队id
        private int project_id;//": 999999999,所属项目id
        private String name;//": "unknown",作业队名称
        private String memo;//": null,作业队备注
        private String duty;//": null,作业队职责
        private String created_at;//": "2020-05-26T01:54:17.000Z",
        private String updated_at;//": "2020-05-26T01:54:17.000Z",
        private Project project;//项目部

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

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }
    }

    //班组
    public static class Classteam implements Serializable {
        private int id;//": 999999999,班组id
        private int operteam_id;//": 999999999,作业队id
        private String name;//": "unknown",班组名称
        private String memo;//": null,班组备注
        private String created_at;//": "2020-05-26T01:55:06.000Z",
        private String updated_at;//": "2020-05-26T01:55:06.000Z",
        private Operteam operteam;//作业队

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

        public Operteam getOperteam() {
            return operteam;
        }

        public void setOperteam(Operteam operteam) {
            this.operteam = operteam;
        }


    }
}
