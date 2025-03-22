package Warenwirtschaft.products.brands;

import Warenwirtschaft.products.user.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class brands {

    public static String getMarkenName(int brandId) {
        String sql = "SELECT name FROM brands WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Abrufen des Markennamens für ID " + brandId);
            e.printStackTrace();
        }

        return "UNBEKANNT";
    }

    // Optional: Andere Methoden wie getBrandIdByName(...) kannst du auch hier definieren
}
