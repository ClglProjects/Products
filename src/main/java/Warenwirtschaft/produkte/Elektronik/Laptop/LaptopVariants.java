package Warenwirtschaft.produkte.Elektronik.Laptop;

public class LaptopVariants {
    private int laptopId;
    private int ram;
    private String color;
    private double price;
    private int stock;
    private String storage;
    private String brandName;

    public LaptopVariants(int laptopId, int ram, String color, double price, int stock, String storage, String brandName) {
        this.laptopId = laptopId;
        this.ram = ram;
        this.color = color;
        this.price = price;
        this.stock = stock;
        this.storage = storage;
        this.brandName = brandName;
    }

    // Getter und Setter
    public int getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(int laptopId) {
        this.laptopId = laptopId;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
