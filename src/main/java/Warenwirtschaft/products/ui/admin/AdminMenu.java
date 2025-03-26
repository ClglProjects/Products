package Warenwirtschaft.products.ui.admin;

import Warenwirtschaft.products.ui.customer.OrderManager;
import Warenwirtschaft.products.user.User;
import java.util.Scanner;
import Warenwirtschaft.products.ui.customer.UserMenu;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_TIME = 5000;
    private static int attemptCounter = 0;
    private static User loggedInAdmin;

    public static void show() {
        while (true) {
            System.out.println("\n🧠 Admin-Menü:");
            System.out.println("1️⃣ Benutzerverwaltung");
            System.out.println("2️⃣ Produktverwaltung");
            System.out.println("3️⃣ Bestellungen anzeigen");
            System.out.println("4️⃣ Abmelden");
            System.out.print("👉 Auswahl: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    UserManager.show();
                    break;
                case "2":
                    AdminProductManager.show();
                    break;
                case "3":
                    OrderManager.show();
                    break;
                case "4":
                    System.out.println("👋 Abgemeldet.");
                    Warenwirtschaft.products.ui.customer.UserMenu.setLoggedInUser(null);
                    return;
                default:
                    System.out.println("❌ Ungültige Eingabe!");
            }
        }
    }

}
