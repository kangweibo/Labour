package com.labour.lar.module;

import java.io.Serializable;

/**
 * 党建内容类别
 */
public class Partytype implements Serializable {
    private int id;//: 1,
    private String name;//: "",
    private String memo;//:
    private String created_at;//:  "2020-05-13T03:02:29.000Z",
    private String updated_at;//:  "2020-05-13T03:02:29.000Z",

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
}
