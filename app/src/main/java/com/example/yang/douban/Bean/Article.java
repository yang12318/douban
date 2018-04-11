package com.example.yang.douban.Bean;

/**
 * Created by youxihouzainali on 2018/3/31.
 */

public class Article {
    private int id;
    private int authorId;
    private String title;
    private String author;
    private String pub_time;
    private int click_num;
    private String text;
    private String src;

    public Article(int id, int authorId, String title, String pub_time, int click_num, String text, String src) {
        this.title = title;
        this.id = id;
        this.authorId = authorId;
        this.pub_time = pub_time;
        this.click_num = click_num;
        this.text = text;
        this.src = src;
    }

    public Article() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
