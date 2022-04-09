package com.uca.entity;

import java.util.ArrayList;

public class Gommette {
    private int id;
    private String colour;
    private String description;

    public static ArrayList<Gommette> gommettes; //tmp?

    public int getId() {
        return id;
    }

    public String getColour() {
        return colour;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
