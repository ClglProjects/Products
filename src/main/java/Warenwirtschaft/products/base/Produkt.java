package Warenwirtschaft.products.base;

import Warenwirtschaft.products.model.VariantProdukt;

public abstract class Produkt implements VariantProdukt {

    private final String brandName;
    private final String model;
    private final int brandId;
    private final int categoryId;
    private final double price;
    private final int stock;

    public Produkt(String brandName, String model, int brandId, int categoryId, double price, int stock) {
        this.brandName = brandName;
        this.model = model;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.price = price;
        this.stock = stock;
    }

    // Getter
    @Override
    public String getBrandName() { return brandName; }

    @Override
    public String getModel() { return model; }

    public int getBrandId() { return brandId; }

    public int getCategoryId() { return categoryId; }

    @Override
    public double getPrice() { return price; }

    @Override
    public int getStock() { return stock; }

    // Optional: eine Standardanzeige (kann überschrieben werden)
    @Override
    public String getDisplayInfo() {
        return String.format("%s %s | 💰 %.2f€ | Bestand: %d",
                brandName, model, price, stock);
    }

    // Diese Methoden müssen in der Unterklasse implementiert werden
    @Override
    public abstract void decreaseStockInDB();

    public abstract void showDetails();
}
