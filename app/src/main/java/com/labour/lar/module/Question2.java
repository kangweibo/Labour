package com.labour.lar.module;

import java.io.Serializable;

public class Question2 implements Serializable {
    private int id;//": id
    private String question;//": 问题
    private String choicea;//": A、重大事故2小时内上报",
    private String choiceb;//": B、一般事故3小时内必须上报",
    private String choicec;//": C、现场人员应立即报告"
    private String choiced;//": D、现场人员应立即报告"
    private String answer;//": C
    private String score;//": 5
    private String empanswer;//: C
    private boolean isright;//: false

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoicea() {
        return choicea;
    }

    public void setChoicea(String choicea) {
        this.choicea = choicea;
    }

    public String getChoiceb() {
        return choiceb;
    }

    public void setChoiceb(String choiceb) {
        this.choiceb = choiceb;
    }

    public String getChoicec() {
        return choicec;
    }

    public void setChoicec(String choicec) {
        this.choicec = choicec;
    }

    public String getChoiced() {
        return choiced;
    }

    public void setChoiced(String choiced) {
        this.choiced = choiced;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getEmpanswer() {
        return empanswer;
    }

    public void setEmpanswer(String empanswer) {
        this.empanswer = empanswer;
    }

    public boolean isIsright() {
        return isright;
    }

    public void setIsright(boolean isright) {
        this.isright = isright;
    }
}
