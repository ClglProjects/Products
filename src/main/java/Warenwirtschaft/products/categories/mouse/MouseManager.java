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
        String selectSql = """
        SELECT mv.id
        FROM mouse_variants mv
        JOIN mouses m ON mv.mouse_id = m.id
        WHERE m.model = ? AND m.brand_id = ?
          AND mv.color = ? AND mv.connection_type = ? AND mv.stock > 0
        LIMIT 1
    """;

        String updateSql = """
        UPDATE mouse_variants
        SET stock = stock - 1
        WHERE id = ?
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setString(1, mouse.getModel());
            selectStmt.setInt(2, mouse.getBrandId());
            selectStmt.setString(3, mouse.getColor());
            selectStmt.setString(4, mouse.getConnectionType());

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int variantId = rs.getInt("id");

                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, variantId);
                    int updated = updateStmt.executeUpdate();

                    if (updated > 0) {
                        System.out.println("✅ Maus-Bestand erfolgreich reduziert.");
                    } else {
                        System.out.println("❌ Maus nicht gefunden oder ausverkauft.");
                    }
                }

            } else {
                System.out.println("❌ Keine passende Variante mit Bestand gefunden.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Aktualisieren des Maus-Bestands.");
            e.printStackTrace();
        }
    }




    public List<Mouse> getModelVariants(String model, int brandId) {
        List<Mouse> variants = new ArrayList<>();

        String sql = """
        SELECT mv.color, mv.connection_type, mv.rgb, mv.dpi, mv.buttons, mv.battery_life,
               mv.price, mv.stock, mv.brandname,
               m.model, m.brand_id, m.category_id
        FROM mouse_variants mv
        JOIN mouses m ON mv.mouse_id = m.id
        WHERE m.model = ? AND m.brand_id = ?
       ORDER BY mv.color, mv.connection_type, mv.rgb;                                  
        
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, model);
            stmt.setInt(2, brandId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Mouse mouse = new Mouse(
                        rs.getString("brandname"),
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
