// Warenwirtschaft/produkte/Elektronik/Handys/Handys.java
package Warenwirtschaft.products.categories.phones;

import Warenwirtschaft.products.base.Produkt;
import Warenwirtschaft.products.model.VariantProdukt;

public class Handys extends Produkt implements VariantProdukt {
    private String color;
    private String storage;

    public Handys(String brandName, String model, int brandId, int categoryId,
                  String color, String storage, double price, int stock) {
        super(brandName, model, brandId, categoryId, price, stock);
        this.color = color;
        this.storage = storage;
    }

    // Getter
    public String getColor() { return color; }
    public String getStorage() { return storage; }

    // `VariantProdukt` Methoden
    @Override
    public String getModel() {
        return super.getModel();
    }

    @Override
    public String getDisplayInfo() {
        return String.format("%s %s (%s, %s) | 💰 %.2f€ | Bestand: %d",
                getBrandName(), getModel(), color, storage, getPrice(), getStock());
    }

    @Override
    public void decreaseStockInDB() {
        // Hier rufst du die Methode aus dem HandyManager auf, um den Lagerbestand zu verringern.
        new HandyManager().decreaseStock(this);
    }

    @Override
    public void showDetails() {
        // Details anzeigen
    }
}
