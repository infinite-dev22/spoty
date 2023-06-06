package org.infinite.spoty.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.SupplierDao;
import org.infinite.spoty.database.models.Supplier;

public class SupplierViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty code = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final StringProperty address = new SimpleStringProperty("");
    private static final StringProperty taxNumber = new SimpleStringProperty("");
    private static final StringProperty country = new SimpleStringProperty("");
    public static final ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        SupplierViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        SupplierViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getCode() {
        return code.get();
    }

    public static void setCode(String code) {
        SupplierViewModel.code.set(code);
    }

    public static StringProperty codeProperty() {
        return code;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        SupplierViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        SupplierViewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static String getCity() {
        return city.get();
    }

    public static void setCity(String city) {
        SupplierViewModel.city.set(city);
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static String getAddress() {
        return address.get();
    }

    public static void setAddress(String address) {
        SupplierViewModel.address.set(address);
    }

    public static StringProperty addressProperty() {
        return address;
    }

    public static String getTaxNumber() {
        return taxNumber.get();
    }

    public static void setTaxNumber(String taxNumber) {
        SupplierViewModel.taxNumber.set(taxNumber);
    }

    public static StringProperty taxNumberProperty() {
        return taxNumber;
    }

    public static String getCountry() {
        return country.get();
    }

    public static void setCountry(String country) {
        SupplierViewModel.country.set(country);
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
        Supplier supplier = new Supplier(getName(), getEmail(), getPhone(), getCity(), getAddress(), getTaxNumber(), getCountry());
        SupplierDao.saveSupplier(supplier);
        resetProperties();
        getSuppliers();
    }

    public static ObservableList<Supplier> getSuppliers() {
        suppliersList.clear();
        suppliersList.addAll(SupplierDao.fetchSuppliers());
        return suppliersList;
    }

    public static void getItem(int supplierID) {
        Supplier supplier = SupplierDao.findSupplier(supplierID);
        setId(supplier.getId());
        setName(supplier.getName());
        setEmail(supplier.getEmail());
        setPhone(supplier.getPhone());
        setCity(supplier.getCity());
        setCountry(supplier.getCountry());
        setAddress(supplier.getAddress());
        setTaxNumber(supplier.getTaxNumber());
        getSuppliers();
    }

    public static void updateItem(int supplierID) {
        Supplier supplier = new Supplier(getName(), getEmail(), getPhone(), getTaxNumber(), getAddress(), getCity(),
                getCountry());
        SupplierDao.updateSupplier(supplier, supplierID);
        getSuppliers();
    }
}
