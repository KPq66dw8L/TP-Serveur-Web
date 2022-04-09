package com.uca.entity;

import java.util.ArrayList;

public class StudentEntity extends UserEntity {
    private static ArrayList<GivenGommettes> everyGommettes;
//    private int id;
//    private String firstname;
//    private String lastname;
    private String group;
    ArrayList<GivenGommettes> gommettes;

    public ArrayList<GivenGommettes> getGommettes() {
        return gommettes;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
