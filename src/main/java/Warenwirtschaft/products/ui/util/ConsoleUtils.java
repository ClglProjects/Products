package Warenwirtschaft.products.ui.util;

import java.util.Scanner;

public class ConsoleUtils {

    public static int safeIntInput(Scanner scanner, int min, int max) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.out.print("❌ Bitte gib eine Zahl zwischen " + min + " und " + max + " ein: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("❌ Ungültige Eingabe! Bitte eine Zahl eingeben: ");
            }
        }
    }
}
