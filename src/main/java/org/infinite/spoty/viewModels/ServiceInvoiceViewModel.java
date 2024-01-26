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
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.dtos.ServiceInvoice;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.ServiceInvoicesRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.infinite.spoty.viewModels.quotations.QuotationMasterViewModel;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ServiceInvoiceViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ServiceInvoicesRepositoryImpl serviceInvoicesRepository = new ServiceInvoicesRepositoryImpl();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty customerName = new SimpleStringProperty("");
    private static final StringProperty date = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>();
    public static ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    private static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
    private static final ObjectProperty<ServiceInvoice> serviceInvoice = new SimpleObjectProperty<>();
    public static ObservableList<ServiceInvoice> serviceInvoicesList = FXCollections.observableArrayList();
    private static final ListProperty<ServiceInvoice> serviceInvoices = new SimpleListProperty<>(serviceInvoicesList);

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        ServiceInvoiceViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getCustomerName() {
        return customerName.get();
    }

    public static void setCustomerName(String customerName) {
        ServiceInvoiceViewModel.customerName.set(customerName);
    }

    public static StringProperty nameProperty() {
        return customerName;
    }

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, QuotationMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        ServiceInvoiceViewModel.date.set(date);
    }

    public static StringProperty emailProperty() {
        return date;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        ServiceInvoiceViewModel.description.set(description);
    }

    public static StringProperty townProperty() {
        return description;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        ServiceInvoiceViewModel.branch.set(branch);
    }

    public static ObservableList<ServiceInvoice> getServiceInvoices() {
        return serviceInvoices.get();
    }

    public static void setServiceInvoicesList(ObservableList<ServiceInvoice> serviceInvoicesList) {
        ServiceInvoiceViewModel.serviceInvoicesList = serviceInvoicesList;
    }

    public static ListProperty<ServiceInvoice> servicesProperty() {
        return serviceInvoices;
    }

    public static void saveServiceInvoice() throws Exception {

        var serviceInvoice =
                ServiceInvoice.builder()
                        .branch(getBranch())
                        .customerName(getCustomerName())
                        .date(getDate())
                        .description(getDescription())
                        .build();
        serviceInvoicesRepository.post(serviceInvoice);

        clearServiceInvoiceData();
        getAllServiceInvoices();
    }

    public static void clearServiceInvoiceData() {
        setId(0L);
        setCustomerName("");
        setDate("");
        setDescription("");
    }

    public static void getAllServiceInvoices() {
        Type listType = new TypeToken<ArrayList<ServiceInvoice>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    serviceInvoicesList.clear();

                    try {
                        ArrayList<ServiceInvoice> serviceInvoiceList = gson.fromJson(serviceInvoicesRepository.fetchAll().body(), listType);
                        serviceInvoicesList.addAll(serviceInvoiceList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, ServiceInvoiceViewModel.class);
                    }
                });
    }

    public static void getItem(Long index) throws Exception {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = serviceInvoicesRepository.fetch(findModel).body();
        var serviceInvoice = gson.fromJson(response, ServiceInvoice.class);

        setBranch(serviceInvoice.getBranch());
        setId(serviceInvoice.getId());
        setCustomerName(serviceInvoice.getCustomerName());
        setDate(serviceInvoice.getLocaleDate());
        setDescription(serviceInvoice.getDescription());

        getAllServiceInvoices();
    }

    public static void searchItem(String search) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<ServiceInvoice>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    serviceInvoicesList.clear();

                    try {
                        ArrayList<ServiceInvoice> serviceInvoiceList = gson.fromJson(serviceInvoicesRepository.search(searchModel).body(), listType);
                        serviceInvoicesList.addAll(serviceInvoiceList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, ServiceInvoiceViewModel.class);
                    }
                });

    }

    public static void updateItem() throws IOException, InterruptedException {

        var serviceInvoice = ServiceInvoice.builder()
                .id(getId())
                .branch(getBranch())
                .customerName(getCustomerName())
                .date(getDate())
                .description(getDescription())
                .build();

        serviceInvoicesRepository.put(serviceInvoice);
        clearServiceInvoiceData();
        getAllServiceInvoices();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        serviceInvoicesRepository.delete(findModel);
        getAllServiceInvoices();
    }
}
