package Warenwirtschaft.products.ui.customer;

import Warenwirtschaft.products.model.VariantProdukt;
import Warenwirtschaft.products.user.User;

import java.util.Scanner;

public class CartMenu {

    public static void showCartMenu(Scanner scanner, User user) {
        while (true) {
            user.getCart().showCart();
            System.out.println("1️⃣ Produkt entfernen");
            System.out.println("2️⃣ Menge ändern");
            System.out.println("3️⃣ Checkout");
            System.out.println("4️⃣ Zurück");
            System.out.print("👉 Auswahl: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> removeFromCart(scanner, user);
                case "2" -> updateQuantity(scanner, user);
                case "3" -> user.getCart().checkout(user);
                case "4" -> { return; }
                default -> System.out.println("❌ Ungültige Eingabe!");
            }
        }
    }

    private static void removeFromCart(Scanner scanner, User user) {
        System.out.print("👉 Modellname: ");
        String model = scanner.nextLine();
        VariantProdukt p = findProduct(user, model);
        if (p != null) {
            user.getCart().removeFromCart(p);
        } else {
            System.out.println("❌ Produkt nicht gefunden.");
        }
    }

    private static void updateQuantity(Scanner scanner, User user) {
        System.out.print("👉 Modellname: ");
        String model = scanner.nextLine();
        VariantProdukt p = findProduct(user, model);
        if (p != null) {
            System.out.print("🔁 Neue Menge: ");
            int q = Integer.parseInt(scanner.nextLine());
            user.getCart().updateQuantity(p, q);
        } else {
            System.out.println("❌ Produkt nicht gefunden.");
        }
    }

    private static VariantProdukt findProduct(User user, String modelName) {
        return user.getCart().getCartItems().keySet()
                .stream()
                .filter(p -> p.getModel().equalsIgnoreCase(modelName))
                .findFirst()
                .orElse(null);
    }
}
