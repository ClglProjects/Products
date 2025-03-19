package org.example.HandyMarken;

import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class GooglePixel extends Handys implements HandyInterface {
    private static ArrayList<GooglePixel> pixels = new ArrayList<>();

    public GooglePixel(String name, String model, String brand, double price, int stock) {
        super(name, model, brand, price, stock);
    }



    @Override
    public void showAllDevices() {
        System.out.println("\n📌 Verfügbare Google Pixel-Geräte:");
        if (pixels.isEmpty()) {
            System.out.println("❌ Keine Google Pixel-Geräte verfügbar.");
            return;
        }
        for (GooglePixel pixel : pixels) {
            String status = (pixel.getStock() > 0) ? "✅ Verfügbar: " + pixel.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + pixel.getName() + " " + pixel.getModel() + " | 💰 " + pixel.getPrice() + "€ | " + status);
        }
    }

    @Override
    public GooglePixel findDeviceByModel(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return null;
        }

        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<GooglePixel> besteVorschläge = new ArrayList<>();

        for (GooglePixel pixel : pixels) {
            String model = pixel.getModel().replaceAll("\\s+", "").toLowerCase();
            String fullName = (pixel.getName() + pixel.getModel()).replaceAll("\\s+", "").toLowerCase();
            String shortName = pixel.getName().substring(0, 2).toLowerCase() + pixel.getModel();

            if (model.equals(userInput) || fullName.equals(userInput) || shortName.equals(userInput)) {
                return pixel;
            }

            int distanceModel = levenshtein.apply(model, userInput);
            int distanceFullName = levenshtein.apply(fullName, userInput);
            int distanceShortName = levenshtein.apply(shortName, userInput);

            int minCurrent = Math.min(distanceModel, Math.min(distanceFullName, distanceShortName));

            if (minCurrent < minDistance) {
                minDistance = minCurrent;
                besteVorschläge.clear();
                besteVorschläge.add(pixel);
            } else if (minCurrent == minDistance) {
                besteVorschläge.add(pixel);
            }
        }

        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (GooglePixel vorschlag : besteVorschläge) {
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





/*


 static {
        pixels.add(new GooglePixel("Google Pixel", "4a", "Google", 299.99, 10));         // 2020
        pixels.add(new GooglePixel("Google Pixel", "5", "Google", 499.99, 15));         // 2020
        pixels.add(new GooglePixel("Google Pixel", "6", "Google", 599.99, 20));         // 2021
        pixels.add(new GooglePixel("Google Pixel", "6 Pro", "Google", 899.99, 12));     // 2021
        pixels.add(new GooglePixel("Google Pixel", "7", "Google", 649.99, 18));         // 2022
        pixels.add(new GooglePixel("Google Pixel", "7 Pro", "Google", 949.99, 10));     // 2022
        pixels.add(new GooglePixel("Google Pixel", "8", "Google", 799.99, 14));         // 2023
        pixels.add(new GooglePixel("Google Pixel", "8 Pro", "Google", 1099.99, 8));     // 2023
        pixels.add(new GooglePixel("Google Pixel", "Fold", "Google", 1799.99, 5));      // 2023
        pixels.add(new GooglePixel("Google Pixel", "9", "Google", 999.99, 4));          // 2024
    }


 */