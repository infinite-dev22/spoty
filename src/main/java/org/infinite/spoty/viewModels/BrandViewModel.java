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
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.Brand;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.BrandsRepositoryImpl;
import org.infinite.spoty.utils.ParameterlessConsumer;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class BrandViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    public static ObservableList<Brand> brandsList = FXCollections.observableArrayList();
    private static final ListProperty<Brand> brands = new SimpleListProperty<>(brandsList);
    public static ObservableList<Brand> brandsComboBoxList = FXCollections.observableArrayList();
    public static BrandsRepositoryImpl brandsRepository = new BrandsRepositoryImpl();

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

    public static void saveBrand(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var brand = Brand.builder()
                .name(getName())
                .description(getDescription())
                // .image(getImage)
                .build();

        var task = brandsRepository.post(brand);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void clearBrandData() {
        setId(0);
        setName("");
        setDescription("");
    }

    public static void getItems(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = brandsRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Brand>>() {
                }.getType();

                ArrayList<Brand> brandList = gson.fromJson(task.get().body(), listType);
                brandsList.clear();
                brandsList.addAll(brandList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BrandViewModel.class);
            }
        });
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = brandsRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var brand = gson.fromJson(task.get().body(), Brand.class);

                setId(brand.getId());
                setName(brand.getName());
                setDescription(brand.getDescription());
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = brandsRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Brand>>() {
                }.getType();
                ArrayList<Brand> brandList = gson.fromJson(task.get().body(), listType);
                brandsList.clear();
                brandsList.addAll(brandList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BranchViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var brand = Brand.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                // .image(getImage)
                .build();

        var task = brandsRepository.put(brand);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = brandsRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
