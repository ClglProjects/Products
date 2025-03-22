package Warenwirtschaft.produkte.Elektronik.Laptop;

import Warenwirtschaft.produkte.Elektronik.Handys.Produkt;

public class Laptops extends Produkt {
    private String processor;
    private double screenSize;
    private double batteryLife;

    public Laptops(String brandName, String model, int brandId, int categoryId,
                   String processor, double screenSize, double batteryLife,
                   double price, int stock) {

        super(brandName, model, brandId, categoryId, price, stock);
        this.processor = processor;
        this.screenSize = screenSize;
        this.batteryLife = batteryLife;
    }

    // Getter
    public String getProcessor() { return processor; }
    public double getScreenSize() { return screenSize; }
    public double getBatteryLife() { return batteryLife; }

    @Override
    public void showDetails() {
        System.out.printf("💻 %s %s | %s, %.1f\" | 🔋 %.1f Std. | 💰 %.2f€ | Lager: %d\n",
                getBrandName(), getModel(), processor, screenSize, batteryLife,
                getPrice(), getStock());
    }
}
