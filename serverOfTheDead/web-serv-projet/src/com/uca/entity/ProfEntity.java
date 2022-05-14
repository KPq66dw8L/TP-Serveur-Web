package com.uca.entity;

public class ProfEntity extends UserEntity{
    private String username;
    private String salt;
    private String hashedPassword;

    public String getUsername(){
        return this.username;
    }

    public String getSalt() {
        return this.salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
