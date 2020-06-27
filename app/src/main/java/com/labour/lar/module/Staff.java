package com.labour.lar.module;

import java.io.Serializable;

/**
 * 作业队成员
 */
public class Staff implements Serializable {
    private int id;//": 2,
    private String operteam_id;//":":项目id
    private String name;//":": "张三",
    private String duty;//":": "12200030001",
    private String phone;//":": "12200030001",
    private String pic;// "url": "/uploads/staff/pic/2/temp.png"
    private String memo;//":": "110101161102060468",

    private String created_at;//":": "",
    private String updated_at;// ""
    private String gender;//":": "110101161102060468",
    private String idcard;//":": "110101161102060468",

    private String idpic1;//"url": "/uploads/staff/idpic1/2/temp.png"
    private String idpic2;//"url": "/uploads/staff/idpic2/2/temp.png"

    private int prole_id;//":": 5,
    private String prole;//":": "staff",
    private String nation;//":": null,
    private String birthday;//":": null,
    private String address;//":": null,

    private boolean identified;//": true,
    private String prolename;//":": null


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperteam_id() {
        return operteam_id;
    }

    public void setOperteam_id(String operteam_id) {
        this.operteam_id = operteam_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdpic1() {
        return idpic1;
    }

    public void setIdpic1(String idpic1) {
        this.idpic1 = idpic1;
    }

    public String getIdpic2() {
        return idpic2;
    }

    public void setIdpic2(String idpic2) {
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
}
