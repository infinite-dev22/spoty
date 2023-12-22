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

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.daos.Brand;


public class BrandViewModel {
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    public static ObservableList<Brand> brandsList = FXCollections.observableArrayList();
    private static final ListProperty<Brand> brands = new SimpleListProperty<>(brandsList);
    public static ObservableList<Brand> brandsComboBoxList = FXCollections.observableArrayList();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        BrandViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
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

    public static ObservableList<Brand> getBrands() {
        return brands.get();
    }

    public static void setBrands(ObservableList<Brand> brands) {
        BrandViewModel.brands.set(brands);
    }

    public static ListProperty<Brand> brandsProperty() {
        return brands;
    }

    public static void saveBrand() throws Exception {
//        Dao<Brand, Long> brandDao = DaoManager.createDao(connectionSource, Brand.class);
//
//        Brand brand = new Brand(getName(), getDescription());
//        brandDao.create(brand);

        clearBrandData();
        getItems();
    }

    public static void clearBrandData() {
        setId(0);
        setName("");
        setDescription("");
    }

    public static void getItems() throws Exception {
//        SQLiteConnection connection = SQLiteConnection.getInstance();
//        ConnectionSource connectionSource = connection.getConnection();
//
//        Dao<Brand, Long> brandDao = DaoManager.createDao(connectionSource, Brand.class);

//        Platform.runLater(
//                () -> {
//                    brandsList.clear();
//
//                    try {
//                        brandsList.addAll(brandDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, BrandViewModel.class);
//                    }
//                });
    }

    public static void getItem(long brandID) throws Exception {
//        SQLiteConnection connection = SQLiteConnection.getInstance();
//        ConnectionSource connectionSource = connection.getConnection();
//
//        Dao<Brand, Long> brandDao = DaoManager.createDao(connectionSource, Brand.class);
//
//        Brand brand = brandDao.queryForId(brandID);
//        setId(brand.getId());
//        setName(brand.getName());
//        setDescription(brand.getDescription());
        getItems();
    }

    public static void updateItem(long index) throws Exception {
//        SQLiteConnection connection = SQLiteConnection.getInstance();
//        ConnectionSource connectionSource = connection.getConnection();
//
//        Dao<Brand, Long> brandDao = DaoManager.createDao(connectionSource, Brand.class);
//
//        Brand brand = brandDao.queryForId(index);
//
//        brand.setName(getName());
//        brand.setDescription(getDescription());
//
//        brandDao.update(brand);

        clearBrandData();
        getItems();
    }

    public static void deleteItem(long index) throws Exception {
//        SQLiteConnection connection = SQLiteConnection.getInstance();
//        ConnectionSource connectionSource = connection.getConnection();
//
//        Dao<Brand, Long> brandDao = DaoManager.createDao(connectionSource, Brand.class);
//
//        brandDao.deleteById(index);

        getItems();
    }
}
