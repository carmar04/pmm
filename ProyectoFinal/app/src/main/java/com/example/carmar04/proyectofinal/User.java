package com.example.carmar04.proyectofinal;

import java.io.Serializable;

public class User implements Serializable {

    int UserId;
    String nickName;
    String mail;
    String password;

    public User(String nickName, String password){
        this.nickName = nickName;
        this.mail = mail;
        this.password = password;
    }

    public void setUserId(int UserId){
        this.UserId = UserId;
    }
    public int getUserId(){
        return this.UserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
