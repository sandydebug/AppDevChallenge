package com.example.appdevchallenge.Model;

public class EmailModel {

    private String desc;
    private String from;
    private String date;
    private String msg;

    public EmailModel() {
    }

    public EmailModel(String desc, String from, String date, String msg) {
        this.desc = desc;
        this.from = from;
        this.date = date;
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
