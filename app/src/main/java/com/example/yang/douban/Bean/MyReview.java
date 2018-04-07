package com.example.yang.douban.Bean;

/**
 * Created by youxihouzainali on 2018/4/7.
 */

public class MyReview {
    private int id;
    private String pub_time;
    private String text;
    private int bookId;
    private String bookName;

    public MyReview() {

    }

    public MyReview(int id, String pub_time, String text, int bookId, String bookName) {
        this.id = id;
        this.pub_time = pub_time;
        this.text = text;
        this.bookId = bookId;
        this.bookName = bookName;
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

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
