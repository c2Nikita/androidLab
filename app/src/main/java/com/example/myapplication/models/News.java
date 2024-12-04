package com.example.myapplication.models;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class News implements Serializable {


    private String title;
    private String url;
    private String source;


    public String getTitle() {
        return this.title;
    }
    public String getUrl() {
        return this.url;
    }
    public String getSource() {
        return this.source;
    }
}
