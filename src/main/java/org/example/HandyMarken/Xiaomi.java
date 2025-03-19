package org.example.HandyMarken;

import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class Xiaomi extends Handys implements HandyInterface {
    private static ArrayList<Xiaomi> xiaomis = new ArrayList<>();

    public Xiaomi(String name, String model, String brand, double price, int stock) {
        super(name, model, brand, price, stock);
    }

    static {
        xiaomis.add(new Xiaomi("Xiaomi", "Mi 10", "Xiaomi", 349.99, 15));         // 2020
        xiaomis.add(new Xiaomi("Xiaomi", "Mi 11", "Xiaomi", 499.99, 20));         // 2021
        xiaomis.add(new Xiaomi("Xiaomi", "Mi 11 Ultra", "Xiaomi", 799.99, 10));   // 2021
        xiaomis.add(new Xiaomi("Xiaomi", "12", "Xiaomi", 649.99, 18));            // 2022
        xiaomis.add(new Xiaomi("Xiaomi", "12 Pro", "Xiaomi", 849.99, 12));        // 2022
        xiaomis.add(new Xiaomi("Xiaomi", "13", "Xiaomi", 899.99, 22));            // 2023
        xiaomis.add(new Xiaomi("Xiaomi", "13 Pro", "Xiaomi", 1099.99, 8));        // 2023
        xiaomis.add(new Xiaomi("Xiaomi", "14", "Xiaomi", 1199.99, 5));            // 2024
        xiaomis.add(new Xiaomi("Xiaomi", "14 Ultra", "Xiaomi", 1399.99, 3));      // 2024
    }

    @Override
    public void showAllDevices() {
        System.out.println("\n📌 Verfügbare Xiaomi-Geräte:");
        if (xiaomis.isEmpty()) {
            System.out.println("❌ Keine Xiaomi-Geräte verfügbar.");
            return;
        }
        for (Xiaomi xiaomi : xiaomis) {
            String status = (xiaomi.getStock() > 0) ? "✅ Verfügbar: " + xiaomi.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + xiaomi.getName() + " " + xiaomi.getModel() + " | 💰 " + xiaomi.getPrice() + "€ | " + status);
        }
    }

    @Override
    public Xiaomi findDeviceByModel(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return null;
        }

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<Xiaomi> besteVorschläge = new ArrayList<>();

        for (Xiaomi xiaomi : xiaomis) {
            String model = xiaomi.getModel().replaceAll("\\s+", "").toLowerCase();
            String fullName = (xiaomi.getName() + xiaomi.getModel()).replaceAll("\\s+", "").toLowerCase();
            String shortName = xiaomi.getName().substring(0, 2).toLowerCase() + xiaomi.getModel();

            if (model.equals(userInput) || fullName.equals(userInput) || shortName.equals(userInput)) {
                return xiaomi;
            }

            int distanceModel = levenshtein.apply(model, userInput);
            int distanceFullName = levenshtein.apply(fullName, userInput);
            int distanceShortName = levenshtein.apply(shortName, userInput);

            int minCurrent = Math.min(distanceModel, Math.min(distanceFullName, distanceShortName));

            if (minCurrent < minDistance) {
                minDistance = minCurrent;
                besteVorschläge.clear();
                besteVorschläge.add(xiaomi);
            } else if (minCurrent == minDistance) {
                besteVorschläge.add(xiaomi);
            }
        }

        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (Xiaomi vorschlag : besteVorschläge) {
                System.out.println("   ➜ " + vorschlag.getName() + " " + vorschlag.getModel());
            }

            System.out.print("👉 Möchten Sie dieses Modell wählen? (Ja/Nein): ");
            Scanner scanner = new Scanner(System.in);
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
}
