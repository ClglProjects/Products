package org.example.Menu;

import java.util.Scanner;
import org.example.HandyMarken.*;
import org.example.User.User;
import org.example.User.AuthSystem;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null; // Null bedeutet: Niemand eingeloggt
    private static Warenkorb warenkorb = new Warenkorb();

    public static void showMainMenu() {
        while (true) {
            if (loggedInUser == null) {
                // Login-/Registrierungs-Menü
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
                // Hauptmenü nach erfolgreichem Login
                System.out.println("\n📌 Willkommen zurück, " + loggedInUser.getUsername() + "!");
                System.out.println("1️⃣ Apple (iPhones anzeigen)");
                System.out.println("2️⃣ Samsung (Samsung-Handys anzeigen)");
                System.out.println("3️⃣ Huawei (Huawei-Handys anzeigen)");
                System.out.println("4️⃣ Xiaomi (Xiaomi-Handys anzeigen)");
                System.out.println("5️⃣ Google Pixel (Pixel-Handys anzeigen)");
                System.out.println("6️⃣ Warenkorb anzeigen");
                System.out.println("7️⃣ Abmelden");
                System.out.print("👉 Wähle eine Option: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        // Hier 7 Argumente: name, model, brand, color, storage, price, stock
                        Iphone iphone = new Iphone("", "", "Apple", "", "", 0.0, 0);
                        iphone.showAllDevices();
                        handleDeviceSelection(scanner, iphone);
                        break;
                    case "2":
                        Samsung samsung = new Samsung("", "", "Samsung", "", "", 0.0, 0);
                        samsung.showAllDevices();
                        handleDeviceSelection(scanner, samsung);
                        break;
                    case "3":
                        Huawei huawei = new Huawei("", "", "Huawei", "", "", 0.0, 0);
                        huawei.showAllDevices();
                        handleDeviceSelection(scanner, huawei);
                        break;
                    case "4":
                        Xiaomi xiaomi = new Xiaomi("", "", "Xiaomi", "", "", 0.0, 0);
                        xiaomi.showAllDevices();
                        handleDeviceSelection(scanner, xiaomi);
                        break;
                    case "5":
                        GooglePixel pixel = new GooglePixel("", "", "Google", "", "", 0.0, 0);
                        pixel.showAllDevices();
                        handleDeviceSelection(scanner, pixel);
                        break;
                    case "6":

                        loggedInUser.getCart().showCart();
                        break;
                    case "7":
                        loggedInUser = null;
                        System.out.println("👋 Du hast dich erfolgreich abgemeldet.");
                        break;
                    default:
                        System.out.println("❌ Ungültige Eingabe! Bitte eine Zahl zwischen 1 und 7 eingeben.");
                }
            }
        }
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



    private static void handleDeviceSelection(Scanner scanner, HandyInterface brand) {
        while (true) {
            System.out.print("\n📱 Gib das Modell ein, das du dir wünschst (oder 'zurück' zum Hauptmenü): ");
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (userInput.equals("zurück")) {
                return;
            }

            Handys gefundenesGerät = brand.findDeviceByModel(userInput);

            if (gefundenesGerät != null) {
                System.out.println("✅ Gefunden: " + gefundenesGerät.getName()
                        + " " + gefundenesGerät.getModel()
                        + " (" + gefundenesGerät.getColor() + ", " + gefundenesGerät.getStorage() + ")"
                        + " - 💰 " + gefundenesGerät.getPrice() + "€");
                System.out.print("👉 Möchtest du dieses Modell kaufen? (Ja/Nein): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if (confirmation.matches("^(ja|j|yes|y|si|oui|aye|jo|yep|yup)$")) {

                    gefundenesGerät.decreaseStock();
                    loggedInUser.getCart().addToCart(gefundenesGerät);
                    System.out.println("🛒 " + gefundenesGerät.getName() + " " + gefundenesGerät.getModel()
                            + " wurde zum Warenkorb hinzugefügt!");


                    loggedInUser.getCart().showCart();

                    System.out.println("\n🔄 Was möchtest du tun?");
                    System.out.println("1️⃣ Neues Modell eingeben");
                    System.out.println("2️⃣ Zurück zum Hauptmenü");
                    System.out.println("3️⃣ Zur Kasse gehen");
                    System.out.print("👉 Deine Wahl: ");

                    String choice = scanner.nextLine().trim();

                    switch (choice) {
                        case "1":
                            continue;
                        case "2":
                            return;
                        case "3":
                            loggedInUser.getCart().checkout();
                            return;
                        default:
                            System.out.println("❌ Ungültige Eingabe! Zurück zum Hauptmenü.");
                            return;
                    }

                } else {
                    System.out.println("❌ Kein Problem! Wähle ein anderes Modell oder tippe 'zurück'.");
                }
            } else {
                System.out.println("❌ Modell nicht gefunden. Versuche es erneut.");
            }
        }
    }
}