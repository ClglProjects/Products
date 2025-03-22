// Warenwirtschaft/produkte/service/ShoppingService.java
package Warenwirtschaft.products.service;

import Warenwirtschaft.products.user.User;
import Warenwirtschaft.products.model.VariantProdukt;
import Warenwirtschaft.products.utils.ConsoleUtils;

import java.util.List;
import java.util.Scanner;

public class ShoppingService<T extends VariantProdukt> {

    private final List<T> varianten;

    public ShoppingService(List<T> varianten) {
        this.varianten = varianten;
    }

    public T chooseVariantFromList(Scanner scanner) {
        System.out.println("\n📱 Verfügbare Varianten:");
        for (int i = 0; i < varianten.size(); i++) {
            System.out.printf("%d️⃣ %s\n", i + 1, varianten.get(i).getDisplayInfo());
        }

        System.out.println((varianten.size() + 1) + "️⃣ Zurück");
        System.out.print("👉 Wähle eine Variante: ");

        int auswahl = ConsoleUtils.safeIntInput(scanner, 1, varianten.size() + 1);

        if (auswahl == varianten.size() + 1) return null;
        return varianten.get(auswahl - 1);
    }

    public void addToCartIfConfirmed(T produkt, User user, Scanner scanner) {
        System.out.print("👉 Möchtest du dieses Modell kaufen? (ja/nein): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.matches("ja|j|yes|y")) {
            produkt.decreaseStockInDB();
            user.getCart().addToCart(produkt);
            System.out.println("✅ Produkt wurde zum Warenkorb hinzugefügt!");
        } else {
            System.out.println("❌ Kauf abgebrochen.");
        }
    }
}
