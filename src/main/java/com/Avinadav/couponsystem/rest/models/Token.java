package com.Avinadav.couponsystem.rest.models;

public class Token {
    private String token;

    public Token(String tokenName) {
        this.token = tokenName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
