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
import org.infinite.spoty.data_source.dtos.ProductCategory;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.ProductCategoriesRepositoryImpl;
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


public class ProductCategoryViewModel {
    public static final ProductCategoriesRepositoryImpl productCategoriesRepository = new ProductCategoriesRepositoryImpl();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty code = new SimpleStringProperty("");
    private static final StringProperty title = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    public static ObservableList<ProductCategory> categoriesList =
            FXCollections.observableArrayList();
    public static final ListProperty<ProductCategory> categories =
            new SimpleListProperty<>(categoriesList);
    public static ObservableList<ProductCategory> categoriesComboBoxList =
            FXCollections.observableArrayList();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        ProductCategoryViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getTitle() {
        return title.get();
    }

    public static void setTitle(String title) {
        ProductCategoryViewModel.title.set(title);
    }

    public static StringProperty titleProperty() {
        return title;
    }

    public static String getCode() {
        return code.get();
    }

    public static void setCode(String code) {
        ProductCategoryViewModel.code.set(code);
    }

    public static StringProperty codeProperty() {
        return code;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ProductCategoryViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static ObservableList<ProductCategory> getCategories() {
        return categories.get();
    }

    public static void setCategories(ObservableList<ProductCategory> categories) {
        ProductCategoryViewModel.categories.set(categories);
    }

    public static ListProperty<ProductCategory> categoriesProperty() {
        return categories;
    }

    public static void saveProductCategory(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var productCategory = ProductCategory.builder()
                .code(getCode())
                .name(getName())
                .build();

        var task = productCategoriesRepository.post(productCategory);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void clearProductCategoryData() {
        setId(0);
        setCode("");
        setName("");
    }

    public static void getItems(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = productCategoriesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<ProductCategory>>() {
                }.getType();
                ArrayList<ProductCategory> categoryList = gson.fromJson(task.get().body(), listType);

                categoriesList.clear();
                categoriesList.addAll(categoryList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ProductCategoryViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = productCategoriesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var productCategory = gson.fromJson(task.get().body(), ProductCategory.class);

                setId(productCategory.getId());
                setCode(productCategory.getCode());
                setName(productCategory.getName());
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ProductCategoryViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = productCategoriesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<ProductCategory>>() {
                }.getType();
                ArrayList<ProductCategory> currencyList = gson.fromJson(task.get().body(), listType);

                categoriesList.clear();
                categoriesList.addAll(currencyList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ProductCategoryViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var productCategory = ProductCategory.builder()
                .id(getId())
                .code(getCode())
                .name(getName())
                .build();

        var task = productCategoriesRepository.put(productCategory);
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

        var task = productCategoriesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
