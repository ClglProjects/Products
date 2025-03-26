package Warenwirtschaft.products.ui.customer;

import Warenwirtschaft.products.model.VariantProdukt;
import Warenwirtschaft.products.service.ProductService;
import Warenwirtschaft.products.service.ShoppingService;
import Warenwirtschaft.products.user.User;
import Warenwirtschaft.products.ui.util.ConsoleUtils;

import java.util.*;

public class UserShopNavigator {
    private static final ProductService productService = new ProductService();

    public static void showCategoryFirstMenu(Scanner scanner, User user) {
        List<String> categories = productService.getAllCategories();
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d️⃣ %s\n", i + 1, categories.get(i));
        }
        System.out.printf("%d️⃣ Zurück\n", categories.size() + 1);
        System.out.print("👉 Auswahl: ");
        int input = ConsoleUtils.safeIntInput(scanner, 1, categories.size() + 1);
        if (input <= categories.size()) {
            showBrandsForCategory(scanner, user, categories.get(input - 1));
        }
    }

    public static void showBrandsForCategory(Scanner scanner, User user, String category) {
        List<String> brands = productService.getBrandsForCategory(category);
        for (int i = 0; i < brands.size(); i++) {
            System.out.printf("%d️⃣ %s\n", i + 1, brands.get(i));
        }
        System.out.printf("%d️⃣ Zurück\n", brands.size() + 1);
        System.out.print("👉 Auswahl: ");
        int input = ConsoleUtils.safeIntInput(scanner, 1, brands.size() + 1);
        if (input <= brands.size()) {
            showProductsForCategory(brands.get(input - 1), category, scanner, user);
        }
    }

    public static void showBrandShop(Scanner scanner, User user) {
        List<String> brands = productService.getAllBrands();
        Collections.sort(brands);
        for (int i = 0; i < brands.size(); i++) {
            System.out.printf("%d️⃣ %s\n", i + 1, brands.get(i));
        }
        System.out.print("👉 Marke auswählen: ");
        int input = ConsoleUtils.safeIntInput(scanner, 1, brands.size());
        String brand = brands.get(input - 1);

        List<String> categories = productService.getCategoriesForBrand(brand);
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d️⃣ %s\n", i + 1, categories.get(i));
        }
        System.out.print("👉 Kategorie auswählen: ");
        int catInput = ConsoleUtils.safeIntInput(scanner, 1, categories.size());
        showProductsForCategory(brand, categories.get(catInput - 1), scanner, user);
    }

    public static void showProductsForCategory(String brand, String category, Scanner scanner, User user) {
        List<String> products = productService.getProductsForCategory(brand, category);
        for (int i = 0; i < products.size(); i++) {
            System.out.printf("%d️⃣ %s\n", i + 1, products.get(i));
        }
        System.out.print("👉 Produkt wählen: ");
        int index = ConsoleUtils.safeIntInput(scanner, 1, products.size());
        String model = products.get(index - 1);
        handleDeviceSelection(model, brand, category, scanner, user);
    }

    public static void handleDeviceSelection(String model, String brand, String category, Scanner scanner, User user) {
        int brandId = productService.getBrandId(brand);

        // Sicherer Cast: neue Liste mit allen Varianten
        List<VariantProdukt> varianten = new ArrayList<>(productService.getVariantsForModelAndCategory(model, brandId, category));

        ShoppingService<VariantProdukt> service = new ShoppingService<>(varianten);
        VariantProdukt selected = service.chooseVariantFromList(scanner);

        if (selected != null) {
            service.addToCartIfConfirmed(selected, user, scanner);
        }
    }

}
