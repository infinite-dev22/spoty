package org.infinite.spoty.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.BranchDao;
import org.infinite.spoty.database.models.Branch;

public class BranchViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty title = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty town = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final StringProperty zipcode = new SimpleStringProperty("");
    public static ObservableList<Branch> branchesList = FXCollections.observableArrayList();

    public static Integer getId() {
        return id.get();
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static void setId(Integer id) {
        BranchViewModel.id.set(id);
    }

    public static String getName() {
        return name.get();
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static void setName(String name) {
        BranchViewModel.name.set(name);
    }

    public static String getEmail() {
        return email.get();
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static void setEmail(String email) {
        BranchViewModel.email.set(email);
    }

    public static String getPhone() {
        return phone.get();
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static void setPhone(String phone) {
        BranchViewModel.phone.set(phone);
    }

    public static String getTown() {
        return town.get();
    }

    public static StringProperty townProperty() {
        return town;
    }

    public static void setTown(String town) {
        BranchViewModel.town.set(town);
    }

    public static String getCity() {
        return city.get();
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static void setCity(String city) {
        BranchViewModel.city.set(city);
    }

    public static String getZipcode() {
        return zipcode.get();
    }

    public static StringProperty zipcodeProperty() {
        return zipcode;
    }

    public static void setZipcode(String zipcode) {
        BranchViewModel.zipcode.set(zipcode);
    }

    public static String getTitle() {
        return title.get();
    }

    public static StringProperty titleProperty() {
        return title;
    }

    public static void setTitle(String title) {
        BranchViewModel.title.set(title);
    }

    public static void saveBranch() {
        Branch branch = new Branch(getName(), getCity(), getPhone(), getEmail(), getTown(), getZipcode());
        BranchDao.saveBranch(branch);
        branchesList.clear();
        clearBranchData();
        getBranches();
    }

    public static void clearBranchData() {
        setName("");
        setCity("");
        setPhone("");
        setEmail("");
        setTown("");
        setZipcode("");
    }

    public static ObservableList<Branch> getBranches() {
        branchesList.addAll(BranchDao.getBranches());
        return branchesList;
    }
}
