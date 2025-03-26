package Warenwirtschaft.products.ui.util;

import java.util.Scanner;

public class OptionSelector {

    public static <T extends Enum<T>> T chooseEnumOption(Class<T> enumClass, Scanner scanner, String beschreibung) {
        T[] werte = enumClass.getEnumConstants();

        System.out.println("\n🔘 Wähle " + beschreibung + ":");
        for (int i = 0; i < werte.length; i++) {
            System.out.println((i + 1) + "️⃣ " + formatEnumValue(werte[i]));
        }

        System.out.print("👉 Eingabe (1-" + werte.length + "): ");
        try {
            int eingabe = Integer.parseInt(scanner.nextLine());
            if (eingabe >= 1 && eingabe <= werte.length) {
                return werte[eingabe - 1];
            }
        } catch (Exception ignored) {}

        System.out.println("❌ Ungültige Auswahl.");
        return null;
    }

    private static <T extends Enum<T>> String formatEnumValue(T value) {
        if (value instanceof ProductOptions.Speicher s) return s.getLabel();
        if (value instanceof ProductOptions.RAM r) return r.getLabel();
        return value.name();
    }
}
