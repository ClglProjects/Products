package Warenwirtschaft.products.order;

import Warenwirtschaft.products.model.VariantProdukt;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final Map<VariantProdukt, Integer> cartItems = new HashMap<>();

    public void addToCart(VariantProdukt produkt) {
        cartItems.put(produkt, cartItems.getOrDefault(produkt, 0) + 1);
    }

    public void removeFromCart(VariantProdukt produkt) {
        if (cartItems.containsKey(produkt)) {
            cartItems.remove(produkt);
            System.out.println("🗑️ Produkt wurde aus dem Warenkorb entfernt.");
        } else {
            System.out.println("❌ Produkt ist nicht im Warenkorb.");
        }
    }

    public void updateQuantity(VariantProdukt produkt, int newQuantity) {
        if (cartItems.containsKey(produkt)) {
            if (newQuantity <= 0) {
                cartItems.remove(produkt);
                System.out.println("🚫 Produkt entfernt (Menge = 0).");
            } else {
                cartItems.put(produkt, newQuantity);
                System.out.println("🔁 Menge aktualisiert.");
            }
        } else {
            System.out.println("❌ Produkt ist nicht im Warenkorb.");
        }
    }

    public void showCart() {
        if (cartItems.isEmpty()) {
            System.out.println("🛒 Dein Warenkorb ist leer.");
            return;
        }

        System.out.println("\n🛒 Dein Warenkorb:");
        double gesamtPreis = 0;
        int position = 1;

        for (Map.Entry<VariantProdukt, Integer> entry : cartItems.entrySet()) {
            VariantProdukt produkt = entry.getKey();
            int menge = entry.getValue();
            double einzelpreis = produkt.getPrice();
            double gesamt = einzelpreis * menge;
            gesamtPreis += gesamt;

            System.out.printf("%d. %s\n", position++, produkt.getDisplayInfo());
            System.out.printf("   Menge: %d | Einzelpreis: %.2f€ | Gesamt: %.2f€\n\n", menge, einzelpreis, gesamt);
        }

        System.out.printf("💰 Gesamtpreis: %.2f€\n", gesamtPreis);
    }

    public void clearCart() {
        cartItems.clear();
        System.out.println("🧹 Dein Warenkorb wurde geleert.");
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public int getTotalItems() {
        return cartItems.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double getTotalPrice() {
        return cartItems.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    public Map<VariantProdukt, Integer> getCartItems() {
        return cartItems;
    }

    public void printCartSummary() {
        System.out.printf("🧾 Artikel im Warenkorb: %d | Gesamtpreis: %.2f€\n",
                getTotalItems(), getTotalPrice());
    }
}
