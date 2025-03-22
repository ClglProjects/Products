package Warenwirtschaft.produkte.Elektronik.Handys;

import Warenwirtschaft.produkte.Marken.Marken;
import org.apache.commons.text.similarity.LevenshteinDistance;
import Warenwirtschaft.produkte.User.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Handys extends Produkt {
    private String color;
    private String storage;

    public Handys(String brandName, String model, int brandId, int categoryId, String color, String storage, double price, int stock) {
        super(brandName, model, brandId, categoryId, price, stock);
        this.color = color;
        this.storage = storage;
    }

    public String getColor() { return color; }
    public String getStorage() { return storage; }

    @Override
    public void showDetails() {
        System.out.printf("📱 %s %s (%s, %s) | 💰 %.2f€ | Lagerbestand: %d%n",
                getBrandName(), getModel(), color, storage, getPrice(), getStock());
    }


    /**
     * **Lädt alle Handys einer bestimmten Marke aus der `handys`-Tabelle**
     */
    public static List<Handys> loadAllHandysFromDB(int brandId) {
        List<Handys> handyList = new ArrayList<>();
        String sql = """
        SELECT h.model, b.name AS brand_name, h.brand_id, h.category_id, 
               h.color, h.storage, h.price, h.stock
        FROM handys h
        JOIN brands b ON h.brand_id = b.id
        WHERE h.brand_id = ?
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                handyList.add(new Handys(
                        rs.getString("brand_name"),  // ✅ Richtiger `brandName` direkt aus DB
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
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Laden der Handys aus der Datenbank.");
        }
        return handyList;
    }


    /**
     * **Zeigt alle Handys einer bestimmten Marke**
     */
    public static void showAllHandys(int brandId) {
        List<Handys> handyList = loadAllHandysFromDB(brandId);

        System.out.println("\n📌 Verfügbare Geräte für Marke-ID " + brandId + ":");
        if (handyList.isEmpty()) {
            System.out.println("❌ Keine Geräte verfügbar.");
            return;
        }

        for (Handys handy : handyList) {
            String status = (handy.getStock() > 0) ? "✅ Verfügbar: " + handy.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + handy.getModel() + " (" + handy.getColor() + ", " + handy.getStorage() + ") | 💰 " + handy.getPrice() + "€ | " + status);
        }
    }

    /**
     * **Reduziert den Bestand eines Handys in der Datenbank**
     */
    public void decreaseStockInDB() {
        String sql = "UPDATE handys SET stock = stock - 1 WHERE model = ? AND brand_id = ? AND color = ? AND storage = ? AND stock > 0";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, getModel());
            stmt.setInt(2, getBrandId());
            stmt.setString(3, getColor());
            stmt.setString(4, getStorage());

            int updatedRows = stmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("✅ Kauf erfolgreich! Bestand wurde aktualisiert.");
            } else {
                System.out.println("❌ Nicht verfügbar oder Fehler beim Aktualisieren.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Aktualisieren des Bestandes.");
        }
    }

    /**
     * **Sucht ein Handy nach Modellname mit Tippfehler-Toleranz**
     */
    public static Handys findHandyByModel(String userInput, int brandId) {
        List<Handys> handyList = loadAllHandysFromDB(brandId);

        // Nutzer-Eingabe normalisieren (Leerzeichen & Sonderzeichen entfernen)
        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        Handys bestMatch = null;
        List<Handys> ähnlicheModelle = new ArrayList<>();

        for (Handys handy : handyList) {
            String fullName = (handy.getModel() + handy.getColor() + handy.getStorage())
                    .replaceAll("\\s+", "").toLowerCase(); // Modell + Farbe + Speicher vergleichen

            int distance = levenshtein.apply(fullName, userInput);

            if (fullName.equals(userInput)) {
                return handy; // Exaktes Modell gefunden
            }

            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = handy;
                ähnlicheModelle.clear();
                ähnlicheModelle.add(handy);
            } else if (distance == minDistance) {
                ähnlicheModelle.add(handy);
            }
        }

        if (!ähnlicheModelle.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (Handys handy : ähnlicheModelle) {
                System.out.println("   ➜ " + handy.getModel() + " (" + handy.getColor() + ", " + handy.getStorage() + ")");
            }
            return null;
        }
        return null;
    }

    public static List<Handys> getSuggestions(String userInput, int brandId) {
        List<Handys> handyList = loadAllHandysFromDB(brandId);
        List<Handys> besteVorschläge = new ArrayList<>();

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;

        for (Handys handy : handyList) {
            String fullName = handy.getModel().replaceAll("\\s+", "").toLowerCase();
            int distance = levenshtein.apply(fullName, userInput);

            if (distance < minDistance) {
                minDistance = distance;
                besteVorschläge.clear();
                besteVorschläge.add(handy);
            } else if (distance == minDistance) {
                besteVorschläge.add(handy);
            }
        }

        return besteVorschläge;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Handys handy = (Handys) obj;
        return getModel().equals(handy.getModel()) &&
                getBrandId() == handy.getBrandId() &&
                getColor().equals(handy.getColor()) &&
                getStorage().equals(handy.getStorage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModel(), getBrandId(), getColor(), getStorage());
    }
    public String getBrandName() {
        return Marken.getMarkenName(getBrandId());  // ✅ Zugriff über `getBrandId()`
    }



    public static List<Handys> getModelVariants(String model, int brandId) {
        List<Handys> variants = new ArrayList<>();
        String sql = "SELECT * FROM handys WHERE model = ? AND brand_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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
            e.printStackTrace();
            System.out.println("❌ Fehler beim Abrufen der Modellvarianten.");
        }

        return variants;
    }





}

