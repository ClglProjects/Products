package Warenwirtschaft.products.categories.mouse;

import Warenwirtschaft.products.base.Produkt;

public class Mouse extends Produkt {
    private int id;
    private String model;
    private int brandId;
    private int categoryId;
    private String brandName;
    private int dpi;
    private int buttons;
    private String color;
    private boolean rgb;
    private String connectionType;
    private String batteryLife;

    public Mouse(String brandName, String model, int brandId, int categoryId,
                 int dpi, int buttons, String color, boolean rgb,
                 String connectionType, String batteryLife,
                 double price, int stock) {
        super(brandName, model, brandId, categoryId, price, stock);
        this.dpi = dpi;
        this.buttons = buttons;
        this.color = color;
        this.rgb = rgb;
        this.connectionType = connectionType;
        this.batteryLife = batteryLife;
    }

    // Getter
    public int getDpi() { return dpi; }
    public int getButtons() { return buttons; }
    public String getColor() { return color; }
    public boolean isRgb() { return rgb; }
    public String getConnectionType() { return connectionType; }
    public String getBatteryLife() { return batteryLife; }

    @Override
    public void showDetails() {
        System.out.printf("%s %s (%s, %s) - %d DPI, %d Tasten, RGB: %s, Akku: %s, 💰 %.2f€ (Stock: %d)\n",
                getBrandName(), getModel(), color, connectionType, dpi, buttons,
                rgb ? "Ja" : "Nein", batteryLife, getPrice(), getStock());
    }

    @Override
    public void decreaseStockInDB() {
        new MouseManager().decreaseStock(this);
    }






}
