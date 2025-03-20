package org.example.HandyMarken;

public class Handys extends HandyProdukte {
    private int stock;

    public Handys(String name, String model, String brand, String color, String storage, double price, int stock) {
        super(name, model, brand, color, storage, price);
        this.stock = stock;
    }

    public void zeigeBetriebssystem() {
        System.out.println(getName() + " läuft auf ");
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void decreaseStock() {
        if (stock > 0) {
            stock--;
            System.out.println("✅ Kauf erfolgreich! Neuer Bestand: " + stock);
        } else {
            System.out.println("❌ Nicht verfügbar! Dieses Gerät ist ausverkauft.");
        }
    }
}
