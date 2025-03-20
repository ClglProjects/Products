package org.example.HandyMarken;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.example.User.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Samsung extends Handys implements HandyInterface {

    private static final Scanner scanner = new Scanner(System.in);

    public Samsung(String name, String model, String brand, String color, String storage, double price, int stock) {
        super(name, model, brand, color, storage, price, stock);
    }

    public static ArrayList<Samsung> loadAllSamsungFromDB() {
        ArrayList<Samsung> samsungList = new ArrayList<>();

        String sql = "SELECT name, model, brand, color, storage, price, stock FROM samsung";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                samsungList.add(new Samsung(
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
            throw new RuntimeException("Fehler beim Laden der Samsung-Geräte aus der DB.");
        }

        return samsungList;
    }

    public static void showAllSamsung() {
        ArrayList<Samsung> samsungList = loadAllSamsungFromDB();

        System.out.println("\n📌 Verfügbare Samsung-Geräte:");
        if (samsungList.isEmpty()) {
            System.out.println("❌ Keine Samsung-Geräte verfügbar.");
            return;
        }

        for (Samsung samsung : samsungList) {
            String status = (samsung.getStock() > 0) ? "✅ Verfügbar: " + samsung.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + samsung.getName() + " " + samsung.getModel() + " (" + samsung.getColor() + ", " + samsung.getStorage() + ") | 💰 " + samsung.getPrice() + "€ | " + status);
        }
    }

    public void decreaseStockInDB() {
        String sql = "UPDATE samsung SET stock = stock - 1 WHERE name = ? AND model = ? AND color = ? AND storage = ? AND stock > 0";

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

    public static Samsung findSamsungByModel(String userInput) {
        ArrayList<Samsung> samsungList = loadAllSamsungFromDB();

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<Samsung> besteVorschläge = new ArrayList<>();

        for (Samsung samsung : samsungList) {
            String fullName = (samsung.getName() + samsung.getModel()).replaceAll("\\s+", "").toLowerCase();

            if (fullName.equals(userInput)) {
                return samsung;
            }

            int distance = levenshtein.apply(fullName, userInput);
            if (distance < minDistance) {
                minDistance = distance;
                besteVorschläge.clear();
                besteVorschläge.add(samsung);
            } else if (distance == minDistance) {
                besteVorschläge.add(samsung);
            }
        }

        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (Samsung vorschlag : besteVorschläge) {
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
        showAllSamsung();
    }

    @Override
    public Handys findDeviceByModel(String userInput) {
        return findSamsungByModel(userInput);
    }
}