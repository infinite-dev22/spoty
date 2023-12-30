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
import org.infinite.spoty.data_source.daos.Supplier;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.SuppliersRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class SupplierViewModel {
    public static final ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
    public static final ObservableList<Supplier> suppliersComboBoxList =
            FXCollections.observableArrayList();
    private static final ListProperty<Supplier> suppliers = new SimpleListProperty<>(suppliersList);
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
        SupplierViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
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

    public static ObservableList<Supplier> getSuppliers() {
        return suppliers.get();
    }

    public static void setSuppliers(ObservableList<Supplier> suppliers) {
        SupplierViewModel.suppliers.set(suppliers);
    }

    public static ListProperty<Supplier> suppliersProperty() {
        return suppliers;
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

    public static void saveSupplier() throws IOException, InterruptedException {
        var suppliersRepository = new SuppliersRepositoryImpl();
        var supplier = Supplier.builder()
                .name(getName())
                .email(getEmail())
                .phone(getPhone())
                .city(getCity())
                .address(getAddress())
                .taxNumber(getTaxNumber())
                .country(getCountry())
                .build();

        suppliersRepository.post(supplier);
        resetProperties();
        getAllSuppliers();
    }

    public static void getAllSuppliers() {
        var suppliersRepository = new SuppliersRepositoryImpl();
        Type listType = new TypeToken<ArrayList<Supplier>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    suppliersList.clear();

                    try {
                        ArrayList<Supplier> productList = new Gson().fromJson(suppliersRepository.fetchAll().body(), listType);
                        suppliersList.addAll(productList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, SupplierViewModel.class);
                    }
                });
    }

    public static void getItem(Long supplierID) throws IOException, InterruptedException {
        var suppliersRepository = new SuppliersRepositoryImpl();
        var findModel = new FindModel();
        findModel.setId(supplierID);
        var response = suppliersRepository.fetch(findModel).body();
        var supplier = new Gson().fromJson(response, Supplier.class);

        setId(supplier.getId());
        setName(supplier.getName());
        setEmail(supplier.getEmail());
        setPhone(supplier.getPhone());
        setCity(supplier.getCity());
        setCountry(supplier.getCountry());
        setAddress(supplier.getAddress());
        setTaxNumber(supplier.getTaxNumber());
        getAllSuppliers();
    }

    public static void searchItem(String search) {
        var suppliersRepository = new SuppliersRepositoryImpl();
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<Supplier>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    suppliersList.clear();

                    try {
                        ArrayList<Supplier> currencyList = new Gson().fromJson(suppliersRepository.search(searchModel)
                                .body(), listType);
                        suppliersList.addAll(currencyList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, SupplierViewModel.class);
                    }
                });
    }

    public static void updateItem() throws IOException, InterruptedException {
        var suppliersRepository = new SuppliersRepositoryImpl();
        var supplier = Supplier.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .phone(getPhone())
                .city(getCity())
                .address(getAddress())
                .taxNumber(getTaxNumber())
                .country(getCountry())
                .build();

        suppliersRepository.put(supplier);
        resetProperties();
        getAllSuppliers();
    }

    public static void deleteItem(Long supplierID) throws IOException, InterruptedException {
        var suppliersRepository = new SuppliersRepositoryImpl();
        var findModel = new FindModel();

        findModel.setId(supplierID);
        suppliersRepository.delete(findModel);
        getAllSuppliers();
    }
}
