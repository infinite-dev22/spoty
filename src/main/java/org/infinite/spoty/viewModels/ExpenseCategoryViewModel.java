package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ExpenseCategoryDao;
import org.infinite.spoty.database.models.ExpenseCategory;
import org.infinite.spoty.database.models.User;

public class ExpenseCategoryViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final ObjectProperty<User> user = new SimpleObjectProperty<>(null);
    private static final StringProperty description = new SimpleStringProperty("");
    public static final ObservableList<ExpenseCategory> categoryList = FXCollections.observableArrayList();

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

    public static User getUser() {
        return user.get();
    }

    public static void setUser(User user) {
        ExpenseCategoryViewModel.user.set(user);
    }

    public static ObjectProperty<User> userProperty() {
        return user;
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
        setUser(null);
        setDescription("");
    }

    public static void saveExpenseCategory() {
        ExpenseCategory expenseCategory = new ExpenseCategory(getName(), getUser(), getDescription());
        ExpenseCategoryDao.saveExpenseCategory(expenseCategory);
        resetProperties();
        getCategories();
    }

    public static ObservableList<ExpenseCategory> getCategories() {
        categoryList.clear();
        categoryList.addAll(ExpenseCategoryDao.getExpenseCategories());
        return categoryList;
    }
}
