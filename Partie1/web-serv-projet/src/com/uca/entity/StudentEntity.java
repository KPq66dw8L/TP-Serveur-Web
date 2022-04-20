package com.uca.entity;

import java.util.ArrayList;

public class StudentEntity extends UserEntity {
    public static ArrayList<GivenGommettes> everyGommettes = new ArrayList<>();

    private int nb_white;
    private int nb_green;
    private int nb_red;

    public StudentEntity(){
        this.nb_white = 0;
        this.nb_green = 0;
        this.nb_red = 0;
    }

    private String group;
    public ArrayList<GivenGommettes> gommettes = new ArrayList<>();

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void addGommete(GivenGommettes gom){

        if (gom.getGommette().getColour().equals("white")){
//            System.out.println("white added in entity 1");
            this.nb_white++;
//            System.out.println("white added in entity 2");
            //try-catch because gommettes was not initialized...
            try {
                gommettes.add(gom);
            } catch (Exception e){
                System.out.println(e);
            }
//            System.out.println("white added in entity 3");
        } else if (gom.getGommette().getColour().equals("green")){
            this.nb_green++;
            gommettes.add(gom);
        } else if (gom.getGommette().getColour().equals("red")){
            this.nb_red++;
            gommettes.add(gom);
        } else {
            System.out.println("Gommette colour invalid.");
        }

//        System.out.println("gommette added in entity");
    }

    public String getNb_white() {
        return Integer.toString(nb_white);
    }

    public void setNb_white(int nb_white) {
        System.out.println("Boonnnnn: " + nb_white);
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
