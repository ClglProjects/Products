package Warenwirtschaft.produkte.User;

import Warenwirtschaft.produkte.Shop.Cart;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Cart cart;
    private boolean isAdmin;

    public User(int id, String username, String password, String email, String phone,boolean isAdmin ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.cart = new Cart();
        this.isAdmin = isAdmin;
    }

    public User(String username, String password, String email, String phone) {
        this(0, username, password, email, phone, false);
    }
    public boolean isAdmin() {
        return isAdmin;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }
}
