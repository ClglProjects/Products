package Warenwirtschaft.products.model;

public interface VariantProdukt {
    String getBrandName();
    String getModel();
    double getPrice();
    int getStock();
    String getColor();     // ← HINZUGEFÜGT
    String getStorage();   // ← HINZUGEFÜGT
    String getCategory();  // ← HINZUGEFÜGT
    String getDisplayInfo();

    void decreaseStockInDB();
}
