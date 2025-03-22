package Warenwirtschaft.products.service;

import Warenwirtschaft.products.categories.phones.HandyManager;
import Warenwirtschaft.products.categories.laptops.LaptopManager;
import Warenwirtschaft.products.categories.mouse.MouseManager;
import Warenwirtschaft.products.model.VariantProdukt;
import Warenwirtschaft.products.repository.ProductRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();

    public List<String> getAllBrands() {
        return productRepository.fetchAvailableBrands();
    }

    public List<String> getCategoriesForBrand(String brand) {
        return productRepository.fetchCategoriesForBrand(brand);
    }

    public List<String> getProductsForCategory(String brand, String category) {
        return productRepository.fetchProductsForCategory(brand, category);
    }

    public int getBrandId(String brandName) {
        return productRepository.getBrandIdByName(brandName);
    }

    public List<? extends VariantProdukt> getVariantsForModelAndCategory(String model, int brandId, String category) {
        return switch (category.toLowerCase()) {
            case "smartphone", "handy", "handys" -> new HandyManager().getModelVariants(model, brandId);
            case "laptop", "laptops" -> new LaptopManager().getModelVariants(model, brandId);
            case "maus", "mouse", "mouses", "mäuse" -> new MouseManager().getModelVariants(model, brandId);
            default -> List.of(); // Leere Liste bei unbekannter Kategorie
        };
    }

}
