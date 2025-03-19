package org.example.HandyMarken;

import org.apache.commons.text.similarity.LevenshteinDistance;
import java.util.ArrayList;
import java.util.Scanner;

public class Iphone extends Handys implements HandyInterface {
    private String ios;
    private static ArrayList<Iphone> iphoneList = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);


    public Iphone(String thename, String themodel, String thebrand, double theprice, int stock) {
        super(thename, themodel, thebrand, theprice, stock);
    }
    static {
        System.out.println("📢 iPhone-Liste wird geladen...");
        iphoneList.add(new Iphone("iPhone", "1", "Apple", 299.99, 10));
        iphoneList.add(new Iphone("iPhone", "2", "Apple", 349.99, 5));
        iphoneList.add(new Iphone("iPhone", "3", "Apple", 399.99, 7));
        iphoneList.add(new Iphone("iPhone", "4", "Apple", 449.99, 9));
        iphoneList.add(new Iphone("iPhone", "5", "Apple", 499.99, 11));
        iphoneList.add(new Iphone("iPhone", "6", "Apple", 549.99, 13));
        iphoneList.add(new Iphone("iPhone", "7", "Apple", 599.99, 15));
        iphoneList.add(new Iphone("iPhone", "8", "Apple", 649.99, 17));
        iphoneList.add(new Iphone("iPhone", "9", "Apple", 699.99, 19));
        iphoneList.add(new Iphone("iPhone", "X", "Apple", 749.99, 20));
        iphoneList.add(new Iphone("iPhone", "11", "Apple", 799.99, 25));
        iphoneList.add(new Iphone("iPhone", "12", "Apple", 849.99, 30));
        iphoneList.add(new Iphone("iPhone", "13", "Apple", 899.99, 35));
        iphoneList.add(new Iphone("iPhone", "14", "Apple", 949.99, 40));
        iphoneList.add(new Iphone("iPhone", "15", "Apple", 999.99, 45));
        iphoneList.add(new Iphone("iPhone", "16", "Apple", 1099.99, 50));
        System.out.println("✅ iPhones erfolgreich hinzugefügt!");
    }

    // 📌 Zeigt alle iPhones an
    public static void showAllIphone() {
        System.out.println("\n📌 Verfügbare iPhones:");

        if (iphoneList.isEmpty()) {
            System.out.println("❌ Keine iPhones verfügbar.");
            return;
        }

        for (Iphone iphone : iphoneList) {
            String status = (iphone.getStock() > 0) ? "✅ Verfügbar: " + iphone.getStock() : "❌ Nicht verfügbar";
            System.out.println("📌 " + iphone.getName() + " " + iphone.getModel() + " | 💰 " + iphone.getPrice() + "€ | " + status);
        }
    }

    // 📌 Bestand um 1 reduzieren (wenn verfügbar)
    public void decreaseStock() {
        if (getStock() > 0) {
            setStock(getStock() - 1);
            System.out.println("✅ Kauf erfolgreich! Neuer Bestand: " + getStock());
        } else {
            System.out.println("❌ Nicht verfügbar! iPhone ist ausverkauft.");
        }
    }

    // 📌 iPhone-Modell suchen (mit Fehlerkorrektur)
    public static Iphone findIphoneByModel(String userInput) {
        if (userInput == null || userInput.trim().isEmpty() || userInput.length() < 2) {
            System.out.println("❌ Ungültige Eingabe! Bitte mindestens 2 Zeichen eingeben.");
            return null;
        }

        // Eingabe bereinigen
        userInput = userInput.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        int minDistance = Integer.MAX_VALUE;
        ArrayList<Iphone> besteVorschläge = new ArrayList<>();

        for (Iphone iphone : iphoneList) {
            String model = iphone.getModel().replaceAll("\\s+", "").toLowerCase();
            String fullName = (iphone.getName() + iphone.getModel()).replaceAll("\\s+", "").toLowerCase();
            String shortName = iphone.getName().substring(0, 2).toLowerCase() + iphone.getModel();


            if (model.equals(userInput) || fullName.equals(userInput) || shortName.equals(userInput)) {
                return iphone;
            }


            int distance = levenshtein.apply(fullName, userInput);
            if (distance < minDistance) {
                minDistance = distance;
                besteVorschläge.clear();
                besteVorschläge.add(iphone);
            } else if (distance == minDistance) {
                besteVorschläge.add(iphone);
            }
        }


        if (!besteVorschläge.isEmpty() && minDistance <= 3) {
            System.out.println("❓ Meinten Sie vielleicht eines dieser Modelle?");
            for (Iphone vorschlag : besteVorschläge) {
                System.out.println("   ➜ " + vorschlag.getName() + " " + vorschlag.getModel());
            }

            // Benutzer nach Bestätigung fragen
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
        showAllIphone();
    }

    @Override
    public Handys findDeviceByModel(String userInput) {
        return findIphoneByModel(userInput);
    }
}