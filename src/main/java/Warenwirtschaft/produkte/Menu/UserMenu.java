package Warenwirtschaft.produkte.Menu;

import Warenwirtschaft.produkte.Elektronik.Handys.Handys;
import Warenwirtschaft.produkte.Marken.Marken;
import Warenwirtschaft.produkte.User.AuthSystem;
import Warenwirtschaft.produkte.User.DatabaseConnection;
import Warenwirtschaft.produkte.User.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void showMainMenu() {
        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n📌 Willkommen im Handy-Shop!");
                System.out.println("1️⃣ Einloggen");
                System.out.println("2️⃣ Registrieren");
                System.out.println("3️⃣ Programm beenden");
                System.out.print("👉 Wähle eine Option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        loginUser();
                        break;
                    case "2":
                        registerUser();
                        break;
                    case "3":
                        System.out.println("👋 Programm wird beendet.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Ungültige Eingabe!");
                }
            } else {
                if (loggedInUser.isAdmin()) {
                    AdminMenu.showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
    }

    private static void showUserMenu() {
        while (true) {
            List<String> availableBrands = getAvailableBrands();
            Collections.sort(availableBrands);

            System.out.println("\n📌 Willkommen zurück, " + loggedInUser.getUsername() + "!");
            System.out.println("🔍 Wähle eine Marke:");

            for (int i = 0; i < availableBrands.size(); i++) {
                System.out.println((i + 1) + "️⃣ " + availableBrands.get(i));
            }
            System.out.println((availableBrands.size() + 1) + "️⃣ Warenkorb anzeigen");
            System.out.println((availableBrands.size() + 2) + "️⃣ Abmelden");
            System.out.print("👉 Wähle eine Option: ");

            String choice = scanner.nextLine();
            int selectedOption;

            try {
                selectedOption = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("❌ Ungültige Eingabe! Bitte eine Zahl eingeben.");
                return;
            }

            if (selectedOption > 0 && selectedOption <= availableBrands.size()) {
                String selectedBrand = availableBrands.get(selectedOption - 1);
                showCategoryForBrand(selectedBrand);
            } else if (selectedOption == availableBrands.size() + 1) {
                loggedInUser.getCart().showCart();
            } else if (selectedOption == availableBrands.size() + 2) {
                loggedInUser = null;
                System.out.println("👋 Du hast dich erfolgreich abgemeldet.");
                return;
            } else {
                System.out.println("❌ Ungültige Eingabe!");
            }
        }
    }

    private static void showCategoryForBrand(String brand) {
        List<String> categories = getAvailableCategoriesForBrand(brand);

        if (categories.isEmpty()) {
            System.out.println("❌ Für diese Marke sind derzeit keine Kategorien verfügbar.");
            return;
        }

        System.out.println("\n📌 Du hast '" + brand + "' ausgewählt!");
        System.out.println("🔍 Wähle eine Produktkategorie:");

        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + "️⃣ " + categories.get(i));
        }
        System.out.println((categories.size() + 1) + "️⃣ Zurück zur Markenübersicht");
        System.out.print("👉 Wähle eine Option: ");

        int selectedOption = Integer.parseInt(scanner.nextLine());
        if (selectedOption > 0 && selectedOption <= categories.size()) {
            String selectedCategory = categories.get(selectedOption - 1);
            showProductsForCategory(brand, selectedCategory);
        }
    }

    private static void showProductsForCategory(String brand, String category) {
        while (true) {
            List<String> products = getAvailableProductsForCategory(brand, category);

            if (products.isEmpty()) {
                System.out.println("❌ In dieser Kategorie sind derzeit keine Produkte verfügbar.");
                return;
            }

            System.out.println("\n📌 Du hast '" + category + "' für '" + brand + "' ausgewählt!");
            System.out.println("🔍 Wähle ein Produkt:");

            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + "️⃣ " + products.get(i));
            }
            System.out.println((products.size() + 1) + "️⃣ Zurück zur Kategorieübersicht");
            System.out.print("👉 Wähle eine Option: ");

            int selectedOption = Integer.parseInt(scanner.nextLine());
            if (selectedOption > 0 && selectedOption <= products.size()) {
                String selectedProduct = products.get(selectedOption - 1);
                handleDeviceSelection(selectedProduct, brand);
            } else if (selectedOption == products.size() + 1) {
                return;
            }
        }
    }


    private static List<String> getAvailableCategoriesForBrand(String brand) {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM products WHERE brand = ? ORDER BY category ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, brand);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Abrufen der Kategorien für " + brand);
            e.printStackTrace();
        }
        return categories;
    }

    private static List<String> getAvailableProductsForCategory(String brand, String category) {
        List<String> products = new ArrayList<>();
        String sql = "SELECT DISTINCT model FROM products WHERE brand = ? AND category = ? ORDER BY model ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, brand);
            stmt.setString(2, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(rs.getString("model"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Abrufen der Produkte für " + category + " von " + brand);
            e.printStackTrace();
        }
        return products;
    }


private static void loginUser() {
        System.out.print("👉 Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("👉 Passwort: ");
        String password = scanner.nextLine();

        loggedInUser = AuthSystem.login(username, password);

        if (loggedInUser != null) {
            System.out.println("✅ Login erfolgreich.");
        } else {
            System.out.println("❌ Benutzername oder Passwort falsch.");
        }
    }

    private static void registerUser() {
        System.out.print("👉 Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("👉 Passwort: ");
        String password = scanner.nextLine();
        System.out.print("👉 E-Mail: ");
        String email = scanner.nextLine();
        System.out.print("👉 Telefonnummer (optional): ");
        String phone = scanner.nextLine();

        boolean success = AuthSystem.register(username, password, email, phone);

        if (success) {
            System.out.println("✅ Registrierung erfolgreich!");
            loggedInUser = AuthSystem.login(username, password);
        } else {
            System.out.println("❌ Benutzername oder E-Mail existiert bereits!");
        }
    }

    private static List<String> getAvailableBrands() {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT DISTINCT brand FROM products ORDER BY brand ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String brand = rs.getString("brand").toUpperCase(); // Einheitlich groß schreiben
                if (!brands.contains(brand)) {
                    brands.add(brand);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Abrufen der Marken.");
            e.printStackTrace();
        }

        return brands;
    }



    private static void handleDeviceSelection(String model, String brand) {
        int brandId = getBrandIdByName(brand);         // ✅ funktioniert
        List<Handys> varianten = Handys.getModelVariants(model, brandId);

        if (varianten.isEmpty()) {
            System.out.println("❌ Keine Varianten für dieses Modell gefunden.");
            return;
        }

        System.out.println("\n📱 Verfügbare Varianten für Modell '" + model + "':");
        for (int i = 0; i < varianten.size(); i++) {
            Handys handy = varianten.get(i);
            System.out.printf("%d️⃣ %s (%s, %s) | 💰 %.2f€ | Bestand: %d\n",
                    (i + 1), handy.getModel(), handy.getColor(), handy.getStorage(), handy.getPrice(), handy.getStock());
        }

        System.out.println((varianten.size() + 1) + "️⃣ Zurück");
        System.out.print("👉 Wähle eine Variante: ");

        int auswahl = safeIntInput(1, varianten.size() + 1);

        if (auswahl == varianten.size() + 1) {
            return; // Zurück
        }

        Handys gewähltesHandy = varianten.get(auswahl - 1);

        System.out.print("👉 Möchtest du dieses Modell kaufen? (ja/nein): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.matches("ja|j|yes|y")) {
            gewähltesHandy.decreaseStockInDB();
            loggedInUser.getCart().addToCart(gewähltesHandy);
            System.out.println("✅ Produkt wurde zum Warenkorb hinzugefügt!");
        }
    }



    private static int safeIntInput(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int number = Integer.parseInt(input);

                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.out.print("❌ Bitte gib eine Zahl zwischen " + min + " und " + max + " ein: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("❌ Ungültige Eingabe! Bitte eine Zahl eingeben: ");
            }
        }
    }


    private static int getBrandIdByName(String brandName) {
        String sql = "SELECT id FROM brands WHERE UPPER(name) = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, brandName.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Suchen der Brand-ID für: " + brandName);
            e.printStackTrace();
        }
        return -1;
    }




}
