package Warenwirtschaft.products.categories.phones;

import Warenwirtschaft.products.base.ProduktManager;
import Warenwirtschaft.products.user.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HandyManager extends ProduktManager<Handys> {

    @Override
    public List<Handys> loadAllByBrandId(int brandId) {
        List<Handys> handyList = new ArrayList<>();
        String sql = """
            SELECT h.model, h.brand_id, h.category_id, h.color, h.storage, h.price, h.stock, b.name AS brand_name
            FROM handys h
            JOIN brands b ON h.brand_id = b.id
            WHERE h.brand_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Handys handy = new Handys(
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getString("color"),
                        rs.getString("storage"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                handyList.add(handy);
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Handys aus der Datenbank.");
            e.printStackTrace();
        }

        return handyList;
    }

    public List<Handys> getModelVariants(String model, int brandId) {
        List<Handys> variants = new ArrayList<>();
        String sql = """
            SELECT h.*, b.name AS brand_name
            FROM handys h
            JOIN brands b ON h.brand_id = b.id
            WHERE h.model = ? AND h.brand_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, model);
            stmt.setInt(2, brandId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                variants.add(new Handys(
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getString("color"),
                        rs.getString("storage"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Abrufen der Varianten.");
            e.printStackTrace();
        }

        return variants;
    }

    @Override
    public List<Handys> loadAllFromDB(int brandId) {
        return loadAllByBrandId(brandId); // Einfacher Weiterleitung
    }

    @Override
    public Handys findByModel(String modelName, int brandId) {
        String sql = """
            SELECT h.*, b.name AS brand_name
            FROM handys h
            JOIN brands b ON h.brand_id = b.id
            WHERE h.model = ? AND h.brand_id = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, modelName);
            stmt.setInt(2, brandId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Handys(
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getString("color"),
                        rs.getString("storage"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Finden des Modells.");
            e.printStackTrace();
        }

        return null;
    }

    public void decreaseStock(Handys handy) {
        String sql = """
            UPDATE handys
            SET stock = stock - 1
            WHERE brand_id = ? AND model = ? AND color = ? AND storage = ? AND stock > 0
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, handy.getBrandId());
            stmt.setString(2, handy.getModel());
            stmt.setString(3, handy.getColor());
            stmt.setString(4, handy.getStorage());

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                System.out.println("✅ Bestand erfolgreich reduziert.");
            } else {
                System.out.println("❌ Produkt nicht verfügbar oder ausverkauft.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Aktualisieren des Lagerbestands.");
            e.printStackTrace();
        }
    }
}
