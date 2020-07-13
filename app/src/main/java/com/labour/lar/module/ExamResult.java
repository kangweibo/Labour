package com.labour.lar.module;

import java.io.Serializable;
import java.util.List;

/**
 * 考试成绩
 */
public class ExamResult implements Serializable {
    private int id;//": 考试成绩id
    private int employee_id;//": 工人id
    private int exam_id;//": 试卷id
    private int totalscore;//": 得分分数
    private int fullscore;//": 满分分数
    private int rightnum;//": 对题数量
    private int wrongnum;//": 错题数量
    private String updated_at;//": 更新时间
    private boolean ispass;// 是否通过

    private List<Question1> q1s;
    private List<Question2> q2s;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public int getFullscore() {
        return fullscore;
    }

    public void setFullscore(int fullscore) {
        this.fullscore = fullscore;
    }

    public int getRightnum() {
        return rightnum;
    }

    public void setRightnum(int rightnum) {
        this.rightnum = rightnum;
    }

    public int getWrongnum() {
        return wrongnum;
    }

    public void setWrongnum(int wrongnum) {
        this.wrongnum = wrongnum;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isIspass() {
        return ispass;
    }

    public void setIspass(boolean ispass) {
        this.ispass = ispass;
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
