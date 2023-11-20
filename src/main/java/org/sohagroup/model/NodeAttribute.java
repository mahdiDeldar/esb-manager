package org.sohagroup.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NodeAttribute {
    private int id;
    private String key;
    private String type;
    private String Category;
    private Boolean optional;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }
}
