package com.labour.lar.module;

import java.io.Serializable;

/**
 * 党建内容
 */
public class Party implements Serializable {
    private int id;//: 1,
    private int partytype_id;// 内容id
    private String title;//: "",
    private String memo;//:
    private String content;//
    private String created_at;//:  "2020-05-13T03:02:29.000Z",
    private String updated_at;//:  "2020-05-13T03:02:29.000Z",

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartytype_id() {
        return partytype_id;
    }

    public void setPartytype_id(int partytype_id) {
        this.partytype_id = partytype_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
