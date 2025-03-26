package Warenwirtschaft.products.ui.customer;

import Warenwirtschaft.products.user.DatabaseConnection;
import Warenwirtschaft.products.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class OrderManager {
    private static final Scanner scanner = new Scanner(System.in);

    // Admin-Sicht
    public static void show() {
        System.out.println("\n📦 Bestellübersicht:");
        displayAllOrders();
    }

    private static void displayAllOrders() {
        String sql = """
            SELECT o.id AS order_id, u.username, oi.product_name, oi.quantity,
                   o.total_price, o.order_date
            FROM orders o
            JOIN users u ON o.user_id = u.id
            JOIN order_items oi ON oi.order_id = o.id
            ORDER BY o.order_date DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int currentOrderId = -1;

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String username = rs.getString("username");
                String product = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double total = rs.getDouble("total_price");
                String date = rs.getString("order_date");

                if (orderId != currentOrderId) {
                    System.out.println("\n🧾 Bestellung ID: " + orderId +
                            " | Benutzer: " + username +
                            " | Gesamtpreis: " + total + "€" +
                            " | Datum: " + date);
                    currentOrderId = orderId;
                }

                System.out.printf("   ➤ %s x%d\n", product, quantity);
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Bestellungen.");
            e.printStackTrace();
        }
    }

    // Benutzer-Sicht
    public static void showUserOrders(User user) {
        String orderSql = "SELECT * FROM orders WHERE user_id = ?";
        String itemsSql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {

            orderStmt.setInt(1, user.getId());
            ResultSet orders = orderStmt.executeQuery();

            if (!orders.isBeforeFirst()) {
                System.out.println("📭 Du hast noch keine Bestellungen.");
                return;
            }

            while (orders.next()) {
                int orderId = orders.getInt("id");
                double totalPrice = orders.getDouble("total_price");
                String date = orders.getString("order_date");

                System.out.println("\n🧾 Bestellung #" + orderId +
                        " | Gesamt: " + totalPrice + "€" +
                        " | Datum: " + date);

                try (PreparedStatement itemStmt = conn.prepareStatement(itemsSql)) {
                    itemStmt.setInt(1, orderId);
                    ResultSet items = itemStmt.executeQuery();

                    while (items.next()) {
                        String productName = items.getString("product_name");
                        String color = items.getString("color");
                        String storage = items.getString("storage");
                        int quantity = items.getInt("quantity");
                        double price = items.getDouble("unit_price");

                        System.out.printf("   ➤ %s (%s, %s) x%d | Einzelpreis: %.2f€\n",
                                productName, color, storage, quantity, price);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden deiner Bestellungen.");
            e.printStackTrace();
        }
    }
}
