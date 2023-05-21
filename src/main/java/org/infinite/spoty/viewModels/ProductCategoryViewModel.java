package org.infinite.spoty.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ProductCategoryDao;
import org.infinite.spoty.database.models.ProductCategory;

public class ProductCategoryViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty code = new SimpleStringProperty("");
    private static final StringProperty title = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    public static ObservableList<ProductCategory> categoriesList = FXCollections.observableArrayList();

    public static Integer getId() {
        return id.get();
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static void setId(Integer id) {
        ProductCategoryViewModel.id.set(id);
    }

    public static String getTitle() {
        return title.get();
    }

    public static StringProperty titleProperty() {
        return title;
    }

    public static void setTitle(String title) {
        ProductCategoryViewModel.title.set(title);
    }

    public static String getCode() {
        return code.get();
    }

    public static StringProperty codeProperty() {
        return code;
    }

    public static void setCode(String code) {
        ProductCategoryViewModel.code.set(code);
    }

    public static String getName() {
        return name.get();
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static void setName(String name) {
        ProductCategoryViewModel.name.set(name);
    }

    public static void saveProductCategory() {
        ProductCategory productCategory = new ProductCategory(getCode(), getName());
        ProductCategoryDao.saveProductCategory(productCategory);
        categoriesList.clear();
        clearProductCategoryData();
        getItems();
    }

    public static void clearProductCategoryData() {
        setCode("");
        setName("");
    }

    public static ObservableList<ProductCategory> getItems() {
        categoriesList.addAll(ProductCategoryDao.getProductCategories());
        return categoriesList;
    }
}
