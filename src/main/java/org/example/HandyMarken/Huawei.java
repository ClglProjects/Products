package org.example.HandyMarken;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.example.User.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Huawei extends Handys implements HandyInterface {

    private static final Scanner scanner = new Scanner(System.in);

    public Huawei(String name, String model, String brand, String color, String storage, double price, int stock) {
        super(name, model, brand, color, storage, price, stock);
    }

    public static ArrayList<Huawei> loadAllHuaweiFromDB() {
        ArrayList<Huawei> huaweiList = new ArrayList<>();

        String sql = "SELECT name, model, brand, color, storage, price, stock FROM huawei";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                huaweiList.add(new Huawei(
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
            throw new RuntimeException("Fehler beim Laden der Huawei-Geräte aus der DB.");
        }

        return huaweiList;
    }

    public static void showAllHuawei() {
        ArrayList<Huawei> huaweiList = loadAllHuaweiFromDB();

        System.out.println("\n📌 Verfügbare Huawei-Geräte:");
        if (huaweiList.isEmpty()) {
            System.out.println("❌ Keine Huawei-Geräte verfügbar.");
            return;
        }

        for (Huawei huawei : huaweiList) {
            String status = (huawei.getStock() > 0) ? "✅ Verfügbar: " + huawei.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + huawei.getName() + " " + huawei.getModel() +
                    " | 🏳️ " + huawei.getColor() +
                    " | 💾 " + huawei.getStorage() +
                    " | 💰 " + huawei.getPrice() + "€ | " + status);
        }
    }

    public void decreaseStockInDB() {
        if (getStock() <= 0) {
            System.out.println("❌ Nicht verfügbar! Huawei ist ausverkauft.");
            return;
        }

        String sql = "UPDATE huawei SET stock = stock - 1 WHERE name = ? AND model = ? AND color = ? AND storage = ? AND stock > 0";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, getName());
            stmt.setString(2, getModel());
            stmt.setString(3, getColor());
            stmt.setString(4, getStorage());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Kauf erfolgreich! Bestand wurde aktualisiert.");
            } else {
                System.out.println("❌ Nicht verfügbar oder Fehler beim Aktualisieren.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Aktualisieren des Bestandes.");
        }
    }

    public static Huawei findHuaweiByModel(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            System.out.println("❌ Ungültige Eingabe! Bitte Modellnamen eingeben.");
            return null;
        }

        ArrayList<Huawei> huaweiList = loadAllHuaweiFromDB();

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<Huawei> besteVorschläge = new ArrayList<>();

        for (Huawei huawei : huaweiList) {
            String fullName = (huawei.getName() + huawei.getModel()).replaceAll("\\s+", "").toLowerCase();

            if (fullName.equals(userInput)) {
                return huawei;
            }

            int distance = levenshtein.apply(fullName, userInput);
            if (distance < minDistance) {
                minDistance = distance;
                besteVorschläge.clear();
                besteVorschläge.add(huawei);
            } else if (distance == minDistance) {
                besteVorschläge.add(huawei);
            }
        }

        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (Huawei vorschlag : besteVorschläge) {
                System.out.println("   ➜ " + vorschlag.getName() + " " + vorschlag.getModel());
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

        System.out.println("❌ Kein passendes Modell gefunden.");
        return null;
    }

    @Override
    public void showAllDevices() {
        showAllHuawei();
    }

    @Override
    public Handys findDeviceByModel(String userInput) {
        return findHuaweiByModel(userInput);
    }
}
