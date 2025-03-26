// Warenwirtschaft/produkte/Elektronik/Laptop/Laptops.java
package Warenwirtschaft.products.categories.laptops;

import Warenwirtschaft.products.base.Produkt;
import Warenwirtschaft.products.model.VariantProdukt;

public class Laptops extends Produkt implements VariantProdukt {
    private String processor;
    private double screenSize;
    private final String batteryLife; // statt double

    private int ram;
    private String storage;
    private String color;



    public Laptops(String brandName, String model, int brandId, int categoryId,
                   String processor, double screenSize, String batteryLife,
                   int ram, String storage, String color,
                   double price, int stock){
        super(brandName, model, brandId, categoryId, price, stock);
        this.processor = processor;
        this.screenSize = screenSize;
        this.batteryLife = batteryLife;
        this.ram = ram;
        this.storage = storage;
        this.color = color;
    }


    // Getter
    public String getProcessor() { return processor; }
    public double getScreenSize() { return screenSize; }
    public String getBatteryLife() { return batteryLife; }
    public int getRam() { return ram; }
    public String getStorage() { return storage; }

    @Override
    public String getCategory() {
        return "laptop";
    }

    public String getColor() { return color; }



    @Override
    public String getDisplayInfo() {
        return String.format("%s %s | %s, %d GB RAM, %s, %s | %.1f\" | 🔋 %s | 💰 %.2f€ | Lager: %d",
                getBrandName(), getModel(), processor, ram, storage, color,
                screenSize, batteryLife, getPrice(), getStock());
    }



    @Override
    public void decreaseStockInDB() {
        new LaptopManager().decreaseStock(this);
    }

    @Override
    public void showDetails() {
        // Details anzeigen
    }
}
