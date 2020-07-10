package com.labour.lar.module;

import java.io.Serializable;

public class Question1 implements Serializable {
    private int id;//": id
    private String question;//": 问题
    private String answer;//: "错误"
    private String score;//": 5
    private String empanswer;//: "错误"
    private boolean isright;//: false

    private boolean isShowAnswer;//: 是否显示答案

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

    public boolean isShowAnswer() {
        return isShowAnswer;
    }

    public void setShowAnswer(boolean showAnswer) {
        isShowAnswer = showAnswer;
    }
}
