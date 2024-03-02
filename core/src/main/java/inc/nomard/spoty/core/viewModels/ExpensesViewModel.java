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

package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Expense;
import inc.nomard.spoty.network_bridge.dtos.ExpenseCategory;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.ExpensesRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ExpensesViewModel {
    public static final ObservableList<Expense> expensesList = FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<Expense> expenses = new SimpleListProperty<>(expensesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final StringProperty reference = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty amount = new SimpleStringProperty("");
    private static final ObjectProperty<ExpenseCategory> category = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty details = new SimpleStringProperty("");
    private static final ExpensesRepositoryImpl expenseRepository = new ExpensesRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        ExpensesViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @NotNull Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, ExpensesViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        ExpensesViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static String getReference() {
        return reference.get();
    }

    public static void setReference(String reference) {
        ExpensesViewModel.reference.set(reference);
    }

    public static StringProperty referenceProperty() {
        return reference;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ExpensesViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static double getAmount() {
        return Double.parseDouble(!amount.get().isEmpty() ? amount.get() : "0");
    }

    public static void setAmount(double amount) {
        ExpensesViewModel.amount.set(amount > 0 ? Double.toString(amount) : "");
    }

    public static StringProperty amountProperty() {
        return amount;
    }

    public static ExpenseCategory getCategory() {
        return category.get();
    }

    public static void setCategory(ExpenseCategory category) {
        ExpensesViewModel.category.set(category);
    }

    public static ObjectProperty<ExpenseCategory> categoryProperty() {
        return category;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        ExpensesViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getDetails() {
        return details.get();
    }

    public static void setDetails(String details) {
        ExpensesViewModel.details.set(details);
    }

    public static StringProperty detailsProperty() {
        return details;
    }

    public static ObservableList<Expense> getExpenses() {
        return expenses.get();
    }

    public static void setExpenses(ObservableList<Expense> expenses) {
        ExpensesViewModel.expenses.set(expenses);
    }

    public static ListProperty<Expense> expensesProperty() {
        return expenses;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setReference("");
        setName("");
        setAmount(0);
        setCategory(null);
        setBranch(null);
        setDetails("");
    }

    public static void saveExpense(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var expense = Expense.builder()
                .date(getDate())
                .ref(getReference())
                .name(getName())
                .expenseCategory(getCategory())
                .details(getDetails())
                .build();

        var task = expenseRepository.post(expense);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllExpenses(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var task = expenseRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Expense>>() {
                }.getType();
                ArrayList<Expense> expenseList = gson.fromJson(task.get().body(), listType);

                expensesList.clear();
                expensesList.addAll(expenseList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ExpensesViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = expenseRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var expense = gson.fromJson(task.get().body(), Expense.class);

                setId(expense.getId());
                setDate(expense.getLocaleDate());
                setName(expense.getName());
                setCategory(expense.getExpenseCategory());
                setAmount(expense.getAmount());
                setDetails(expense.getDetails());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = expenseRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var listType = new TypeToken<ArrayList<Expense>>() {
                }.getType();
                ArrayList<Expense> expenseList = gson.fromJson(task.get().body(), listType);

                expensesList.clear();
                expensesList.addAll(expenseList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ExpensesViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var expense = Expense.builder()
                .id(getId())
                .date(getDate())
                .ref(getReference())
                .name(getName())
                .expenseCategory(getCategory())
                .details(getDetails())
                .build();

        var task = expenseRepository.put(expense);
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

        var task = expenseRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
