package Warenwirtschaft.produkte.Shop;

import Warenwirtschaft.produkte.Elektronik.Handys.Handys;
import java.util.HashMap;
import java.util.Map;


public class Cart {
    private final Map<Handys, Integer> cartItems = new HashMap<>();

    /** ✅ **Fügt ein Handy zum Warenkorb hinzu. Falls es bereits existiert, wird die Menge erhöht.** */
    public void addToCart(Handys handy) {
        cartItems.put(handy, cartItems.getOrDefault(handy, 0) + 1);
        System.out.println("➕ " + handy.getBrandName() + " " + handy.getModel() + " wurde dem Warenkorb hinzugefügt.");
    }

    /** ✅ **Zeigt alle Geräte im Warenkorb mit Menge & Gesamtpreis an.** */
    public void showCart() {
        if (cartItems.isEmpty()) {
            System.out.println("🛒 Dein Warenkorb ist leer.");
            return;
        }

        System.out.println("\n🛍 Dein Warenkorb:");
        double gesamtPreis = 0;

        for (Map.Entry<Handys, Integer> entry : cartItems.entrySet()) {
            Handys handy = entry.getKey();
            int anzahl = entry.getValue();
            double gesamtPreisProArtikel = handy.getPrice() * anzahl;
            gesamtPreis += gesamtPreisProArtikel;

            // ✅ Markennamen + Produktname werden korrekt angezeigt!
            System.out.printf("📌 %s %s (%s, %s) | 💰 %.2f€ x %d = %.2f€%n",
                    handy.getBrandName(), handy.getModel(), handy.getColor(), handy.getStorage(),
                    handy.getPrice(), anzahl, gesamtPreisProArtikel);
        }

        System.out.printf("\n💰 **Gesamtpreis: %.2f€**%n", gesamtPreis);
    }

    /** ✅ **Entfernt ein Gerät (nach Modell) aus dem Warenkorb.** */
    public void removeFromCart(String model) {
        Handys foundHandy = null;
        for (Handys handy : cartItems.keySet()) {
            if (handy.getModel().equalsIgnoreCase(model)) {
                foundHandy = handy;
                break;
            }
        }

        if (foundHandy != null) {
            if (cartItems.get(foundHandy) > 1) {
                cartItems.put(foundHandy, cartItems.get(foundHandy) - 1);
            } else {
                cartItems.remove(foundHandy);
            }
            System.out.println("❌ " + foundHandy.getBrandName() + " " + foundHandy.getModel() + " wurde entfernt.");
        } else {
            System.out.println("⚠️ Gerät nicht im Warenkorb gefunden.");
        }
    }

    /** ✅ **Schließt den Kauf ab und leert den Warenkorb.** */
    public void checkout() {
        if (cartItems.isEmpty()) {
            System.out.println("🛒 Dein Warenkorb ist leer. Kein Kauf nötig.");
            return;
        }

        double total = 0;
        System.out.println("\n✅ Kauf abgeschlossen! Folgende Geräte wurden gekauft:");
        for (Map.Entry<Handys, Integer> entry : cartItems.entrySet()) {
            Handys handy = entry.getKey();
            int anzahl = entry.getValue();
            double gesamtPreisProArtikel = handy.getPrice() * anzahl;
            total += gesamtPreisProArtikel;

            System.out.printf("📌 %s %s (%s, %s) | 💰 %.2f€ x %d = %.2f€%n",
                    handy.getBrandName(), handy.getModel(), handy.getColor(), handy.getStorage(),
                    handy.getPrice(), anzahl, gesamtPreisProArtikel);
        }

        System.out.printf("💰 **Gesamtpreis: %.2f€**%n", total);
        cartItems.clear(); // Warenkorb leeren
        System.out.println("🛍 Dein Warenkorb wurde geleert.");
    }

    /** ✅ **Löscht den gesamten Warenkorb ohne Kauf.** */
    public void clearCart() {
        cartItems.clear();
        System.out.println("🛒 Dein Warenkorb wurde geleert.");
    }

    /** ✅ **Aktualisiert die Menge eines Geräts im Warenkorb oder entfernt es, falls Menge = 0.** */
    public void updateQuantity(Handys handy, int newQuantity) {
        if (cartItems.containsKey(handy)) {
            if (newQuantity > 0) {
                cartItems.put(handy, newQuantity);
                System.out.println("🔄 Menge von " + handy.getBrandName() + " " + handy.getModel() + " wurde auf " + newQuantity + " aktualisiert.");
            } else {
                cartItems.remove(handy);
                System.out.println("🗑 " + handy.getBrandName() + " " + handy.getModel() + " wurde aus dem Warenkorb entfernt.");
            }
        } else {
            System.out.println("❌ Dieses Gerät ist nicht im Warenkorb.");
        }
    }
}
