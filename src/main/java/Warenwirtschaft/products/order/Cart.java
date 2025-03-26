package Warenwirtschaft.products.order;

import Warenwirtschaft.products.model.VariantProdukt;
import Warenwirtschaft.products.user.DatabaseConnection;
import Warenwirtschaft.products.user.User;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final Map<VariantProdukt, Integer> cartItems = new HashMap<>();

    public void addToCart(VariantProdukt produkt) {
        cartItems.put(produkt, cartItems.getOrDefault(produkt, 0) + 1);
    }

    public void removeFromCart(VariantProdukt produkt) {
        if (cartItems.containsKey(produkt)) {
            cartItems.remove(produkt);
            System.out.println("🗑️ Produkt wurde aus dem Warenkorb entfernt.");
        } else {
            System.out.println("❌ Produkt ist nicht im Warenkorb.");
        }
    }

    public void updateQuantity(VariantProdukt produkt, int newQuantity) {
        if (cartItems.containsKey(produkt)) {
            if (newQuantity <= 0) {
                cartItems.remove(produkt);
                System.out.println("🚫 Produkt entfernt (Menge = 0).");
            } else {
                cartItems.put(produkt, newQuantity);
                System.out.println("🔁 Menge aktualisiert.");
            }
        } else {
            System.out.println("❌ Produkt ist nicht im Warenkorb.");
        }
    }

    public void showCart() {
        if (cartItems.isEmpty()) {
            System.out.println("🛒 Dein Warenkorb ist leer.");
            return;
        }

        System.out.println("\n🛒 Dein Warenkorb:");
        double gesamtPreis = 0;
        int position = 1;

        for (Map.Entry<VariantProdukt, Integer> entry : cartItems.entrySet()) {
            VariantProdukt produkt = entry.getKey();
            int menge = entry.getValue();
            double einzelpreis = produkt.getPrice();
            double gesamt = einzelpreis * menge;
            gesamtPreis += gesamt;

            System.out.printf("%d. %s\n", position++, produkt.getDisplayInfo());
            System.out.printf("   Menge: %d | Einzelpreis: %.2f€ | Gesamt: %.2f€\n\n", menge, einzelpreis, gesamt);
        }

        System.out.printf("💰 Gesamtpreis: %.2f€\n", gesamtPreis);
    }

    public void clearCart() {
        cartItems.clear();
        System.out.println("🧹 Dein Warenkorb wurde geleert.");
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public int getTotalItems() {
        return cartItems.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double getTotalPrice() {
        return cartItems.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    public Map<VariantProdukt, Integer> getCartItems() {
        return cartItems;
    }

    public void printCartSummary() {
        System.out.printf("🧾 Artikel im Warenkorb: %d | Gesamtpreis: %.2f€\n",
                getTotalItems(), getTotalPrice());
    }

    public void checkout(User user) {
        if (cartItems.isEmpty()) {
            System.out.println("🛒 Dein Warenkorb ist leer.");
            return;
        }

        double totalPrice = getTotalPrice();

        String insertOrderSQL = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
        String insertItemSQL = "INSERT INTO order_items (order_id, product_name, category, color, storage, quantity, unit_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, user.getId());
                orderStmt.setDouble(2, totalPrice);
                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {
                        for (Map.Entry<VariantProdukt, Integer> entry : cartItems.entrySet()) {
                            VariantProdukt produkt = entry.getKey();
                            int menge = entry.getValue();

                            itemStmt.setInt(1, orderId);
                            itemStmt.setString(2, produkt.getModel());
                            itemStmt.setString(3, produkt.getCategory());
                            itemStmt.setString(4, produkt.getColor());
                            itemStmt.setString(5, produkt.getStorage());
                            itemStmt.setInt(6, menge);
                            itemStmt.setDouble(7, produkt.getPrice());

                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }

                    conn.commit();
                    System.out.println("✅ Bestellung erfolgreich aufgegeben!");
                    cartItems.clear();
                } else {
                    conn.rollback();
                    System.out.println("❌ Fehler beim Generieren der Bestellnummer.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Fehler beim Checkout.");
        }
    }
}
