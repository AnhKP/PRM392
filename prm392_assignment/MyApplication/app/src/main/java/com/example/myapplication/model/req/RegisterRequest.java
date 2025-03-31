package com.example.myapplication.model.req;

public class RegisterRequest {
    private String email ;
    private String password ;
    private String phoneNumber ;
    private String fullName ;

    public RegisterRequest(String email,String password, String phoneNumber, String fullName) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
    }
}
