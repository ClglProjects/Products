package org.example.HandyMarken;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.example.User.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GooglePixel extends Handys implements HandyInterface {

    private static final Scanner scanner = new Scanner(System.in);

    public GooglePixel(String name, String model, String brand, String color, String storage, double price, int stock) {
        super(name, model, brand, color, storage, price, stock);
    }

    public static ArrayList<GooglePixel> loadAllGooglePixelsFromDB() {
        ArrayList<GooglePixel> pixelList = new ArrayList<>();

        String sql = "SELECT name, model, brand, color, storage, price, stock FROM google_pixel";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pixelList.add(new GooglePixel(
                        rs.getString("name"),
                        rs.getString("model"),
                        rs.getString("brand"),
                        rs.getString("color"),
                        rs.getString("storage"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Laden der Google Pixel-Geräte aus der DB.");
        }

        return pixelList;
    }

    public static void showAllGooglePixels() {
        ArrayList<GooglePixel> pixelList = loadAllGooglePixelsFromDB();

        System.out.println("\n📌 Verfügbare Google Pixel-Geräte:");
        if (pixelList.isEmpty()) {
            System.out.println("❌ Keine Google Pixel-Geräte verfügbar.");
            return;
        }

        for (GooglePixel pixel : pixelList) {
            String status = (pixel.getStock() > 0) ? "✅ Verfügbar: " + pixel.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + pixel.getName() + " " + pixel.getModel() + " (" + pixel.getColor() + ", " + pixel.getStorage() + ") | 💰 " + pixel.getPrice() + "€ | " + status);
        }
    }

    public void decreaseStockInDB() {
        String sql = "UPDATE google_pixel SET stock = stock - 1 WHERE name = ? AND model = ? AND color = ? AND storage = ? AND stock > 0";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, getName());
            stmt.setString(2, getModel());
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

    public static GooglePixel findGooglePixelByModel(String userInput) {
        ArrayList<GooglePixel> pixelList = loadAllGooglePixelsFromDB();

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<GooglePixel> besteVorschläge = new ArrayList<>();

        for (GooglePixel pixel : pixelList) {
            String fullName = (pixel.getName() + pixel.getModel()).replaceAll("\\s+", "").toLowerCase();

            if (fullName.equals(userInput)) {
                return pixel;
            }

            int distance = levenshtein.apply(fullName, userInput);
            if (distance < minDistance) {
                minDistance = distance;
                besteVorschläge.clear();
                besteVorschläge.add(pixel);
            } else if (distance == minDistance) {
                besteVorschläge.add(pixel);
            }
        }

        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (GooglePixel vorschlag : besteVorschläge) {
                System.out.println("   ➜ " + vorschlag.getName() + " " + vorschlag.getModel() + " (" + vorschlag.getColor() + ", " + vorschlag.getStorage() + ")");
            }

            System.out.print("👉 Möchten Sie dieses Modell wählen? (Ja/Nein): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.matches("^(ja|j|yes|y|si|oui|aye|jo|yep|yup)$")) {
                return besteVorschläge.get(0);
            } else {
                System.out.println("❌ Okay, versuche es erneut mit einer neuen Eingabe.");
                return null;
            }
        }
        return null;
    }

    @Override
    public void showAllDevices() {
        showAllGooglePixels();
    }

    @Override
    public Handys findDeviceByModel(String userInput) {
        return findGooglePixelByModel(userInput);
    }
}