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

package inc.normad.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.normad.spoty.network_bridge.dtos.ExpenseCategory;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.ExpenseCategoriesRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class ExpenseCategoryViewModel {
    public static final ObservableList<ExpenseCategory> categoryList =
            FXCollections.observableArrayList();
    public static final ObservableList<ExpenseCategory> categoryComboBoxList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<ExpenseCategory> categories =
            new SimpleListProperty<>(categoryList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final ExpenseCategoriesRepositoryImpl expenseCategoriesRepository = new ExpenseCategoriesRepositoryImpl();

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

    public static void saveExpenseCategory(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var expenseCategory = ExpenseCategory.builder()
                .name(getName())
                .description(getDescription())
                .build();

        var task = expenseCategoriesRepository.post(expenseCategory);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllCategories(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = expenseCategoriesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<ExpenseCategory>>() {
                }.getType();
                ArrayList<ExpenseCategory> categoryListList = gson.fromJson(task.get().body(), listType);

                categoryList.clear();
                categoryList.addAll(categoryListList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ExpenseCategoryViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = expenseCategoriesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var expenseCategory = gson.fromJson(task.get().body(), ExpenseCategory.class);

                setId(expenseCategory.getId());
                setName(expenseCategory.getName());
                setDescription(expenseCategory.getDescription());
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ExpenseCategoryViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = expenseCategoriesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<ExpenseCategory>>() {
                }.getType();
                ArrayList<ExpenseCategory> expenseCategoryList = gson.fromJson(
                        task.get().body(), listType);

                categoryList.clear();
                categoryList.addAll(expenseCategoryList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ExpenseCategoryViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var expenseCategory = ExpenseCategory.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .build();

        var task = expenseCategoriesRepository.put(expenseCategory);
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

        var task = expenseCategoriesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
