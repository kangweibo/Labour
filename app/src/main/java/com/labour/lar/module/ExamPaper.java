package com.labour.lar.module;

import java.io.Serializable;
import java.util.List;

/**
 * 试卷
 */
public class ExamPaper implements Serializable {
    private int id;//": id
    private String title;//": 测试题1
    private String examtime;//": 时长
    private String passpercent;//": 及格分数
    private String q1num;//": 判断题数量
    private String q2num;//": 单选题数量
    private String q3num;//
    private String q4num;//

    private List<Question1> q1s;
    private List<Question2> q2s;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExamtime() {
        return examtime;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }

    public String getPasspercent() {
        return passpercent;
    }

    public void setPasspercent(String passpercent) {
        this.passpercent = passpercent;
    }

    public String getQ1num() {
        return q1num;
    }

    public void setQ1num(String q1num) {
        this.q1num = q1num;
    }

    public String getQ2num() {
        return q2num;
    }

    public void setQ2num(String q2num) {
        this.q2num = q2num;
    }

    public String getQ3num() {
        return q3num;
    }

    public void setQ3num(String q3num) {
        this.q3num = q3num;
    }

    public String getQ4num() {
        return q4num;
    }

    public void setQ4num(String q4num) {
        this.q4num = q4num;
    }

    public List<Question1> getQ1s() {
        return q1s;
    }

    public void setQ1s(List<Question1> q1s) {
        this.q1s = q1s;
    }

    public List<Question2> getQ2s() {
        return q2s;
    }

    public void setQ2s(List<Question2> q2s) {
        this.q2s = q2s;
    }
}
