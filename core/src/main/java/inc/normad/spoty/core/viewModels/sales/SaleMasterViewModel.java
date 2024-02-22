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

package inc.normad.spoty.core.viewModels.sales;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import inc.normad.spoty.network_bridge.dtos.Branch;
import inc.normad.spoty.network_bridge.dtos.Customer;
import inc.normad.spoty.network_bridge.dtos.sales.SaleMaster;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.SalesRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.normad.spoty.core.values.SharedResources.PENDING_DELETES;

public class SaleMasterViewModel {
    @Getter
    public static final ObservableList<SaleMaster> saleMastersList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<SaleMaster> sales = new SimpleListProperty<>(saleMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final StringProperty ref = new SimpleStringProperty("");
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty saleStatus = new SimpleStringProperty("");
    private static final DoubleProperty total = new SimpleDoubleProperty();
    private static final DoubleProperty paid = new SimpleDoubleProperty();
    private static final StringProperty payStatus = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final SalesRepositoryImpl salesRepository = new SalesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        SaleMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

            if (Objects.equals(date.get(), "")) {
                setDate(dateFormat.format(new Date()));
            }

            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, SaleMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        SaleMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        SaleMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        SaleMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getSaleStatus() {
        return saleStatus.get();
    }

    public static void setSaleStatus(String saleStatus) {
        SaleMasterViewModel.saleStatus.set(saleStatus);
    }

    public static StringProperty saleStatusProperty() {
        return saleStatus;
    }

    public static String getPayStatus() {
        return payStatus.get();
    }

    public static void setPayStatus(String payStatus) {
        SaleMasterViewModel.payStatus.set(payStatus);
    }

    public static StringProperty payStatusProperty() {
        return payStatus;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        SaleMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<SaleMaster> getSales() {
        return sales.get();
    }

    public static void setSales(ObservableList<SaleMaster> sales) {
        SaleMasterViewModel.sales.set(sales);
    }

    public static ListProperty<SaleMaster> salesProperty() {
        return sales;
    }

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        SaleMasterViewModel.total.set(total);
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static double getPaid() {
        return paid.get();
    }

    public static void setPaid(double paid) {
        SaleMasterViewModel.paid.set(paid);
    }

    public static DoubleProperty paidProperty() {
        return paid;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setDate("");
                    setCustomer(null);
                    setBranch(null);
                    setSaleStatus("");
                    setNote("");
                    setTotal(0);
                    setPaid(0);
                    PENDING_DELETES.clear();
                    SaleDetailViewModel.resetProperties();
                });
    }

    public static void saveSaleMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var saleMaster = SaleMaster.builder()
                .customer(getCustomer())
                .total(getTotal())
                .amountPaid(getPaid())
                .saleStatus(getSaleStatus())
                .paymentStatus(getPayStatus())
                .notes(getNote())
                .date(getDate())
                .build();

        if (!SaleDetailViewModel.saleDetailsList.isEmpty()) {
            SaleDetailViewModel.saleDetailsList.forEach(
                    saleDetail -> saleDetail.setSale(saleMaster));

            saleMaster.setSaleDetails(SaleDetailViewModel.saleDetailsList);
        }

        var task = salesRepository.postMaster(saleMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            SaleDetailViewModel.saveSaleDetails(onActivity, null, onFailed);
            onSuccess.run();
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getSaleMasters(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = salesRepository.fetchAllMaster();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<SaleMaster>>() {
            }.getType();
            ArrayList<SaleMaster> saleMasterList = new ArrayList<>();
            try {
                saleMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SaleMasterViewModel.class);
            }

            saleMastersList.clear();
            saleMastersList.addAll(saleMasterList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) throws IOException, InterruptedException {
        var findModel = FindModel.builder().id(index).build();
        var task = salesRepository.fetchMaster(findModel);

        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            SaleMaster saleMaster = new SaleMaster();
            try {
                saleMaster = gson.fromJson(task.get().body(), SaleMaster.class);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SaleMasterViewModel.class);
            }

            setId(saleMaster.getId());
            setDate(saleMaster.getLocaleDate());
            setCustomer(saleMaster.getCustomer());
            setNote(saleMaster.getNotes());
            setSaleStatus(saleMaster.getSaleStatus());
            setPayStatus(saleMaster.getPaymentStatus());
            SaleDetailViewModel.saleDetailsList.clear();
            SaleDetailViewModel.saleDetailsList.addAll(saleMaster.getSaleDetails());
        });
        SpotyThreader.spotyThreadPool(task);
        // getSaleMasters();
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = salesRepository.searchMaster(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<SaleMaster>>() {
            }.getType();
            ArrayList<SaleMaster> saleMasterList = new ArrayList<>();
            try {
                saleMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SaleMasterViewModel.class);
            }

            saleMastersList.clear();
            saleMastersList.addAll(saleMasterList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var saleMaster = SaleMaster.builder()
                .id(getId())
                .customer(getCustomer())
                .total(getTotal())
                .amountPaid(getPaid())
                .saleStatus(getSaleStatus())
                .paymentStatus(getPayStatus())
                .notes(getNote())
                .date(getDate())
                .build();

        if (!PENDING_DELETES.isEmpty()) {
            SaleDetailViewModel.deleteSaleDetails(PENDING_DELETES, onActivity, null, onFailed);
        }

        if (!SaleDetailViewModel.getSaleDetailsList().isEmpty()) {
            SaleDetailViewModel.getSaleDetailsList()
                    .forEach(saleDetail -> saleDetail.setSale(saleMaster));

            saleMaster.setSaleDetails(SaleDetailViewModel.getSaleDetailsList());
        }

        var task = salesRepository.putMaster(saleMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> SaleDetailViewModel.updateSaleDetails(onActivity, onSuccess, onFailed));
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        // getSaleMasters();
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = salesRepository.deleteMaster(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        // getSaleMasters();
    }
}
