package org.example.HandyMarken;

import java.util.ArrayList;

public class Warenkorb {
    private ArrayList<Handys> cart; // Liste mit Handys im Warenkorb

    public Warenkorb() {
        this.cart = new ArrayList<>();
    }

    // 📌 Gerät zum Warenkorb hinzufügen
    public void addToCart(Handys handy) {
        if (handy != null) {
            cart.add(handy);
            System.out.println("🛒 " + handy.getName() + " " + handy.getModel() + " wurde zum Warenkorb hinzugefügt!");
        } else {
            System.out.println("⚠️ Kein gültiges Gerät ausgewählt!");
        }
    }

    // 📌 Alle Geräte im Warenkorb anzeigen
    public void showCart() {
        if (cart.isEmpty()) {
            System.out.println("🛒 Dein Warenkorb ist leer.");
            return;
        }

        System.out.println("\n🛍 Dein Warenkorb:");
        double total = 0;

        for (Handys handy : cart) {
            System.out.println("📌 " + handy.getName() + " " + handy.getModel() + " | 💰 " + handy.getPrice() + "€");
            total += handy.getPrice();
        }

        System.out.println("💰 Gesamtpreis: " + total + "€");
    }


    public void removeFromCart(String model) {
        for (Handys handy : cart) {
            if (handy.getModel().equalsIgnoreCase(model)) {
                cart.remove(handy);
                System.out.println("❌ " + handy.getName() + " " + handy.getModel() + " wurde entfernt.");
                return;
            }
        }
        System.out.println("⚠️ Gerät nicht im Warenkorb gefunden.");
    }


    public void checkout() {
        if (cart.isEmpty()) {
            System.out.println("🛒 Dein Warenkorb ist leer. Kein Kauf nötig.");
            return;
        }

        double total = 0;
        System.out.println("\n✅ Kauf abgeschlossen! Folgende Geräte wurden gekauft:");
        for (Handys handy : cart) {
            System.out.println("📌 " + handy.getName() + " " + handy.getModel() + " | 💰 " + handy.getPrice() + "€");
            total += handy.getPrice();
        }

        System.out.println("💰 Gesamtpreis: " + total + "€");
        cart.clear(); // Alle gekauften Geräte aus dem Warenkorb entfernen
        System.out.println("🛍 Dein Warenkorb wurde geleert.");
    }
}
