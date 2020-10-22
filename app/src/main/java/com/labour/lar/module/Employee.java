package com.labour.lar.module;

import java.io.Serializable;

/**
 * 班组成员
 */
public class Employee implements Serializable {
    private int id;//": 2,
    private String classteam_id;//":":班组id
    private String name;//":": "张三",
    private String phone;//":": "12200030001",
    private String idcard;//":": "110101161102060468",

    private String email;//":": "111@qq.com",
    private String status;//":": "1",
    private String currentstatus;

    private Pic pic;// "url": "/uploads/staff/pic/2/temp.png"
    private Pic idpic1;//"url": "/uploads/staff/idpic1/2/temp.png"
    private Pic idpic2;//"url": "/uploads/staff/idpic2/2/temp.png"

    private int prole_id;//":": 5,
    private String prole;//":": "staff",
    private String nation;//":": null,
    private String birthday;//":": null,
    private String address;//":": null,

    private boolean identified;//": true,
    private String prolename;//":": null
    private String projectname;//":": 项目名称
    private String operteamname;//":": 作业队名称
    private String classteamname;//":": 班组名称
    private String duty;//":": 职务
    private String bankcard;
    private String examstatus;
    private String totalworkday;//	累计工时
    private String totalsalary;//	发放总额

    public static class Pic implements Serializable {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassteam_id() {
        return classteam_id;
    }

    public void setClassteam_id(String classteam_id) {
        this.classteam_id = classteam_id;
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

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentstatus() {
        return currentstatus;
    }

    public void setCurrentstatus(String currentstatus) {
        this.currentstatus = currentstatus;
    }

    public Pic getPic() {
        return pic;
    }

    public void setPic(Pic pic) {
        this.pic = pic;
    }

    public Pic getIdpic1() {
        return idpic1;
    }

    public void setIdpic1(Pic idpic1) {
        this.idpic1 = idpic1;
    }

    public Pic getIdpic2() {
        return idpic2;
    }

    public void setIdpic2(Pic idpic2) {
        this.idpic2 = idpic2;
    }

    public int getProle_id() {
        return prole_id;
    }

    public void setProle_id(int prole_id) {
        this.prole_id = prole_id;
    }

    public String getProle() {
        return prole;
    }

    public void setProle(String prole) {
        this.prole = prole;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIdentified() {
        return identified;
    }

    public void setIdentified(boolean identified) {
        this.identified = identified;
    }

    public String getProlename() {
        return prolename;
    }

    public void setProlename(String prolename) {
        this.prolename = prolename;
    }

    public String getClassteamname() {
        return classteamname;
    }

    public void setClassteamname(String classteamname) {
        this.classteamname = classteamname;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getOperteamname() {
        return operteamname;
    }

    public void setOperteamname(String operteamname) {
        this.operteamname = operteamname;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getExamstatus() {
        return examstatus;
    }

    public void setExamstatus(String examstatus) {
        this.examstatus = examstatus;
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
}
