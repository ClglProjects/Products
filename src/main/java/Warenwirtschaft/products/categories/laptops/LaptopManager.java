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
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getString("processor"),
                        rs.getDouble("screen_size"),
                        rs.getDouble("battery_life"),
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
        String sql = """
            UPDATE laptops
            SET stock = stock - 1
            WHERE brand_id = ? AND model = ? AND processor = ? AND ram = ? AND color = ? AND storage = ? AND stock > 0
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, laptop.getBrandId());
            stmt.setString(2, laptop.getModel());
            stmt.setString(3, laptop.getProcessor());
            stmt.setInt(4, laptop.getRam());
            stmt.setString(5, laptop.getColor());
            stmt.setString(6, laptop.getStorage());

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                System.out.println("✅ Bestand erfolgreich reduziert.");
            } else {
                System.out.println("❌ Produkt nicht gefunden oder ausverkauft.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Aktualisieren des Bestands.");
            e.printStackTrace();
        }
    }

    public List<Laptops> getModelVariants(String model, int brandId) {
        List<Laptops> variants = new ArrayList<>();
        String sql = """
        SELECT * FROM laptops
        WHERE model = ? AND brand_id = ?
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, model);
            stmt.setInt(2, brandId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Laptops laptop = new Laptops(
                        rs.getString("brand_name"),
                        rs.getString("model"),
                        rs.getInt("brand_id"),
                        rs.getInt("category_id"),
                        rs.getString("processor"),
                        rs.getDouble("screen_size"),
                        rs.getDouble("battery_life"),
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
