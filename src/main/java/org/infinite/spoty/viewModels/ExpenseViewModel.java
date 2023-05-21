package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ExpenseDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Expense;
import org.infinite.spoty.database.models.ExpenseCategory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final StringProperty reference = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    private static final DoubleProperty amount = new SimpleDoubleProperty(0);
    private static final ObjectProperty<ExpenseCategory> category = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty details = new SimpleStringProperty("");
    private static final ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        ExpenseViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date.get());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDate(String date) {
        ExpenseViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static String getReference() {
        return reference.get();
    }

    public static void setReference(String reference) {
        ExpenseViewModel.reference.set(reference);
    }

    public static StringProperty referenceProperty() {
        return reference;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ExpenseViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static double getAmount() {
        return amount.get();
    }

    public static void setAmount(double amount) {
        ExpenseViewModel.amount.set(amount);
    }

    public static DoubleProperty amountProperty() {
        return amount;
    }

    public static ExpenseCategory getCategory() {
        return category.get();
    }

    public static void setCategory(ExpenseCategory category) {
        ExpenseViewModel.category.set(category);
    }

    public static ObjectProperty<ExpenseCategory> categoryProperty() {
        return category;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        ExpenseViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getDetails() {
        return details.get();
    }

    public static void setDetails(String details) {
        ExpenseViewModel.details.set(details);
    }

    public static StringProperty detailsProperty() {
        return details;
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

    public static void saveExpense() {
        Expense expense = new Expense(getDate(), getCategory(), getBranch(), getDetails(), getAmount());
        ExpenseDao.saveExpense(expense);
        getExpenses();
        resetProperties();
    }

    public static ObservableList<Expense> getExpenses() {
        expenseList.clear();
        expenseList.addAll(ExpenseDao.getExpenses());
        return expenseList;
    }
}
