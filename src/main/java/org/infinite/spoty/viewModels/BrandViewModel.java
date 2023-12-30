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
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.daos.Brand;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.BrandsRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


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

    public static void saveBrand() throws IOException, InterruptedException {
        var brandsRepository = new BrandsRepositoryImpl();
        var brand = Brand.builder()
                .name(getName())
                .description(getDescription())
                // .image(getImage)
                .build();

        brandsRepository.post(brand);
        clearBrandData();
        getItems();
    }

    public static void clearBrandData() {
        setId(0);
        setName("");
        setDescription("");
    }

    public static void getItems() {
        var brandsRepository = new BrandsRepositoryImpl();
        Type listType = new TypeToken<ArrayList<Brand>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    brandsList.clear();

                    try {
                        ArrayList<Brand> brandList = new Gson().fromJson(brandsRepository.fetchAll().body(), listType);
                        brandsList.addAll(brandList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, BrandViewModel.class);
                    }
                });
    }

    public static void getItem(long brandID) throws IOException, InterruptedException {
        var brandsRepository = new BrandsRepositoryImpl();
        var findModel = new FindModel();
        findModel.setId(brandID);
        var response = brandsRepository.fetch(findModel).body();
        var brand = new Gson().fromJson(response, Brand.class);

        setId(brand.getId());
        setName(brand.getName());
        setDescription(brand.getDescription());
        getItems();
    }

    public static void searchItem(String search) {
        var brandsRepository = new BrandsRepositoryImpl();
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<Brand>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    brandsList.clear();

                    try {
                        ArrayList<Brand> brandList = new Gson().fromJson(brandsRepository.search(searchModel).body(), listType);
                        brandsList.addAll(brandList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, BranchViewModel.class);
                    }
                });
    }

    public static void updateItem() throws IOException, InterruptedException {
        var brandsRepository = new BrandsRepositoryImpl();

        var brand = Brand.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                // .image(getImage)
                .build();

        brandsRepository.put(brand);
        clearBrandData();
        getItems();
    }

    public static void deleteItem(Long brandID) throws IOException, InterruptedException {
        var brandsRepository = new BrandsRepositoryImpl();
        var findModel = new FindModel();

        findModel.setId(brandID);
        brandsRepository.delete(findModel);
        getItems();
    }
}
