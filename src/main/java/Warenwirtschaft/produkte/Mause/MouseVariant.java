package Warenwirtschaft.produkte.Mause;

public class MouseVariant {
    private int id;
    private int mouseId;
    private String color;
    private boolean rgb;
    private String connectionType;
    private String batteryLife;
    private double price;
    private int stock;

    public MouseVariant(int id, int mouseId, String color, boolean rgb, String connectionType, String batteryLife, double price, int stock) {
        this.id = id;
        this.mouseId = mouseId;
        this.color = color;
        this.rgb = rgb;
        this.connectionType = connectionType;
        this.batteryLife = batteryLife;
        this.price = price;
        this.stock = stock;
    }

    // Getter
    public int getId() { return id; }
    public int getMouseId() { return mouseId; }
    public String getColor() { return color; }
    public boolean hasRGB() { return rgb; }
    public String getConnectionType() { return connectionType; }
    public String getBatteryLife() { return batteryLife; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
}
