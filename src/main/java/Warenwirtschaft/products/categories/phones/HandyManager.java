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
            SELECT h.model, h.brand_id, h.category_id, 
                   hv.color, hv.storage, hv.price, hv.stock, 
                   b.name AS brand_name
            FROM handys h
            JOIN handy_variants hv ON h.id = hv.handy_id
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
            SELECT 
                hv.color, hv.storage, hv.price, hv.stock,
                h.model, h.brand_id, h.category_id,
                b.name AS brand_name
            FROM handy_variants hv
            JOIN handys h ON hv.handy_id = h.id
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
        return loadAllByBrandId(brandId);
    }

    @Override
    public Handys findByModel(String modelName, int brandId) {
        String sql = """
            SELECT 
                h.model, h.brand_id, h.category_id,
                hv.color, hv.storage, hv.price, hv.stock,
                b.name AS brand_name
            FROM handys h
            JOIN handy_variants hv ON h.id = hv.handy_id
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
        String selectSql = """
            SELECT hv.id
            FROM handy_variants hv
            JOIN handys h ON hv.handy_id = h.id
            WHERE h.model = ? AND h.brand_id = ? AND hv.color = ? AND hv.storage = ? AND hv.stock > 0
            LIMIT 1
        """;

        String updateSql = """
            UPDATE handy_variants
            SET stock = stock - 1
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setString(1, handy.getModel());
            selectStmt.setInt(2, handy.getBrandId());
            selectStmt.setString(3, handy.getColor());
            selectStmt.setString(4, handy.getStorage());

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int variantId = rs.getInt("id");

                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, variantId);
                    int updated = updateStmt.executeUpdate();

                    if (updated > 0) {
                        System.out.println("✅ Handy-Bestand erfolgreich reduziert.");
                    } else {
                        System.out.println("❌ Update fehlgeschlagen.");
                    }
                }

            } else {
                System.out.println("❌ Keine passende Variante mit Lagerbestand gefunden.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Reduzieren des Lagerbestands.");
            e.printStackTrace();
        }
    }
}
