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
import org.infinite.spoty.data_source.dtos.ExpenseCategory;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.ExpenseCategoriesRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


public class ExpenseCategoryViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    public static final ObservableList<ExpenseCategory> categoryList =
            FXCollections.observableArrayList();
    public static final ObservableList<ExpenseCategory> categoryComboBoxList =
            FXCollections.observableArrayList();
    private static final ListProperty<ExpenseCategory> categories =
            new SimpleListProperty<>(categoryList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        ExpenseCategoryViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ExpenseCategoryViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        ExpenseCategoryViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static ObservableList<ExpenseCategory> getCategories() {
        return categories.get();
    }

    public static void setCategories(ObservableList<ExpenseCategory> categories) {
        ExpenseCategoryViewModel.categories.set(categories);
    }

    public static ListProperty<ExpenseCategory> categoriesProperty() {
        return categories;
    }

    public static void resetProperties() {
        setId(0);
        setName("");
        setDescription("");
    }

    public static void saveExpenseCategory() throws IOException, InterruptedException {
        var expenseCategoriesRepository = new ExpenseCategoriesRepositoryImpl();
        var expenseCategory = ExpenseCategory.builder()
                .name(getName())
                .description(getDescription())
                .build();

        expenseCategoriesRepository.post(expenseCategory);
        resetProperties();
        getAllCategories();
    }

    public static void getAllCategories() {
        var expenseCategoriesRepository = new ExpenseCategoriesRepositoryImpl();
        Type listType = new TypeToken<ArrayList<ExpenseCategory>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    categoryList.clear();

                    try {
                        ArrayList<ExpenseCategory> categoryListList = gson.fromJson(expenseCategoriesRepository.fetchAll().body(), listType);
                        categoryList.addAll(categoryListList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, ExpenseCategoryViewModel.class);
                    }
                });
    }

    public static void getItem(Long categoryID) throws IOException, InterruptedException {
        var expenseCategoriesRepository = new ExpenseCategoriesRepositoryImpl();
        var findModel = new FindModel();
        findModel.setId(categoryID);
        var response = expenseCategoriesRepository.fetch(findModel).body();
        var expenseCategory = gson.fromJson(response, ExpenseCategory.class);

        setId(expenseCategory.getId());
        setName(expenseCategory.getName());
        setDescription(expenseCategory.getDescription());
        getAllCategories();
    }

    public static void searchItem(String search) {
        var expenseCategoriesRepository = new ExpenseCategoriesRepositoryImpl();
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<ExpenseCategory>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    categoryList.clear();

                    try {
                        ArrayList<ExpenseCategory> expenseCategoryList = gson.fromJson(
                                expenseCategoriesRepository.search(searchModel).body(), listType);
                        categoryList.addAll(expenseCategoryList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, ExpenseCategoryViewModel.class);
                    }
                });
    }

    public static void updateItem() throws Exception {
        var expenseCategoriesRepository = new ExpenseCategoriesRepositoryImpl();
        var expenseCategory = ExpenseCategory.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .build();

        expenseCategoriesRepository.put(expenseCategory);
        resetProperties();
        getAllCategories();
    }

    public static void deleteItem(Long expenseCategoryID) throws Exception {
        var expenseCategoriesRepository = new ExpenseCategoriesRepositoryImpl();
        var findModel = new FindModel();

        findModel.setId(expenseCategoryID);
        expenseCategoriesRepository.delete(findModel);
        getAllCategories();
    }
}
