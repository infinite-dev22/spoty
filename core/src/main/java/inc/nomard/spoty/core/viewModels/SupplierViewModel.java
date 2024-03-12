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

package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.SuppliersRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class SupplierViewModel {
    public static final ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
    public static final ObservableList<Supplier> suppliersComboBoxList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
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
    private static final SuppliersRepositoryImpl suppliersRepository = new SuppliersRepositoryImpl();

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

    public static void saveSupplier(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var supplier = Supplier.builder()
                .name(getName())
                .email(getEmail())
                .phone(getPhone())
                .city(getCity())
                .address(getAddress())
                .taxNumber(getTaxNumber())
                .country(getCountry())
                .build();

        var task = suppliersRepository.post(supplier);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllSuppliers(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var task = suppliersRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Supplier>>() {
                }.getType();
                ArrayList<Supplier> productList = gson.fromJson(task.get().body(), listType);

                suppliersList.clear();
                suppliersList.addAll(productList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SupplierViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = suppliersRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var supplier = gson.fromJson(task.get().body(), Supplier.class);

                setId(supplier.getId());
                setName(supplier.getName());
                setEmail(supplier.getEmail());
                setPhone(supplier.getPhone());
                setCity(supplier.getCity());
                setCountry(supplier.getCountry());
                setAddress(supplier.getAddress());
                setTaxNumber(supplier.getTaxNumber());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = suppliersRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Supplier>>() {
                }.getType();
                ArrayList<Supplier> currencyList = gson.fromJson(task.get()
                        .body(), listType);

                suppliersList.clear();
                suppliersList.addAll(currencyList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SupplierViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
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

        var task = suppliersRepository.put(supplier);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = suppliersRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}