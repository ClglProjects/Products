package org.example.User;


import org.example.HandyMarken.Warenkorb;

public class User {
    private String username;
    private String password;
    private Warenkorb cart;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.cart = new Warenkorb();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Warenkorb getCart() {
        return cart;
    }
}
