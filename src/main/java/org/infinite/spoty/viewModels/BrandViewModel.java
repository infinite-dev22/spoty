package org.infinite.spoty.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.BrandDao;
import org.infinite.spoty.database.models.Brand;

public class BrandViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    public static ObservableList<Brand> brandsList = FXCollections.observableArrayList();

    public static int getId () {
        return id.get();
    }
    public static void setId(int id) {
        BrandViewModel.id.set(id);
    }
    public static IntegerProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        BrandViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        BrandViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static void saveBrand() {
        Brand brand = new Brand(getName(), getDescription());
        BrandDao.saveBrand(brand);
        brandsList.clear();
        clearBrandData();
        getItems();
    }

    public static void clearBrandData() {
        setId(0);
        setName("");
        setDescription("");
    }

    public static ObservableList<Brand> getItems() {
        brandsList.clear();
        brandsList.addAll(BrandDao.getBrands());
        return brandsList;
    }

    public static void getItem(int brandID) {
        Brand brand = BrandDao.findBrand(brandID);
        setId(brand.getId());
        setName(brand.getName());
        setDescription(brand.getDescription());
        getItems();
    }

    public static void updateItem(int brandID) {
        Brand brand = new Brand(getName(), getDescription());
        BrandDao.updateBrand(brand, brandID);
        clearBrandData();
        getItems();
    }
}
