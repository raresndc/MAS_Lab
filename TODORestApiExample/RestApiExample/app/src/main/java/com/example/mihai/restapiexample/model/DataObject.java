package com.example.mihai.restapiexample.model;

public class DataObject {
    public final String title;
    public final String genre;
    public final String release;
    public final String duration;
    public final boolean boxoffice;
    public final String poster;

    public DataObject(String title, String genre, String release, String duration, boolean boxoffice, String poster) {
        this.title = title;
        this.genre = genre;
        this.release = release;
        this.duration = duration;
        this.boxoffice = boxoffice;
        this.poster = poster;
    }
}
