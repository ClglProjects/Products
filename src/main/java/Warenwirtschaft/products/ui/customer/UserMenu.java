package Warenwirtschaft.products.ui.customer;

import Warenwirtschaft.products.ui.admin.AdminMenu;
import Warenwirtschaft.products.user.User;

import java.util.Scanner;

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
                System.out.print("👉 Auswahl: ");

                switch (scanner.nextLine()) {
                    case "1" -> loggedInUser = AuthMenu.login(scanner);
                    case "2" -> loggedInUser = AuthMenu.register(scanner);
                    case "3" -> {
                        System.out.println("👋 Tschüss!");
                        return;
                    }
                    default -> System.out.println("❌ Ungültige Eingabe!");
                }
            } else if (loggedInUser.isAdmin()) {
                AdminMenu.show();
            } else {
                System.out.println("\n🛍️ Menü für " + loggedInUser.getUsername());
                System.out.println("1️⃣ Nach Kategorie shoppen");
                System.out.println("2️⃣ Nach Marke shoppen");
                System.out.println("3️⃣ Meine Bestellungen");
                System.out.println("4️⃣ Warenkorb anzeigen");
                System.out.println("5️⃣ Abmelden");
                System.out.print("👉 Auswahl: ");

                switch (scanner.nextLine()) {
                    case "1" -> UserShopNavigator.showCategoryFirstMenu(scanner, loggedInUser);
                    case "2" -> UserShopNavigator.showBrandShop(scanner, loggedInUser);
                    case "3" -> OrderManager.showUserOrders(loggedInUser);
                    case "4" -> CartMenu.showCartMenu(scanner, loggedInUser);
                    case "5" -> {
                        System.out.println("👋 Abgemeldet.");
                        loggedInUser = null;
                    }
                    default -> System.out.println("❌ Ungültige Eingabe!");
                }
            }
        }
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
}
