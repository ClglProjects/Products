package Warenwirtschaft.products.ui.util;

public class ProductOptions {

    public enum Farbe {
        SCHWARZ, WEISS, BLAU, ROT, GRUEN, GOLD, SILBER, VIOLETT, ORANGE
    }

    public enum Speicher {
        GB64("64GB"), GB128("128GB"), GB256("256GB"), GB512("512GB"), TB1("1TB");

        private final String label;
        Speicher(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public enum Verbindung {
        WIRED, WIRELESS
    }

    public enum RAM {
        GB4("4GB"), GB8("8GB"), GB16("16GB"), GB32("32GB");

        private final String label;
        RAM(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    // kannst du jederzeit erweitern, z.B. Bildschirmgrößen, Akkus, etc.
}
