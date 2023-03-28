package com.example.cloudnotetrack.Model;

public class Category {
    String cat_name;
    String id;

    public Category(String id,String cat_name  ) {
        this.cat_name = cat_name;
        this.id=id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getId() {
        return id;
    }
}
