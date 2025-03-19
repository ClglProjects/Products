package org.example.Menu;

import java.util.Scanner;
import org.example.HandyMarken.*;
import org.example.User.User;
import org.example.User.AuthSystem;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null; // Initialisieren mit null, um zu signalisieren, dass niemand eingeloggt ist
    private static Warenkorb warenkorb = new Warenkorb();

    public static void showMainMenu() {
        while (true) {
            if (loggedInUser == null) {
                // Wenn der Benutzer nicht eingeloggt ist, zeige Login- und Registrierungs-Option
                System.out.println("\n📌 Willkommen im Handy-Shop!");
                System.out.println("1️⃣ Einloggen");
                System.out.println("2️⃣ Registrieren");
                System.out.println("3️⃣ Programm beenden");
                System.out.print("👉 Wähle eine Option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        loginUser(); // Benutzer einloggen
                        break;
                    case "2":
                        registerUser(); // Benutzer registrieren
                        break;
                    case "3":
                        System.out.println("👋 Programm wird beendet.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Ungültige Eingabe!");
                        break;
                }
            } else {
                // Wenn der Benutzer eingeloggt ist, zeige die Optionen
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
                        Iphone iphone = new Iphone("", "", "Apple", 0, 0);
                        iphone.showAllDevices();
                        handleDeviceSelection(scanner, iphone);
                        break;
                    case "2":
                        Samsung samsung = new Samsung("", "", "Samsung", 0, 0);
                        samsung.showAllDevices();
                        handleDeviceSelection(scanner, samsung);
                        break;
                    case "3":
                        Huawei huawei = new Huawei("", "", "Huawei", 0, 0);
                        huawei.showAllDevices();
                        handleDeviceSelection(scanner, huawei);
                        break;
                    case "4":
                        Xiaomi xiaomi = new Xiaomi("", "", "Xiaomi", 0, 0);
                        xiaomi.showAllDevices();
                        handleDeviceSelection(scanner, xiaomi);
                        break;
                    case "5":
                        GooglePixel pixel = new GooglePixel("", "", "Google", 0, 0);
                        pixel.showAllDevices();
                        handleDeviceSelection(scanner, pixel);
                        break;
                    case "6":
                        // Warenkorb anzeigen für den eingeloggenen Benutzer
                        loggedInUser.getCart().showCart();
                        break;
                    case "7":
                        loggedInUser = null;  // Benutzer abmelden
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

        loggedInUser = AuthSystem.login(username, password);  // Benutzer authentifizieren

        if (loggedInUser != null) {
            System.out.println("✅ Du bist jetzt eingeloggt!");
        } else {
            System.out.println("❌ Login fehlgeschlagen! Bitte versuche es erneut.");
        }
    }

    private static void registerUser() {
        System.out.print("👉 Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("👉 Passwort: ");
        String password = scanner.nextLine();

        boolean success = AuthSystem.register(username, password);  // Benutzer registrieren

        if (success) {
            System.out.println("✅ Registrierung erfolgreich!");
            loggedInUser = AuthSystem.login(username, password); // Sofort einloggen nach der Registrierung
        } else {
            System.out.println("❌ Registrierung fehlgeschlagen! Benutzername könnte bereits existieren.");
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
                System.out.println("✅ Gefunden: " + gefundenesGerät.getName() + " " + gefundenesGerät.getModel() + " - 💰 " + gefundenesGerät.getPrice() + "€");
                System.out.print("👉 Möchtest du dieses Modell kaufen? (Ja/Nein): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if (confirmation.matches("^(ja|j|yes|y|si|oui|aye|jo|yep|yup)$")) {
                    gefundenesGerät.decreaseStock();
                    loggedInUser.getCart().addToCart(gefundenesGerät); // Zum Warenkorb des angemeldeten Benutzers hinzufügen
                    System.out.println("🛒 " + gefundenesGerät.getName() + " " + gefundenesGerät.getModel() + " wurde zum Warenkorb hinzugefügt!");

                    // 📌 **Warenkorb direkt anzeigen nach dem Kauf**
                    loggedInUser.getCart().showCart();

                    // 📌 **Nach dem Kauf fragen, was der Nutzer tun will**
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
                            loggedInUser.getCart().checkout(); // Zum Checkout
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
