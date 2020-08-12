package com.labour.lar.module;

import java.io.Serializable;

public class Salary implements Serializable {
    private int id;//": id
    private String employee_id;//": 1
    private String staff_id;//: "错误"
    private String manager_id;//": 5
    private String cardno;//: "6666 6666 6666 666",
    private String amount;//: "5000.0",
    private String memo;// null,
    private String created_at;// "2020-08-10T00:49:41.000Z",
    private String updated_at;// "2020-08-10T00:49:41.000Z",
    private String sadate;// null,
    private String sayear;// "2020",
    private String samonth;// "02"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getSadate() {
        return sadate;
    }

    public void setSadate(String sadate) {
        this.sadate = sadate;
    }

    public String getSayear() {
        return sayear;
    }

    public void setSayear(String sayear) {
        this.sayear = sayear;
    }

    public String getSamonth() {
        return samonth;
    }

    public void setSamonth(String samonth) {
        this.samonth = samonth;
    }
}
