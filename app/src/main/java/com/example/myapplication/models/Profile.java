package com.example.myapplication.models;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile implements Serializable {

    private Long id;
    private String login;
    private String password;
    public String name;
    public String team;
}
