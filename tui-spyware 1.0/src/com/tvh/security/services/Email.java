package com.tvh.security.services;

public class Email {
    private String type;
    private String email;
    
    public Email() {
    }
    
    public Email(String type, String email) {
        this.type = type;
        this.email = email;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
}
