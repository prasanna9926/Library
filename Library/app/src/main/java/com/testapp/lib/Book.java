package com.testapp.lib;


public class Book {

    private String  id;
    private String title;
    private String author;
    private String description;


    public Book(String id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;

    }

    public Book(String title, String author, String description) {

        this.title = title;
        this.author = author;
        this.description = description;

    }

    public Book(String title, String author) {

        this.title = title;
        this.author = author;

    }

    public String  getId() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}

