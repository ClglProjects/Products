package Warenwirtschaft.products.base;

import java.util.List;

public abstract class ProduktManager<T> {

    // Diese Methode muss vorhanden sein:
    public abstract List<T> loadAllByBrandId(int brandId);

    // Optional: Wenn du diese Methoden auch nutzt:
    public abstract List<T> loadAllFromDB(int brandId);
    public abstract T findByModel(String modelName, int brandId);
}
