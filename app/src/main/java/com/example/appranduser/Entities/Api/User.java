package com.example.appranduser.Entities.Api;

public class User {
    public String gender;
    public Name name;
    public Location location;
    public String email;
    public Login login;
    public Dob dob;
    public Registered registered;
    public String phone;
    public String cell;
    public Id id;
    public Picture picture;
    public String nat;

    public String getEmail(){
        return email;
    }
}
