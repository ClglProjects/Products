package Warenwirtschaft.products.model;

public interface VariantProdukt {

    String getModel();              // z. B. "iPhone 15"
    String getBrandName();         // z. B. "Apple"
    double getPrice();             // Preis für Berechnungen
    int getStock();                // Verfügbarer Lagerbestand
    String getDisplayInfo();       // Formatierte Info für Anzeige
    void decreaseStockInDB();      // Reduziert den Bestand in der DB
}
