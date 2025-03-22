package Warenwirtschaft.products.categories.laptops;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Warenwirtschaft.products.user.DatabaseConnection;

public class LaptopManager {

    public List<Laptops> getAllLaptops() {
        List<Laptops> laptops = new ArrayList<>();

        String sql = "SELECT * FROM laptops";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Laptops laptop = new Laptops(
                        rs.getString("brandname"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getString("processor"),
                        rs.getDouble("screen_size"),
                        rs.getString("battery_life"),
                        rs.getInt("ram"),
                        rs.getString("storage"),
                        rs.getString("color"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                laptops.add(laptop);
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Laptops aus der Datenbank.");
            e.printStackTrace();
        }

        return laptops;
    }

    public void decreaseStock(Laptops laptop) {
        String selectSql = """
        SELECT lv.id
        FROM laptop_variants lv
        JOIN laptops l ON lv.laptop_id = l.id
        WHERE l.model = ? AND l.brand_id = ?
          AND lv.ram = ? AND lv.color = ? AND lv.storage = ? AND lv.stock > 0
        LIMIT 1
        """;

        String updateSql = """
        UPDATE laptop_variants
        SET stock = stock - 1
        WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Step 1: ID der Variante holen
            selectStmt.setString(1, laptop.getModel());
            selectStmt.setInt(2, laptop.getBrandId());
            selectStmt.setInt(3, laptop.getRam());
            selectStmt.setString(4, laptop.getColor());
            selectStmt.setString(5, laptop.getStorage());

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int variantId = rs.getInt("id");

                // Step 2: Bestandsupdate durchführen
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, variantId);
                    int updated = updateStmt.executeUpdate();

                    if (updated > 0) {
                        System.out.println("✅ Bestand erfolgreich reduziert.");
                    } else {
                        System.out.println("❌ Bestand konnte nicht aktualisiert werden.");
                    }
                }

            } else {
                System.out.println("❌ Keine passende Variante mit Lagerbestand gefunden.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Aktualisieren des Bestands.");
            e.printStackTrace();
        }
    }




    public List<Laptops> getModelVariants(String model, int brandId) {
        List<Laptops> variants = new ArrayList<>();

        String sql = """
        SELECT lv.ram, lv.storage, lv.color, lv.price, lv.stock, lv.brandname,
               l.model, l.brand_id, l.category_id, l.processor, l.screen_size, l.battery_life
        FROM laptop_variants lv
        JOIN laptops l ON lv.laptop_id = l.id
        WHERE l.model = ? AND l.brand_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, model);
            stmt.setInt(2, brandId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Laptops laptop = new Laptops(
                        rs.getString("brandname"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getString("processor"),
                        rs.getDouble("screen_size"),
                        rs.getString("battery_life"),
                        rs.getInt("ram"),
                        rs.getString("storage"),
                        rs.getString("color"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                variants.add(laptop);
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Laptop-Varianten.");
            e.printStackTrace();
        }

        return variants;
    }






}
