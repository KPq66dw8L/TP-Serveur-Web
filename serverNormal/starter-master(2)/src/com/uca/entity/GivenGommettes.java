package com.uca.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GivenGommettes {
    private int id;
    private int id_student;
    private int id_prof;
    private String date;
    private Gommette gommette;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_student() {
        return id_student;
    }

    public void setId_student(int id_student) {
        this.id_student = id_student;
    }

    public int getId_prof() {
        return id_prof;
    }

    public void setId_prof(int id_prof) {
        this.id_prof = id_prof;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.date = formatter.format(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Gommette getGommette() {
        return gommette;
    }

    public void setGommette(Gommette gommette) {
        this.gommette = gommette;
    }
}
