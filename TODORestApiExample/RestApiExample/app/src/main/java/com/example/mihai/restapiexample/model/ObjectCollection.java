package com.example.mihai.restapiexample.model;

import java.util.List;

public class ObjectCollection {
    public final List<DataObject> items;

    public ObjectCollection(List<DataObject> items) {
        this.items = items;
    }
}
