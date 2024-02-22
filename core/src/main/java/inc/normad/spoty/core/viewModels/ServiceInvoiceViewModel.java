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

package inc.normad.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import inc.normad.spoty.network_bridge.dtos.Branch;
import inc.normad.spoty.network_bridge.dtos.ServiceInvoice;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.ServiceInvoicesRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.normad.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


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
    private static final ObjectProperty<ServiceInvoice> serviceInvoice = new SimpleObjectProperty<>();
    public static ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    private static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
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

    public static void saveServiceInvoice(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var serviceInvoice =
                ServiceInvoice.builder()
                        .customerName(getCustomerName())
                        .date(getDate())
                        .description(getDescription())
                        .build();

        var task = serviceInvoicesRepository.post(serviceInvoice);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void clearServiceInvoiceData() {
        setId(0L);
        setCustomerName("");
        setDate("");
        setDescription("");
    }

    public static void getAllServiceInvoices(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = serviceInvoicesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<ServiceInvoice>>() {
                }.getType();
                ArrayList<ServiceInvoice> serviceInvoiceList = gson.fromJson(task.get().body(), listType);

                serviceInvoicesList.clear();
                serviceInvoicesList.addAll(serviceInvoiceList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = serviceInvoicesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var serviceInvoice = gson.fromJson(task.get().body(), ServiceInvoice.class);

                setId(serviceInvoice.getId());
                setCustomerName(serviceInvoice.getCustomerName());
                setDate(serviceInvoice.getLocaleDate());
                setDescription(serviceInvoice.getDescription());
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ServiceInvoiceViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = serviceInvoicesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<ServiceInvoice>>() {
                }.getType();
                ArrayList<ServiceInvoice> serviceInvoiceList = gson.fromJson(task.get().body(), listType);

                serviceInvoicesList.clear();
                serviceInvoicesList.addAll(serviceInvoiceList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, ServiceInvoiceViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var serviceInvoice = ServiceInvoice.builder()
                .id(getId())
                .customerName(getCustomerName())
                .date(getDate())
                .description(getDescription())
                .build();

        var task = serviceInvoicesRepository.put(serviceInvoice);
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

        var task = serviceInvoicesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
