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
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.daos.Customer;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.CustomersRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class CustomerViewModel {
    public static final ObservableList<Customer> customersList = FXCollections.observableArrayList();
    public static final ObservableList<Customer> customersComboBoxList =
            FXCollections.observableArrayList();
    private static final ListProperty<Customer> customers = new SimpleListProperty<>(customersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty code = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final StringProperty address = new SimpleStringProperty("");
    private static final StringProperty taxNumber = new SimpleStringProperty("");
    private static final StringProperty country = new SimpleStringProperty("");

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        CustomerViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
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

    public static ObservableList<Customer> getCustomers() {
        return customers.get();
    }

    public static void setCustomers(ObservableList<Customer> customers) {
        CustomerViewModel.customers.set(customers);
    }

    public static ListProperty<Customer> customersProperty() {
        return customers;
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

    public static void saveCustomer() throws IOException, InterruptedException {
        var customersRepository = new CustomersRepositoryImpl();
        var customer = Customer.builder()
                .name(getName())
                .email(getEmail())
                .phone(getPhone())
                .city(getCity())
                .address(getAddress())
                .taxNumber(getTaxNumber())
                .country(getCountry())
                .build();

        customersRepository.post(customer);
        resetProperties();
        getAllCustomers();
    }

    public static void getAllCustomers() {
        var customersRepository = new CustomersRepositoryImpl();
        Type listType = new TypeToken<ArrayList<Customer>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    customersList.clear();

                    try {
                        ArrayList<Customer> customerList = new Gson().fromJson(customersRepository.fetchAll().body(), listType);
                        customersList.addAll(customerList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, CustomerViewModel.class);
                    }
                });
    }

    public static void getItem(Long customerID) throws IOException, InterruptedException {
        var customersRepository = new CustomersRepositoryImpl();
        var findModel = new FindModel();
        findModel.setId(customerID);
        var response = customersRepository.fetch(findModel).body();
        var customer = new Gson().fromJson(response, Customer.class);

        setId(customer.getId());
        setName(customer.getName());
        setEmail(customer.getEmail());
        setPhone(customer.getPhone());
        setCity(customer.getCity());
        setCountry(customer.getCountry());
        setAddress(customer.getAddress());
        setTaxNumber(customer.getTaxNumber());
        getAllCustomers();
    }

    public static void searchItem(String search) {
        var customersRepository = new CustomersRepositoryImpl();
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<Customer>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    customersList.clear();

                    try {
                        ArrayList<Customer> customerList = new Gson().fromJson(customersRepository.search(searchModel).body(), listType);
                        customersList.addAll(customerList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, CustomerViewModel.class);
                    }
                });

    }

    public static void updateItem() throws IOException, InterruptedException {
        var customersRepository = new CustomersRepositoryImpl();
        var customer = Customer.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .phone(getPhone())
                .city(getCity())
                .address(getAddress())
                .taxNumber(getTaxNumber())
                .country(getCountry())
                .build();

        customersRepository.put(customer);
        resetProperties();
        getAllCustomers();
    }

    public static void deleteItem(Long customerID) throws Exception {
        var customersRepository = new CustomersRepositoryImpl();
        var findModel = new FindModel();

        findModel.setId(customerID);
        customersRepository.delete(findModel);
        getAllCustomers();
    }
}
