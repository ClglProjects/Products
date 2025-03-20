package org.example.User;

import org.example.HandyMarken.Warenkorb;

public class User {
    private int id;                // DB-ID (optional, kann man auch weglassen)
    private String username;
    private String password;
    private String email;
    private String phone;
    private Warenkorb cart;

    // Konstruktor für das vollständige User-Objekt (z.B. nach DB-Login)
    public User(int id, String username, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.cart = new Warenkorb();
    }

    // Falls du einen einfacheren Konstruktor brauchst (ohne ID), z.B. bei Registrierung
    public User(String username, String password, String email, String phone) {
        this(0, username, password, email, phone);
    }

    // Getter
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public Warenkorb getCart() {
        return cart;
    }
}
