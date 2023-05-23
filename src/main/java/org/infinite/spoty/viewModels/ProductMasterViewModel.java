package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ProductMasterDao;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.database.models.ProductMaster;

public class ProductMasterViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty barcodeType = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    private static final DoubleProperty price = new SimpleDoubleProperty(0);
    private static final StringProperty note = new SimpleStringProperty("");
    private static final BooleanProperty notForSale = new SimpleBooleanProperty(false);
    private static final BooleanProperty isActive = new SimpleBooleanProperty(true);
    private static final ObjectProperty<ProductCategory> category = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Brand> brand = new SimpleObjectProperty<>(null);
    public static final ObservableList<ProductMaster> productMasterList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        ProductMasterViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static String getBarcodeType() {
        return barcodeType.get();
    }

    public static void setBarcodeType(String barcodeType) {
        ProductMasterViewModel.barcodeType.set(barcodeType);
    }

    public static StringProperty barcodeTypeProperty() {
        return barcodeType;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ProductMasterViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static double getPrice() {
        return price.get();
    }

    public static void setPrice(double price) {
        ProductMasterViewModel.price.set(price);
    }

    public static DoubleProperty priceProperty() {
        return price;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        ProductMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static boolean isNotForSale() {
        return notForSale.get();
    }

    public static void setNotForSale(boolean notForSale) {
        ProductMasterViewModel.notForSale.set(notForSale);
    }

    public static BooleanProperty notForSaleProperty() {
        return notForSale;
    }

    public static boolean isIsActive() {
        return isActive.get();
    }

    public static void setIsActive(boolean isActive) {
        ProductMasterViewModel.isActive.set(isActive);
    }

    public static BooleanProperty isActiveProperty() {
        return isActive;
    }

    public static ProductCategory getCategory() {
        return category.get();
    }

    public static void setCategory(ProductCategory category) {
        ProductMasterViewModel.category.set(category);
    }

    public static ObjectProperty<ProductCategory> categoryProperty() {
        return category;
    }

    public static Brand getBrand() {
        return brand.get();
    }

    public static void setBrand(Brand brand) {
        ProductMasterViewModel.brand.set(brand);
    }

    public static ObjectProperty<Brand> brandProperty() {
        return brand;
    }

    public static void resetProperties() {
        setId(0);
        setBarcodeType("");
        setName("");
        setPrice(0);
        setNote("");
        setNotForSale(false);
        setIsActive(true);
        setCategory(null);
        setBrand(null);
    }

    public static void save() {
        ProductMaster productMaster = new ProductMaster(getBarcodeType(), getName(),getPrice(), getCategory(),
                getBrand(), getNote(), isNotForSale(), isIsActive());
        ProductMasterDao.saveProductMaster(productMaster);
        resetProperties();
        getProductMasters();
    }

    public static ObservableList<ProductMaster> getProductMasters() {
        productMasterList.clear();
        productMasterList.addAll(ProductMasterDao.getProductMaster());
        return productMasterList;
    }
}
