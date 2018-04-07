package com.example.yang.douban.Bean;

/**
 * Created by youxihouzainali on 2018/4/7.
 */

public class BookReview {
    private int id;
    private String pub_time;
    private String text;
    private int commenterId;
    private String commenterName;

    public BookReview() {

    }

    public BookReview(int id, String pub_time, String text, int commenterId, String commenterName) {
        this.id = id;
        this.pub_time = pub_time;
        this.text = text;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }
}
