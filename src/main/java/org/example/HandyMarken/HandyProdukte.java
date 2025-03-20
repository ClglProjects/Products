package org.example.HandyMarken;

public class HandyProdukte {
    private String name;
    private String model;
    private String brand;
    private String color;          // Neue Eigenschaft: Farbe
    private String storage;        // Neue Eigenschaft: Speichergröße
    private double price;

    public HandyProdukte(String name, String model, String brand, String color, String storage, double price) {
        this.name = name;
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.storage = storage;
        this.price = price;
    }

    public void getDetails(){
        System.out.println(name + " " + model + " " + brand + " " + color + " " + storage + " " + price + "€");
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
