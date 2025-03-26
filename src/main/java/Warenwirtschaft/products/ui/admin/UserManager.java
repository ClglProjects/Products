
// UserManager.java
package Warenwirtschaft.products.ui.admin;

import Warenwirtschaft.products.user.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManager {
    public static void show() {
        System.out.println("\n👥 Benutzerverwaltung:");
        System.out.println("📋 Alle registrierten Benutzer:");

        String sql = "SELECT id, username, email, phone FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("👤 ID: " + rs.getInt("id") +
                        " | Benutzername: " + rs.getString("username") +
                        " | 📧 E-Mail: " + rs.getString("email") +
                        " | 📞 Telefon: " + rs.getString("phone"));
            }
        } catch (Exception e) {
            System.out.println("❌ Fehler beim Abrufen der Benutzerliste.");
            e.printStackTrace();
        }
    }
}
