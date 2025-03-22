package Warenwirtschaft.produkte.Elektronik.Handys;

public abstract class Produkt {
    private String brandName;
    private String model;
    private int brandId;
    private int categoryId;
    private double price;
    private int stock;

    public Produkt(String brandName, String model, int brandId, int categoryId, double price, int stock) {
        this.brandName = brandName;
        this.model = model;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.price = price;
        this.stock = stock;
    }

    // ✅ Getter & Setter
    public String getBrandName() { return brandName; }
    public String getModel() { return model; }
    public int getBrandId() { return brandId; }
    public int getCategoryId() { return categoryId; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }

    // ✅ Abstrakte Methode für individuelle Details je Produkt-Typ
    public abstract void showDetails();
}
