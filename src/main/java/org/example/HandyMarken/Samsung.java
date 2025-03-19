package org.example.HandyMarken;

import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class Samsung extends Handys implements HandyInterface {
    private static ArrayList<Samsung> samsungs = new ArrayList<>();

    public Samsung(String name, String model, String brand, double price, int stock) {
        super(name, model, brand, price, stock);
    }

    static {
        samsungs.add(new Samsung("Samsung", "S15", "Samsung", 699.99, 10));
        samsungs.add(new Samsung("Samsung", "S16", "Samsung", 749.99, 12));
        samsungs.add(new Samsung("Samsung", "S17", "Samsung", 799.99, 14));
        samsungs.add(new Samsung("Samsung", "S18", "Samsung", 849.99, 16));
        samsungs.add(new Samsung("Samsung", "S19", "Samsung", 899.99, 18));
        samsungs.add(new Samsung("Samsung", "S20", "Samsung", 949.99, 20));
        samsungs.add(new Samsung("Samsung", "S21", "Samsung", 999.99, 22));
        samsungs.add(new Samsung("Samsung", "S22", "Samsung", 1049.99, 24));
        samsungs.add(new Samsung("Samsung", "S23", "Samsung", 1099.99, 26));
    }

    @Override
    public void showAllDevices() {
        System.out.println("\n📌 Verfügbare Samsung-Geräte:");
        if (samsungs.isEmpty()) {
            System.out.println("❌ Keine Samsung-Geräte verfügbar.");
            return;
        }
        for (Samsung samsung : samsungs) {
            String status = (samsung.getStock() > 0) ? "✅ Verfügbar: " + samsung.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + samsung.getName() + " " + samsung.getModel() + " | 💰 " + samsung.getPrice() + "€ | " + status);
        }
    }

    @Override
    public Samsung findDeviceByModel(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return null;
        }

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<Samsung> besteVorschläge = new ArrayList<>();

        for (Samsung samsung : samsungs) {
            String model = samsung.getModel().replaceAll("\\s+", "").toLowerCase();
            String fullName = (samsung.getName() + samsung.getModel()).replaceAll("\\s+", "").toLowerCase();
            String shortName = samsung.getName().substring(0, 2).toLowerCase() + samsung.getModel();

            if (model.equals(userInput) || fullName.equals(userInput) || shortName.equals(userInput)) {
                return samsung;
            }

            int distanceModel = levenshtein.apply(model, userInput);
            int distanceFullName = levenshtein.apply(fullName, userInput);
            int distanceShortName = levenshtein.apply(shortName, userInput);

            int minCurrent = Math.min(distanceModel, Math.min(distanceFullName, distanceShortName));

            if (minCurrent < minDistance) {
                minDistance = minCurrent;
                besteVorschläge.clear();
                besteVorschläge.add(samsung);
            } else if (minCurrent == minDistance) {
                besteVorschläge.add(samsung);
            }
        }

        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (Samsung vorschlag : besteVorschläge) {
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
