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
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.dtos.Expense;
import org.infinite.spoty.data_source.dtos.ExpenseCategory;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.ExpensesRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpensesViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    public static final ObservableList<Expense> expensesList = FXCollections.observableArrayList();
    private static final ListProperty<Expense> expenses = new SimpleListProperty<>(expensesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final StringProperty reference = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty amount = new SimpleStringProperty("");
    private static final ObjectProperty<ExpenseCategory> category = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty details = new SimpleStringProperty("");

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

    public static void saveExpense() throws IOException, InterruptedException {
        var expenseRepository = new ExpensesRepositoryImpl();
        var expense = Expense.builder()
                .date(getDate())
                .ref(getReference())
                .name(getName())
                .expenseCategory(getCategory())
                .branch(getBranch())
                .details(getDetails())
                .build();

        expenseRepository.post(expense);
        resetProperties();
        getAllExpenses();
    }

    public static void getAllExpenses() {
        var expenseRepository = new ExpensesRepositoryImpl();
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class,
                        UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
                .create();
        Type listType = new TypeToken<ArrayList<Expense>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    expensesList.clear();

                    try {
                        ArrayList<Expense> expenseList = gson.fromJson(expenseRepository.fetchAll().body(), listType);
                        expensesList.addAll(expenseList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, ExpensesViewModel.class);
                    }
                });
    }

    public static void getItem(Long expenseID) throws IOException, InterruptedException {
        var expenseRepository = new ExpensesRepositoryImpl();
        var findModel = new FindModel();
        findModel.setId(expenseID);
        var response = expenseRepository.fetch(findModel).body();
        var expense = gson.fromJson(response, Expense.class);
//
        setId(expense.getId());
        setDate(expense.getLocaleDate());
        setName(expense.getName());
        setBranch(expense.getBranch());
        setCategory(expense.getExpenseCategory());
        setAmount(expense.getAmount());
        setDetails(expense.getDetails());
        getAllExpenses();
    }

    public static void searchItem(String search) {
        var expensesRepository = new ExpensesRepositoryImpl();
        var searchModel = new SearchModel();
        var listType = new TypeToken<ArrayList<Expense>>() {
        }.getType();

        searchModel.setSearch(search);

        Platform.runLater(
                () -> {
                    expensesList.clear();

                    try {
                        ArrayList<Expense> expenseList = gson.fromJson(expensesRepository.search(searchModel).body(), listType);
                        expensesList.addAll(expenseList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, ExpensesViewModel.class);
                    }
                });
    }

    public static void updateItem() throws IOException, InterruptedException {
        var expenseRepository = new ExpensesRepositoryImpl();
        var expense = Expense.builder()
                .id(getId())
                .date(getDate())
                .ref(getReference())
                .name(getName())
                .expenseCategory(getCategory())
                .branch(getBranch())
                .details(getDetails())
                .build();

        expenseRepository.put(expense);
        resetProperties();
        getAllExpenses();
    }

    public static void deleteItem(Long expenseID) throws IOException, InterruptedException {
        var expenseRepository = new ExpensesRepositoryImpl();
        var findModel = new FindModel();

        findModel.setId(expenseID);
        expenseRepository.delete(findModel);
        getAllExpenses();
    }
}