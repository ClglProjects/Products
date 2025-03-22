package Warenwirtschaft.products.categories.mouse;

import Warenwirtschaft.products.base.ProduktManager;
import Warenwirtschaft.products.user.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MouseManager extends ProduktManager<Mouse> {

    /**
     * Lädt alle Mäuse einer bestimmten Marke aus der DB.
     */
    @Override
    public List<Mouse> loadAllByBrandId(int brandId) {
        List<Mouse> mouseList = new ArrayList<>();
        String sql = """
            SELECT m.*, b.name AS brand_name
            FROM mouses m
            JOIN brands b ON m.brand_id = b.id
            WHERE m.brand_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Mouse mouse = new Mouse(
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getInt("dpi"),
                        rs.getInt("buttons"),
                        rs.getString("color"),
                        rs.getBoolean("rgb"),
                        rs.getString("connection_type"),
                        rs.getString("battery_life"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                mouseList.add(mouse);
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Mäuse.");
            e.printStackTrace();
        }

        return mouseList;
    }

    @Override
    public List<Mouse> loadAllFromDB(int brandId) {
        return loadAllByBrandId(brandId);
    }

    @Override
    public Mouse findByModel(String modelName, int brandId) {
        String sql = """
            SELECT m.*, b.name AS brand_name
            FROM mouses m
            JOIN brands b ON m.brand_id = b.id
            WHERE m.model = ? AND m.brand_id = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, modelName);
            stmt.setInt(2, brandId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Mouse(
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getInt("dpi"),
                        rs.getInt("buttons"),
                        rs.getString("color"),
                        rs.getBoolean("rgb"),
                        rs.getString("connection_type"),
                        rs.getString("battery_life"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Finden der Maus.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Verringert den Lagerbestand einer Maus um 1.
     */
    public void decreaseStock(Mouse mouse) {
        String sql = """
            UPDATE mouses
            SET stock = stock - 1
            WHERE brand_id = ? AND model = ? AND color = ? AND connection_type = ? AND stock > 0
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mouse.getBrandId());
            stmt.setString(2, mouse.getModel());
            stmt.setString(3, mouse.getColor());
            stmt.setString(4, mouse.getConnectionType());

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                System.out.println("✅ Bestand aktualisiert.");
            } else {
                System.out.println("❌ Produkt nicht gefunden oder ausverkauft.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Aktualisieren des Lagerbestands.");
            e.printStackTrace();
        }
    }
    public List<Mouse> getModelVariants(String model, int brandId) {
        List<Mouse> variants = new ArrayList<>();
        String sql = """
        SELECT * FROM mouses
        WHERE model = ? AND brand_id = ?
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, model);
            stmt.setInt(2, brandId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Mouse mouse = new Mouse(
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getInt("dpi"),
                        rs.getInt("buttons"),
                        rs.getString("color"),
                        rs.getBoolean("rgb"),
                        rs.getString("connection_type"),
                        rs.getString("battery_life"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                variants.add(mouse);
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Maus-Varianten.");
            e.printStackTrace();
        }

        return variants;
    }








}
