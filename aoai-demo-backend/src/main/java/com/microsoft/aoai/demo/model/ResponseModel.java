package com.microsoft.aoai.demo.model;

import java.util.List;

public class ResponseModel {

    public ResponseModel(String type, List<String> tags) {
        this.type = type;
        this.tags = tags;
    }

    private String type;
    private List<String> tags;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}