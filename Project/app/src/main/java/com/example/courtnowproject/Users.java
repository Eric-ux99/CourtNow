package com.example.courtnowproject;

public class Users {

    public String username, age, email, userID;

    public Users(){

    }

    public Users(String username, String age, String email, String userID){
        this.username = username;
        this.age = age;
        this.email = email;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
