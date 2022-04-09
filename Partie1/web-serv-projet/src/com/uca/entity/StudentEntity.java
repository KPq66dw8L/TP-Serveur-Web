package com.uca.entity;

import java.util.ArrayList;

public class StudentEntity extends UserEntity {
    public static ArrayList<GivenGommettes> everyGommettes;
//    private int id;
//    private String firstname;
//    private String lastname;
    private int nb_white;
    private int nb_green;
    private int nb_red;

    public StudentEntity(){
        this.nb_white = 0;
        this.nb_green = 0;
        this.nb_red = 0;
    }

    private String group;
    public ArrayList<GivenGommettes> gommettes;

//    public ArrayList<GivenGommettes> getGommettes() {
//        return gommettes;
//    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void addGommete(GivenGommettes gom){
        if (gom.getGommette().getColour() == "white"){
            this.nb_white++;
        } else if (gom.getGommette().getColour() == "green"){
            this.nb_green++;
        } else {
            this.nb_red++;
        }
        gommettes.add(gom);
    }

    public String getNb_white() {
        return Integer.toString(nb_white);
    }

    public void setNb_white(int nb_white) {
        this.nb_white = nb_white;
    }

    public String getNb_green() {
        return Integer.toString(nb_green);
    }

    public void setNb_green(int nb_green) {
        this.nb_green = nb_green;
    }

    public String getNb_red() {
        return Integer.toString(nb_red);
    }

    public void setNb_red(int nb_red) {
        this.nb_red = nb_red;
    }
}
