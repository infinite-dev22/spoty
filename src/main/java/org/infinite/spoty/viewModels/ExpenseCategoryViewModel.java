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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ExpenseCategoryDao;
import org.infinite.spoty.database.models.ExpenseCategory;

public class ExpenseCategoryViewModel {
    public static final ObservableList<ExpenseCategory> categoryList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        ExpenseCategoryViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
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

    public static void resetProperties() {
        setId(0);
        setName("");
        setDescription("");
    }

    public static void saveExpenseCategory() {
        ExpenseCategory expenseCategory = new ExpenseCategory(getName(), getDescription());
        ExpenseCategoryDao.saveExpenseCategory(expenseCategory);
        resetProperties();
        getCategories();
    }

    public static ObservableList<ExpenseCategory> getCategories() {
        categoryList.clear();
        categoryList.addAll(ExpenseCategoryDao.fetchExpenseCategories());
        return categoryList;
    }

    public static void getItem(int expenseCategoryID) {
        ExpenseCategory expenseCategory = ExpenseCategoryDao.findExpenseCategory(expenseCategoryID);
        setId(expenseCategory.getId());
        setName(expenseCategory.getName());
        setDescription(expenseCategory.getDescription());
        getCategories();
    }

    public static void updateItem(int expenseCategoryID) {
        ExpenseCategory expenseCategory = new ExpenseCategory(getName(), getDescription());
        ExpenseCategoryDao.updateExpenseCategory(expenseCategory, expenseCategoryID);
        getCategories();
    }
}
