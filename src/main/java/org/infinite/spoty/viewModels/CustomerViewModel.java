package org.infinite.spoty.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.CustomerDao;
import org.infinite.spoty.database.models.Customer;

public class CustomerViewModel {
    public static final ObservableList<Customer> customersList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty code = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final StringProperty address = new SimpleStringProperty("");
    private static final StringProperty taxNumber = new SimpleStringProperty("");
    private static final StringProperty country = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        CustomerViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        CustomerViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getCode() {
        return code.get();
    }

    public static void setCode(String code) {
        CustomerViewModel.code.set(code);
    }

    public static StringProperty codeProperty() {
        return code;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        CustomerViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        CustomerViewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static String getCity() {
        return city.get();
    }

    public static void setCity(String city) {
        CustomerViewModel.city.set(city);
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static String getAddress() {
        return address.get();
    }

    public static void setAddress(String address) {
        CustomerViewModel.address.set(address);
    }

    public static StringProperty addressProperty() {
        return address;
    }

    public static String getTaxNumber() {
        return taxNumber.get();
    }

    public static void setTaxNumber(String taxNumber) {
        CustomerViewModel.taxNumber.set(taxNumber);
    }

    public static StringProperty taxNumberProperty() {
        return taxNumber;
    }

    public static String getCountry() {
        return country.get();
    }

    public static void setCountry(String country) {
        CustomerViewModel.country.set(country);
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

    public static void saveCustomer() {
        Customer customer = new Customer(getName(), getEmail(), "+" + getPhone(), getCity(), getAddress(), getTaxNumber(), getCountry());
        CustomerDao.saveCustomer(customer);
        resetProperties();
        getCustomers();
    }

    public static ObservableList<Customer> getCustomers() {
        customersList.clear();
        customersList.addAll(CustomerDao.fetchCustomers());
        return customersList;
    }

    public static void getItem(int customerID) {
        Customer customer = CustomerDao.findCustomer(customerID);
        setId(customer.getId());
        setName(customer.getName());
        setEmail(customer.getEmail());
        setPhone(customer.getPhone());
        setCity(customer.getCity());
        setCountry(customer.getCountry());
        setAddress(customer.getAddress());
        setTaxNumber(customer.getTaxNumber());
        getCustomers();
    }

    public static void updateItem(int customerID) {
        Customer customer = new Customer(getName(), getEmail(), getPhone(), getCity(), getAddress(), getTaxNumber(),
                getCountry());
        CustomerDao.updateCustomer(customer, customerID);
        resetProperties();
        getCustomers();
    }
}
