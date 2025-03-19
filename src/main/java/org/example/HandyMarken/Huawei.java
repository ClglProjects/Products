package org.example.HandyMarken;

import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class Huawei extends Handys implements HandyInterface {
    private static ArrayList<Huawei> huaweis = new ArrayList<>();

    public Huawei(String name, String model, String brand, double price, int stock) {
        super(name, model, brand, price, stock);
    }

    static {
        huaweis.add(new Huawei("Huawei", "Y9 Prime", "Huawei", 299.99, 30));    // 2018
        huaweis.add(new Huawei("Huawei", "P20 Pro", "Huawei", 599.99, 35));     // 2018
        huaweis.add(new Huawei("Huawei", "P30 Pro", "Huawei", 298.00, 18));     // 2019
        huaweis.add(new Huawei("Huawei", "Mate 40 Pro", "Huawei", 413.99, 20)); // 2020
        huaweis.add(new Huawei("Huawei", "P40 Pro", "Huawei", 320.73, 12));     // 2020
        huaweis.add(new Huawei("Huawei", "Nova 9", "Huawei", 262.00, 25));      // 2021
        huaweis.add(new Huawei("Huawei", "P50 Pro", "Huawei", 1138.55, 10));    // 2021
        huaweis.add(new Huawei("Huawei", "Mate 50 Pro", "Huawei", 649.99, 15)); // 2022
        huaweis.add(new Huawei("Huawei", "Mate Xs 2", "Huawei", 1999.99, 5));   // 2022
    }

    @Override
    public void showAllDevices() {
        System.out.println("\n📌 Verfügbare Huawei-Geräte (von ältesten zu neuesten Modellen):");
        if (huaweis.isEmpty()) {
            System.out.println("❌ Keine Huawei-Geräte verfügbar.");
            return;
        }
        for (Huawei huawei : huaweis) {
            String status = (huawei.getStock() > 0) ? "✅ Verfügbar: " + huawei.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + huawei.getName() + " " + huawei.getModel() + " | 💰 " + huawei.getPrice() + "€ | " + status);
        }
    }

    @Override
    public Huawei findDeviceByModel(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return null;
        }

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<Huawei> besteVorschläge = new ArrayList<>();

        for (Huawei huawei : huaweis) {
            String model = huawei.getModel().replaceAll("\\s+", "").toLowerCase();
            String fullName = (huawei.getName() + huawei.getModel()).replaceAll("\\s+", "").toLowerCase();
            String shortName = huawei.getName().substring(0, 2).toLowerCase() + huawei.getModel();

            if (model.equals(userInput) || fullName.equals(userInput) || shortName.equals(userInput)) {
                return huawei;
            }

            int distanceModel = levenshtein.apply(model, userInput);
            int distanceFullName = levenshtein.apply(fullName, userInput);
            int distanceShortName = levenshtein.apply(shortName, userInput);

            int minCurrent = Math.min(distanceModel, Math.min(distanceFullName, distanceShortName));

            if (minCurrent < minDistance) {
                minDistance = minCurrent;
                besteVorschläge.clear();
                besteVorschläge.add(huawei);
            } else if (minCurrent == minDistance) {
                besteVorschläge.add(huawei);
            }
        }

        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (Huawei vorschlag : besteVorschläge) {
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
