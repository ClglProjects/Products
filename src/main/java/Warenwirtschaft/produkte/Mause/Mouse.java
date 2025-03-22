package Warenwirtschaft.produkte.Mause;

public class Mouse {
    private int id;
    private String model;
    private int brandId;
    private int categoryId;
    private String brandName;
    private int dpi;
    private int buttons;

    public Mouse(int id, String model, int brandId, int categoryId, String brandName, int dpi, int buttons) {
        this.id = id;
        this.model = model;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.brandName = brandName;
        this.dpi = dpi;
        this.buttons = buttons;
    }

    // Getter
    public int getId() { return id; }
    public String getModel() { return model; }
    public int getBrandId() { return brandId; }
    public int getCategoryId() { return categoryId; }
    public String getBrandName() { return brandName; }
    public int getDpi() { return dpi; }
    public int getButtons() { return buttons; }
}
