package org.example.HandyMarken;

public class HandyProdukte {
    private String name;
    private String model;
    private String brand;
    private double price;

    public HandyProdukte(String thename, String themodel, String thebrand, double theprice) {
        this.name = thename;
        this.model = themodel;
        this.brand = thebrand;
        this.price = theprice;
    }
    public void getDetails(){
        System.out.println(name+" "+model+" "+brand+" "+price);
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


