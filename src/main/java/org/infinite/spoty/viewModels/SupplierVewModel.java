package org.infinite.spoty.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.SupplierDao;
import org.infinite.spoty.database.models.Supplier;

public class SupplierVewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty code = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final StringProperty address = new SimpleStringProperty("");
    private static final StringProperty taxNumber = new SimpleStringProperty("");
    private static final StringProperty country = new SimpleStringProperty("");
    private static final ObservableList<Supplier> customersList= FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        SupplierVewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        SupplierVewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getCode() {
        return code.get();
    }

    public static void setCode(String code) {
        SupplierVewModel.code.set(code);
    }

    public static StringProperty codeProperty() {
        return code;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        SupplierVewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        SupplierVewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static String getCity() {
        return city.get();
    }

    public static void setCity(String city) {
        SupplierVewModel.city.set(city);
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static String getAddress() {
        return address.get();
    }

    public static void setAddress(String address) {
        SupplierVewModel.address.set(address);
    }

    public static StringProperty addressProperty() {
        return address;
    }

    public static String getTaxNumber() {
        return taxNumber.get();
    }

    public static void setTaxNumber(String taxNumber) {
        SupplierVewModel.taxNumber.set(taxNumber);
    }

    public static StringProperty taxNumberProperty() {
        return taxNumber;
    }

    public static String getCountry() {
        return country.get();
    }

    public static void setCountry(String country) {
        SupplierVewModel.country.set(country);
    }

    public static StringProperty countryProperty() {
        return country;
    }

    public static void resetProperties() {
        setId(0);
        setName("");
        setCode("");
        setEmail("");
        setEmail("");
        setPhone("");
        setCity("");
        setAddress("");
        setTaxNumber("");
        setCountry("");
    }

    public static void saveSupplier() {
        Supplier customer = new Supplier(getName(), getEmail(), getPhone(), getCity(), getAddress(), getTaxNumber(), getCountry());
        SupplierDao.saveSupplier(customer);
        resetProperties();
        getSuppliers();
    }

    public static ObservableList<Supplier> getSuppliers() {
        customersList.clear();
        customersList.addAll(SupplierDao.fetchSuppliers());
        return customersList;
    }
}