package Warenwirtschaft.products.user;

import Warenwirtschaft.products.order.Cart;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Cart cart;
    private boolean isAdmin;

    // Konstruktor mit allen Feldern
    public User(int id, String username, String password, String email, String phone, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.cart = new Cart();
        this.isAdmin = isAdmin;
    }

    // Konstruktor ohne ID, setze Default-Werte für ID und Admin
    public User(String username, String password, String email, String phone) {
        this(0, username, password, email, phone, false); // ID auf 0 und Admin auf false setzen
    }

    // Leerer Konstruktor
    public User() {
        this.id = 0; // Standardwert für id
        this.username = ""; // Standardwert für den Benutzernamen
        this.password = ""; // Standardwert für das Passwort
        this.email = ""; // Standardwert für die E-Mail
        this.phone = ""; // Standardwert für die Telefonnummer
        this.cart = new Cart(); // Standardwert für den Warenkorb
        this.isAdmin = false; // Standardwert für Admin-Rechte
    }

    // Getter und Setter
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
