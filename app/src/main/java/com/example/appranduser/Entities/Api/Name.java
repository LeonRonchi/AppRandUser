package com.example.appranduser.Entities.Api;

public class Name {
    public String title;
    public String first;
    public String last;

    public String getNomeCompleto()
    {
        return title + ". " + first + " " + last;
    }
}
