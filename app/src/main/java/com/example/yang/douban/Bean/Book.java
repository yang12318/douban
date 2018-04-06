package com.example.yang.douban.Bean;

/**
 * Created by youxihouzainali on 2018/3/31.
 */

public class Book {
    private int id;
    private String name;
    private String author;
    private String image;
    private String text;
    private String publisher;
    private int click_num;
    private int good_num;

    public Book(int id, String name, String author, String image, String text,
                String publisher, int good_num, int click_num) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.image = image;
        this.text = text;
        this.publisher = publisher;
        this.good_num = good_num;
        this.click_num = click_num;
    }

    public Book() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getClick_num() {
        return click_num;
    }

    public int getGood_num() {
        return good_num;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setGood_num(int good_num) {
        this.good_num = good_num;
    }
}

