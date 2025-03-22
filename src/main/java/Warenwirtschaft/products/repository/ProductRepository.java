 package Warenwirtschaft.products.repository;

import Warenwirtschaft.products.user.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public List<String> fetchAvailableBrands() {
        List<String> brands = new ArrayList<>();
        String sql = """
        SELECT DISTINCT b.name AS brand
        FROM all_products p
        JOIN brands b ON p.brand_id = b.id
        ORDER BY b.name ASC
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String brand = rs.getString("brand").toUpperCase();
                if (!brands.contains(brand)) {
                    brands.add(brand);
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Fehler beim Abrufen der Marken.");
            e.printStackTrace();
        }

        return brands;
    }


    public List<String> fetchCategoriesForBrand(String brand) {
        List<String> categories = new ArrayList<>();
        String sql = """
        SELECT DISTINCT c.name AS category
        FROM all_products p
        JOIN brands b ON p.brand_id = b.id
        JOIN categories c ON p.category_id = c.id
        WHERE UPPER(b.name) = ?
        ORDER BY c.name ASC
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, brand.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (Exception e) {
            System.out.println("❌ Fehler beim Abrufen der Kategorien für Marke: " + brand);
            e.printStackTrace();
        }

        return categories;
    }


    public List<String> fetchProductsForCategory(String brand, String category) {
        List<String> products = new ArrayList<>();
        String sql = """
        SELECT DISTINCT p.model
        FROM all_products p
        JOIN brands b ON p.brand_id = b.id
        JOIN categories c ON p.category_id = c.id
        WHERE UPPER(b.name) = ? AND UPPER(c.name) = ?
        ORDER BY p.model ASC
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, brand.toUpperCase());
            stmt.setString(2, category.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(rs.getString("model"));
            }

        } catch (Exception e) {
            System.out.println("❌ Fehler beim Abrufen der Produkte für Kategorie '" + category + "' und Marke '" + brand + "'");
            e.printStackTrace();
        }

        return products;
    }


    public static int getBrandIdByName(String brandName) {
        String sql = "SELECT id FROM brands WHERE UPPER(name) = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, brandName.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Suchen der Brand-ID für: " + brandName);
            e.printStackTrace();
        }
        return -1;
    }








}



















