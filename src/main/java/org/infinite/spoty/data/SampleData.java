package org.infinite.spoty.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.models.Currency;
import org.infinite.spoty.models.QuickStats;
import org.infinite.spoty.models.RoleMaster;
import org.infinite.spoty.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SampleData implements Serializable {
    public static List<QuickStats> quickStatsSampleData() {
        List<QuickStats> samples = new ArrayList<>();

        QuickStats quickStats = new QuickStats();
        quickStats.setTitle("2050");
        quickStats.setSubtitle("Total Orders");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("3250");
        quickStats.setSubtitle("Total Expense");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("87.5%");
        quickStats.setSubtitle("Total Revenue");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("2550");
        quickStats.setSubtitle("New Users");
        samples.add(quickStats);

        return samples;
    }

    public static ObservableList<User> userSampleData() {
        ObservableList<User> userList = FXCollections.observableArrayList();

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("John");
        user.setUserEmail("johndoe@gmail.com");
        user.setUserPhoneNumber("0123456789");
        user.setUserStatus("Active");
        userList.add(user);

        user = new User();
        user.setFirstName("William");
        user.setLastName("Castillo");
        user.setUserName("William Castillo");
        user.setUserEmail("admin@example.com");
        user.setUserPhoneNumber("0123456789");
        user.setUserStatus("Active");
        userList.add(user);

        user = new User();
        user.setFirstName("Seller");
        user.setLastName("Seller");
        user.setUserName("Seller");
        user.setUserEmail("Seller@example.com");
        user.setUserPhoneNumber("0123456789");
        user.setUserStatus("Inactive");
        userList.add(user);

        return userList;
    }

    public static ObservableList<RoleMaster> roleMasterSampleData() {
        ObservableList<RoleMaster> roleMasterList = FXCollections.observableArrayList();

        RoleMaster roleMaster = new RoleMaster();
        roleMaster.setRole("Owner");
        roleMaster.setDescription("Administrator role");
        roleMasterList.add(roleMaster);

        return roleMasterList;
    }

    public static ObservableList<Currency> currencySampleData() {
        ObservableList<Currency> currencyList = FXCollections.observableArrayList();

        Currency currency = new Currency();
        currency.setCurrencyName("US Dollar");
        currency.setCurrencySymbol("$");
        currency.setCurrencyCode("USD");
        currencyList.add(currency);

        return currencyList;
    }
}
