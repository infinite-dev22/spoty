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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.Service;
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.dtos.Service;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.ServicesRepositoryImpl;
import org.infinite.spoty.data_source.repositories.implementations.ServicesRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


public class ServiceViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ServicesRepositoryImpl servicesRepository = new ServicesRepositoryImpl();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty charge = new SimpleStringProperty("");
    private static final StringProperty vat = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>();
    public static ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    private static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
    private static final ObjectProperty<Service> service = new SimpleObjectProperty<>();
    public static ObservableList<Service> servicesList = FXCollections.observableArrayList();
    private static final ListProperty<Service> services = new SimpleListProperty<>(servicesList);

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        ServiceViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ServiceViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getCharge() {
        return charge.get();
    }

    public static void setCharge(String charge) {
        ServiceViewModel.charge.set(charge);
    }

    public static StringProperty emailProperty() {
        return charge;
    }

    public static String getVat() {
        return vat.get();
    }

    public static void setVat(String vat) {
        ServiceViewModel.vat.set(vat);
    }

    public static StringProperty phoneProperty() {
        return vat;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        ServiceViewModel.description.set(description);
    }

    public static StringProperty townProperty() {
        return description;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        ServiceViewModel.branch.set(branch);
    }

    public static ObservableList<Service> getServices() {
        return services.get();
    }

    public static void setServicesList(ObservableList<Service> servicesList) {
        ServiceViewModel.servicesList = servicesList;
    }

    public static ListProperty<Service> servicesProperty() {
        return services;
    }

    public static void saveService() throws Exception {

        var service =
                Service.builder()
                        .branch(getBranch())
                        .name(getName())
                        .charge(getCharge())
                        .vat(getVat())
                        .description(getDescription())
                        .build();
        servicesRepository.post(service);

        clearServiceData();
        getAllServices();
    }

    public static void clearServiceData() {
        setId(0L);
        setName("");
        setVat("");
        setCharge("");
        setDescription("");
    }

    public static void getAllServices() {
        Type listType = new TypeToken<ArrayList<Service>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    servicesList.clear();

                    try {
                        ArrayList<Service> serviceList = gson.fromJson(servicesRepository.fetchAll().body(), listType);
                        servicesList.addAll(serviceList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, ServiceViewModel.class);
                    }
                });
    }

    public static void getItem(Long index) throws Exception {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = servicesRepository.fetch(findModel).body();
        var service = gson.fromJson(response, Service.class);

        setBranch(service.getBranch());
        setId(service.getId());
        setName(service.getName());
        setCharge(service.getCharge());
        setVat(service.getVat());
        setDescription(service.getDescription());

        getAllServices();
    }

    public static void searchItem(String search) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<Service>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    servicesList.clear();

                    try {
                        ArrayList<Service> serviceList = gson.fromJson(servicesRepository.search(searchModel).body(), listType);
                        servicesList.addAll(serviceList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, ServiceViewModel.class);
                    }
                });

    }

    public static void updateItem() throws IOException, InterruptedException {

        var service = Service.builder()
                .id(getId())
                .branch(getBranch())
                .name(getName())
                .charge(getCharge())
                .vat(getVat())
                .description(getDescription())
                .build();

        servicesRepository.put(service);
        clearServiceData();
        getAllServices();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        servicesRepository.delete(findModel);
        getAllServices();
    }
}
