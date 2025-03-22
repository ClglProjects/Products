package Warenwirtschaft.products.ui;

import Warenwirtschaft.products.user.AuthSystem;

import Warenwirtschaft.products.user.User;
import Warenwirtschaft.products.model.VariantProdukt;
import Warenwirtschaft.products.service.ProductService;
import Warenwirtschaft.products.service.ShoppingService;
import java.util.*;

public class UserMenu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProductService productService = new ProductService();
    private static User loggedInUser = null;

    public static void showMainMenu() {
        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n📌 Willkommen im Handy-Shop!");
                System.out.println("1️⃣ Einloggen");
                System.out.println("2️⃣ Registrieren");
                System.out.println("3️⃣ Programm beenden");
                System.out.print("👉 Wähle eine Option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        loginUser();
                        break;
                    case "2":
                        registerUser();
                        break;
                    case "3":
                        System.out.println("👋 Programm wird beendet.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Ungültige Eingabe!");
                }
            } else {
                if (loggedInUser.isAdmin()) {
                    AdminMenu.showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
    }

    private static void showUserMenu() {
        while (true) {
            List<String> availableBrands = productService.getAllBrands();
            Collections.sort(availableBrands);

            System.out.println("\n📌 Willkommen zurück, " + loggedInUser.getUsername() + "!");
            System.out.println("🔍 Wähle eine Marke:");

            for (int i = 0; i < availableBrands.size(); i++) {
                System.out.println((i + 1) + "️⃣ " + availableBrands.get(i));
            }
            System.out.println((availableBrands.size() + 1) + "️⃣ Warenkorb anzeigen");
            System.out.println((availableBrands.size() + 2) + "️⃣ Abmelden");
            System.out.print("👉 Wähle eine Option: ");

            String choice = scanner.nextLine();
            int selectedOption;

            try {
                selectedOption = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("❌ Ungültige Eingabe! Bitte eine Zahl eingeben.");
                return;
            }

            if (selectedOption > 0 && selectedOption <= availableBrands.size()) {
                String selectedBrand = availableBrands.get(selectedOption - 1);
                showCategoryForBrand(selectedBrand);
            } else if (selectedOption == availableBrands.size() + 1) {
                loggedInUser.getCart().showCart();
            } else if (selectedOption == availableBrands.size() + 2) {
                loggedInUser = null;
                System.out.println("👋 Du hast dich erfolgreich abgemeldet.");
                return;
            } else {
                System.out.println("❌ Ungültige Eingabe!");
            }
        }
    }

    private static void showCategoryForBrand(String brand) {
        List<String> categories = productService.getCategoriesForBrand(brand);

        if (categories.isEmpty()) {
            System.out.println("❌ Für diese Marke sind derzeit keine Kategorien verfügbar.");
            return;
        }

        System.out.println("\n📌 Du hast '" + brand + "' ausgewählt!");
        System.out.println("🔍 Wähle eine Produktkategorie:");

        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + "️⃣ " + categories.get(i));
        }
        System.out.println((categories.size() + 1) + "️⃣ Zurück zur Markenübersicht");
        System.out.print("👉 Wähle eine Option: ");

        int selectedOption = Integer.parseInt(scanner.nextLine());
        if (selectedOption > 0 && selectedOption <= categories.size()) {
            String selectedCategory = categories.get(selectedOption - 1);
            showProductsForCategory(brand, selectedCategory);
        }
    }

    private static void showProductsForCategory(String brand, String category) {
        while (true) {
            List<String> products = productService.getProductsForCategory(brand, category);
            if (products.isEmpty()) {
                System.out.println("❌ In dieser Kategorie sind derzeit keine Produkte verfügbar.");
                return;
            }

            System.out.println("\n📌 Du hast '" + category + "' für '" + brand + "' ausgewählt!");
            System.out.println("🔍 Wähle ein Produkt:");

            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + "️⃣ " + products.get(i));
            }
            System.out.println((products.size() + 1) + "️⃣ Zurück zur Kategorieübersicht");
            System.out.print("👉 Wähle eine Option: ");

            int selectedOption = Integer.parseInt(scanner.nextLine());
            if (selectedOption > 0 && selectedOption <= products.size()) {
                String selectedProduct = products.get(selectedOption - 1);
                handleDeviceSelection(selectedProduct, brand, category);
            } else if (selectedOption == products.size() + 1) {
                return;
            }
        }
    }


    private static void loginUser() {
        System.out.print("👉 Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("👉 Passwort: ");
        String password = scanner.nextLine();

        loggedInUser = AuthSystem.login(username, password);

        if (loggedInUser != null) {
            System.out.println("✅ Login erfolgreich.");
        } else {
            System.out.println("❌ Benutzername oder Passwort falsch.");
        }
    }

    private static void registerUser() {
        System.out.print("👉 Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("👉 Passwort: ");
        String password = scanner.nextLine();
        System.out.print("👉 E-Mail: ");
        String email = scanner.nextLine();
        System.out.print("👉 Telefonnummer (optional): ");
        String phone = scanner.nextLine();

        boolean success = AuthSystem.register(username, password, email, phone);

        if (success) {
            System.out.println("✅ Registrierung erfolgreich!");
            loggedInUser = AuthSystem.login(username, password);
        } else {
            System.out.println("❌ Benutzername oder E-Mail existiert bereits!");
        }
    }

    private static void handleDeviceSelection(String model, String brand, String category) {
        int brandId = productService.getBrandId(brand);

        List<VariantProdukt> varianten = new ArrayList<>(productService.getVariantsForModelAndCategory(model, brandId, category));

        ShoppingService<VariantProdukt> service = new ShoppingService<>(varianten);
        VariantProdukt ausgewählt = service.chooseVariantFromList(scanner);

        if (ausgewählt != null) {
            service.addToCartIfConfirmed(ausgewählt, loggedInUser, scanner);
        }
    }


}
