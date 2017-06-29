package com.junova.huizhong.model;

public class NewsParam {

    String title;
    String content;
    String date;
    String id;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NewsParam(String title, String content, String date) {
        super();
        this.title = title;
        this.content = content;
        this.date = date;

    }

}
