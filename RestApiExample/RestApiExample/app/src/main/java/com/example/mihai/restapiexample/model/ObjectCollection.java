package com.example.mihai.restapiexample.model;

public class ObjectCollection {

    public final DataObject[] items;

    public ObjectCollection(DataObject[] dataObjects) {
        this.items = dataObjects;
    }
}
