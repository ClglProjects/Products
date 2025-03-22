package Warenwirtschaft.products.ui;

import Warenwirtschaft.products.user.DatabaseConnection;
import Warenwirtschaft.products.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static User loggedInAdmin;
    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_TIME = 5000;
    private static int attemptCounter = 0;

    public static boolean login() {
        boolean loggedIn = false;

        while (attemptCounter < MAX_ATTEMPTS) {
            System.out.print("👉 Benutzername: ");
            String username = scanner.nextLine().trim();

            System.out.print("👉 Passwort: ");
            String password = scanner.nextLine().trim();


            String correctUsername = "admin";
            String correctPassword = "adminpasswort";

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                loggedIn = true;
                System.out.println("✅ Login erfolgreich.");
                System.out.println("👐 Willkommen zurück, " + username + "!");
                break;
            } else {
                attemptCounter++;
                System.out.println("❌ Benutzername oder Passwort falsch. Versuchen Sie es erneut.");
                if (attemptCounter >= MAX_ATTEMPTS) {
                    System.out.println("❌ Zu viele falsche Anmeldeversuche. Bitte warten Sie 5 Sekunden.");
                    try {
                        Thread.sleep(LOCK_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    attemptCounter = 0;
                }
            }
        }
        return loggedIn;
    }

    // Admin-Menü anzeigen
    public static void showAdminMenu() {
        if (!login()) {
            System.out.println("❌ Anmeldung fehlgeschlagen. Programm wird beendet.");
            return; // Beendet das Programm, wenn der Login fehlgeschlagen ist
        }

        while (true) {
            System.out.println("\n\ud83d\udc50 Willkommen zurück, Admin!");
            System.out.println("1️⃣ Alle Benutzer anzeigen");
            System.out.println("2️⃣ Geräte verwalten (Hinzufügen/Löschen)");
            System.out.println("3️⃣ Bestellungen anzeigen");
            System.out.println("4️⃣ Zurück zum Hauptmenü");
            System.out.print("👉 Wähle eine Option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showAllUsers();
                    break;
                case "2":
                    manageDevices();
                    break;
                case "3":
                    viewOrders();
                    break;
                case "4":
                    return; // Zurück zum Hauptmenü
                default:
                    System.out.println("❌ Ungültige Eingabe!");
            }
        }
    }


    // Methode zur Anzeige aller Benutzer
    private static void showAllUsers() {
        System.out.println("\n📌 Liste aller Benutzer:");
        String sql = "SELECT id, username, email, phone FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("👤 ID: " + rs.getInt("id") + " | Benutzername: " + rs.getString("username")
                        + " | 📧 E-Mail: " + rs.getString("email") + " | 📞 Telefon: " + rs.getString("phone"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Benutzer.");
            e.printStackTrace();
        }
    }

    private static void manageDevices() {
        System.out.println("\n📌 Geräte-Verwaltung:");
        System.out.println("1️⃣ Neues Gerät hinzufügen");
        System.out.println("2️⃣ Gerät entfernen");
        System.out.println("3️⃣ Zurück");
        System.out.print("👉 Wähle eine Option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                addNewDevice();
                break;
            case "2":
                removeDevice();
                break;
            case "3":
                return;
            default:
                System.out.println("❌ Ungültige Eingabe!");
        }
    }

    private static void addNewDevice() {
        String name = null, model = null, brand = null, color = null, storage = null;
        double price = 0;
        int stock = 0;

        while (true) {
            System.out.print("👉 Name des Geräts: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("❌ Der Name des Geräts darf nicht leer sein!");
                continue;
            }

            System.out.print("👉 Modell: ");
            model = scanner.nextLine().trim();
            if (model.isEmpty()) {
                System.out.println("❌ Das Modell darf nicht leer sein!");
                continue;
            }

            System.out.println("👉 Wähle eine Marke:");
            String[] availableBrands = {"iphone", "samsung", "xiaomi", "huawei", "google", "oneplus"};
            for (int i = 0; i < availableBrands.length; i++) {
                System.out.println((i + 1) + "️⃣ " + availableBrands[i]);
            }
            System.out.print("👉 Wähle eine Marke (1-" + availableBrands.length + "): ");
            int brandChoice = Integer.parseInt(scanner.nextLine().trim());
            if (brandChoice < 1 || brandChoice > availableBrands.length) {
                System.out.println("❌ Ungültige Auswahl!");
                continue;
            }
            brand = availableBrands[brandChoice - 1];


            System.out.println("👉 Wähle eine Farbe:");
            String[] availableColors = {"Schwarz", "Weiß", "Blau", "Rot", "Silber"};
            for (int i = 0; i < availableColors.length; i++) {
                System.out.println((i + 1) + "️⃣ " + availableColors[i]);
            }
            System.out.print("👉 Wähle eine Farbe (1-" + availableColors.length + "): ");
            int colorChoice = Integer.parseInt(scanner.nextLine().trim());
            if (colorChoice < 1 || colorChoice > availableColors.length) {
                System.out.println("❌ Ungültige Auswahl!");
                continue;
            }
            color = availableColors[colorChoice - 1];

            System.out.println("👉 Wähle eine Speichergröße:");
            String[] availableStorage = {"64GB", "128GB", "256GB", "512GB"};
            for (int i = 0; i < availableStorage.length; i++) {
                System.out.println((i + 1) + "️⃣ " + availableStorage[i]);
            }
            System.out.print("👉 Wähle eine Speichergröße (1-" + availableStorage.length + "): ");
            int storageChoice = Integer.parseInt(scanner.nextLine().trim());
            if (storageChoice < 1 || storageChoice > availableStorage.length) {
                System.out.println("❌ Ungültige Auswahl!");
                continue;
            }
            storage = availableStorage[storageChoice - 1];

            System.out.print("👉 Preis: ");
            try {
                price = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ungültiger Preis. Bitte eine gültige Zahl eingeben.");
                continue;  // Preis erneut eingeben
            }

            System.out.print("👉 Lagerbestand: ");
            try {
                stock = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ungültiger Lagerbestand. Bitte eine gültige Zahl eingeben.");
                continue;
            }

            System.out.println("\nBitte überprüfen Sie Ihre Eingaben:");
            System.out.println("Name: " + name);
            System.out.println("Modell: " + model);
            System.out.println("Marke: " + brand);
            System.out.println("Farbe: " + color);
            System.out.println("Speicher: " + storage);
            System.out.println("Preis: " + price + "€");
            System.out.println("Lagerbestand: " + stock);

            System.out.print("👉 Möchten Sie diese Eingaben speichern? (ja/nein): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("ja")) {
                // Einfügen des Geräts in die Datenbank
                String sql = "INSERT INTO handys (name, model, brand, color, storage, price, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement stmt = connection.prepareStatement(sql)) {

                    stmt.setString(1, name);
                    stmt.setString(2, model);
                    stmt.setString(3, brand);
                    stmt.setString(4, color);
                    stmt.setString(5, storage);
                    stmt.setDouble(6, price);
                    stmt.setInt(7, stock);
                    stmt.executeUpdate();

                    System.out.println("✅ Gerät erfolgreich hinzugefügt!");
                    break;
                } catch (Exception e) {
                    System.out.println("❌ Fehler beim Hinzufügen des Geräts.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("❌ Eingaben wurden nicht gespeichert.");
                System.out.println("👉 Möchten Sie die Eingaben korrigieren oder zurückgehen? (korrigieren/abbrechen): ");
                String action = scanner.nextLine().trim().toLowerCase();
                if (action.equals("abbrechen")) {
                    break;  // Abbrechen und zurückkehren
                }
            }
        }
    }




    private static void removeDevice() {
        System.out.print("👉 Modell des zu löschenden Geräts: ");
        String model = scanner.nextLine().trim();

        String sql = "DELETE FROM handys WHERE model = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, model);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Gerät erfolgreich entfernt!");
            } else {
                System.out.println("❌ Kein Gerät mit diesem Modell gefunden.");
            }

        } catch (Exception e) {
            System.out.println("❌ Fehler beim Entfernen des Geräts.");
            e.printStackTrace();
        }
    }

    private static void viewOrders() {
        System.out.println("📦 Bestellübersicht folgt...");
        // Hier könnte eine Abfrage für Bestellungen hinzugefügt werden
    }

    static String getCorrectBrandName(String brand) {
        Map<String, String> brandCorrections = new HashMap<>();
        brandCorrections.put("xiami", "xiaomi");
        brandCorrections.put("xiamo", "xiaomi");
        brandCorrections.put("samsng", "samsung");
        brandCorrections.put("ipone", "iphone");

        return brandCorrections.getOrDefault(brand, brand);
    }

















}