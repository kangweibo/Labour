package com.labour.lar.module;

import java.io.Serializable;

public class FZMessage implements Serializable {

    private int id;//: 1,
    private String fromtag;//: "project",
    private int fromid;//: 1,
    private String totag;//: "classteam",
    private int toid;//: 1,
    private String title;//: "标题",
    private String content;//: "内容",
    private String mlinkto;//: "",
    private String created_at;//: "2020-06-22T02:23:34.000Z",
    private String updated_at;//:"2020-06-22T02:23:34.000Z"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromtag() {
        return fromtag;
    }

    public void setFromtag(String fromtag) {
        this.fromtag = fromtag;
    }

    public int getFromid() {
        return fromid;
    }

    public void setFromid(int fromid) {
        this.fromid = fromid;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public int getToid() {
        return toid;
    }

    public void setToid(int toid) {
        this.toid = toid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMlinkto() {
        return mlinkto;
    }

    public void setMlinkto(String mlinkto) {
        this.mlinkto = mlinkto;
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
