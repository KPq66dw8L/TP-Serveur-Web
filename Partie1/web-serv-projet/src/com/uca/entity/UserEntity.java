package com.uca.entity;

public class UserEntity {
    private String firstName;
    private String lastName;
    private int id;
    private String prof;
    private int gommette_blanche;
    private int gommette_verte;
    private int gommette_rouge;

    public UserEntity() {
        //Ignored !
        gommette_verte = 0;
        gommette_blanche = 0;
        gommette_rouge = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProf(){
        return this.prof;
    }

    public void setProf(String prof){
        this.prof = prof;
    }

    public int getBlanche() {
        return gommette_blanche;
    }

    public void setBlanche(int gommette_blanche) {
        this.gommette_blanche = gommette_blanche;
    }

    public int getRouge() {
        return gommette_rouge;
    }

    public void setRouge(int gommette_rouge) {
        this.gommette_rouge = gommette_rouge;
    }

    public int getVerte() {
        return gommette_verte;
    }

    public void setVerte(int verte) {
        this.gommette_verte = verte;
    }
}
