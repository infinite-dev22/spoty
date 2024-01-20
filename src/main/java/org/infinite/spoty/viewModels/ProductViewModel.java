/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.Brand;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.ProductCategory;
import org.infinite.spoty.data_source.dtos.UnitOfMeasure;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.ProductsRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static org.infinite.spoty.values.SharedResources.setTempId;

public class ProductViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    public static final ObservableList<Product> productsList = FXCollections.observableArrayList();
    private static final ListProperty<Product> products = new SimpleListProperty<>(productsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Brand> brand = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<ProductCategory> category = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> unit = new SimpleObjectProperty<>(null);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty productType = new SimpleStringProperty("");
    private static final StringProperty barcodeType = new SimpleStringProperty("");
    private static final StringProperty image = new SimpleStringProperty("");
    private static final LongProperty quantity = new SimpleLongProperty(0);
    private static final DoubleProperty cost = new SimpleDoubleProperty(0);
    private static final StringProperty price = new SimpleStringProperty();
    private static final StringProperty discount = new SimpleStringProperty();
    private static final StringProperty netTax = new SimpleStringProperty();
    private static final StringProperty taxType = new SimpleStringProperty("");
    private static final StringProperty stockAlert = new SimpleStringProperty();
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");

    public static @NotNull Long getId() {
        return id.get();
    }

    public static void setId(long id) {
        ProductViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static UnitOfMeasure getUnit() {
        return unit.get();
    }

    public static void setUnit(UnitOfMeasure unit) {
        ProductViewModel.unit.set(unit);
    }

    public static ObjectProperty<UnitOfMeasure> unitProperty() {
        return unit;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ProductViewModel.name.set(name != null ? name : "");
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static long getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(long quantity) {
        ProductViewModel.quantity.set(quantity);
    }

    public static LongProperty quantityProperty() {
        return quantity;
    }

    public static double getCost() {
        return cost.get();
    }

    public static void setCost(double cost) {
        ProductViewModel.cost.set(cost);
    }

    public static DoubleProperty costProperty() {
        return cost;
    }

    public static double getPrice() {
        return (Objects.equals(price.get(), null) || price.get().isEmpty())
                ? 0.0
                : Double.parseDouble(price.get());
    }

    public static void setPrice(double price) {
        ProductViewModel.price.set((price == 0) ? "" : String.valueOf(price));
    }

    public static StringProperty priceProperty() {
        return price;
    }

    public static double getDiscount() {
        return (Objects.equals(discount.get(), null) || discount.get().isEmpty())
                ? 0.0
                : Double.parseDouble(discount.get());
    }

    public static void setDiscount(double discount) {
        ProductViewModel.discount.set((discount == 0) ? "" : String.valueOf(discount));
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static double getNetTax() {
        return (Objects.equals(netTax.get(), null) || netTax.get().isEmpty())
                ? 0.0
                : Double.parseDouble(netTax.get());
    }

    public static void setNetTax(double netTax) {
        ProductViewModel.netTax.set((netTax == 0) ? "" : String.valueOf(netTax));
    }

    public static StringProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        ProductViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static long getStockAlert() {
        return (Objects.equals(stockAlert.get(), null) || stockAlert.get().isEmpty())
                ? 0
                : Long.parseLong(stockAlert.get());
    }

    public static void setStockAlert(long stockAlert) {
        ProductViewModel.stockAlert.set((stockAlert == 0) ? "" : String.valueOf(stockAlert));
    }

    public static StringProperty stockAlertProperty() {
        return stockAlert;
    }

    public static String getSerialNumber() {
        return serial.get();
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static ObservableList<Product> getProducts() {
        return products.get();
    }

    public static void setProducts(ObservableList<Product> products) {
        ProductViewModel.products.set(products);
    }

    public static Brand getBrand() {
        return brand.get();
    }

    public static void setBrand(Brand brand) {
        ProductViewModel.brand.set(brand);
    }

    public static ObjectProperty<Brand> brandProperty() {
        return brand;
    }

    public static ProductCategory getCategory() {
        return category.get();
    }

    public static void setCategory(ProductCategory category) {
        ProductViewModel.category.set(category);
    }

    public static ObjectProperty<ProductCategory> categoryProperty() {
        return category;
    }

    public static String getProductType() {
        return productType.get();
    }

    public static void setProductType(String productType) {
        ProductViewModel.productType.set(productType);
    }

    public static StringProperty productTypeProperty() {
        return productType;
    }

    public static String getBarcodeType() {
        return barcodeType.get();
    }

    public static void setBarcodeType(String barcodeType) {
        ProductViewModel.barcodeType.set(barcodeType);
    }

    public static StringProperty barcodeTypeProperty() {
        return barcodeType;
    }

    public static String getImage() {
        return image.get();
    }

    public static void setImage(String image) {
        ProductViewModel.image.set(image);
    }

    public static StringProperty imageProperty() {
        return image;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        ProductViewModel.serial.set(serial);
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        ProductViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static ListProperty<Product> productsProperty() {
        return products;
    }

    public static void saveProduct() throws IOException, InterruptedException {
        var productsRepository = new ProductsRepositoryImpl();
        var product = Product.builder()
                .name(getName())
                .brand(getBrand())
                .category(getCategory())
                .unit(getUnit())
                .productType(getProductType())
                .barcodeType(getBarcodeType())
                .price(getPrice())
                .cost(getCost())
                .quantity(getQuantity())
                .netTax(getNetTax())
                .discount(getDiscount())
                .taxType(getTaxType())
                .stockAlert(getStockAlert())
                .serialNumber(getSerialNumber())
                .image(getImage())
                .build();

        productsRepository.post(product);
        resetProperties();

        getAllProducts();
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0);
                    setTempId(-1);
                    setBrand(null);
                    setCategory(null);
                    setUnit(null);
                    setName("");
                    setProductType("");
                    setBarcodeType("");
                    setImage("");
                    setQuantity(0);
                    setCost(0);
                    setPrice(0);
                    setNetTax(0);
                    setDiscount(0);
                    setTaxType("");
                    setStockAlert(0);
                    setSerial("");
                    setTempId(-1);
                });
    }

    public static void getAllProducts() {
        var productsRepository = new ProductsRepositoryImpl();
        Type listType = new TypeToken<ArrayList<Product>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    productsList.clear();

                    try {
                        ArrayList<Product> productList = gson.fromJson(productsRepository.fetchAll().body(), listType);
                        productsList.addAll(productList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, ProductViewModel.class);
                    }
                });
    }

    public static void getProduct(Long productID) throws IOException, InterruptedException {
        var productsRepository = new ProductsRepositoryImpl();
        var findModel = new FindModel();
        findModel.setId(productID);
        var response = productsRepository.fetch(findModel).body();
        var product = gson.fromJson(response, Product.class);

        setId(product.getId());
        setName(product.getName());
        setBrand(product.getBrand());
        setCategory(product.getCategory());
        setUnit(product.getUnit());
        setProductType(product.getProductType());
        setBarcodeType(product.getBarcodeType());
        setPrice(product.getPrice());
        setCost(product.getCost());
        setQuantity(product.getQuantity());
        setDiscount(product.getDiscount());
        setNetTax(product.getNetTax());
        setTaxType(product.getTaxType());
        setStockAlert(product.getStockAlert());
        setSerial(product.getSerialNumber());
        setImage(product.getImage());
    }

    public static void searchItem(String search) {
        var productsRepository = new ProductsRepositoryImpl();
        var searchModel = new SearchModel();
        searchModel.setSearch(search);
        Type listType = new TypeToken<ArrayList<Product>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    productsList.clear();

                    try {
                        ArrayList<Product> productList = gson.fromJson(productsRepository.search(searchModel)
                                .body(), listType);
                        productsList.addAll(productList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, ProductViewModel.class);
                    }
                });
    }

    public static void updateProduct() throws IOException, InterruptedException {
        var productsRepository = new ProductsRepositoryImpl();
        var product = Product.builder()
                .id(getId())
                .name(getName())
                .brand(getBrand())
                .category(getCategory())
                .unit(getUnit())
                .productType(getProductType())
                .barcodeType(getBarcodeType())
                .price(getPrice())
                .cost(getCost())
                .quantity(getQuantity())
                .netTax(getNetTax())
                .discount(getDiscount())
                .taxType(getTaxType())
                .stockAlert(getStockAlert())
                .serialNumber(getSerialNumber())
                .image(getImage())
                .build();

        productsRepository.put(product);
        resetProperties();
        getAllProducts();
    }

//    public static void updateProduct(long index) {
//        var productsRepository = new ProductsRepositoryImpl();
////        Dao<Product, Long> productDao = DaoManager.createDao(connectionSource, Product.class);
////
////        Product product = productDao.queryForId(index);
////
////        product.setQuantity(getQuantity());
////        product.setUpdatedBy(getName());
////        product.setUpdatedAt(Date.now());
////
////        productDao.update(product);
//
//        Platform.runLater(ProductViewModel::resetProperties);
//
//        getAllProducts();
//    }

    public static void deleteProduct(Long productID) throws IOException, InterruptedException {
        var productsRepository = new ProductsRepositoryImpl();
        var findModel = new FindModel();

        findModel.setId(productID);
        productsRepository.delete(findModel);
        getAllProducts();
    }
}
