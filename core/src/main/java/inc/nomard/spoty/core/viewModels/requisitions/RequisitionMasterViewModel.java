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

package inc.nomard.spoty.core.viewModels.requisitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.core.viewModels.adjustments.AdjustmentMasterViewModel;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.requisitions.RequisitionMaster;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.RequisitionsRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

public class RequisitionMasterViewModel {
    @Getter
    public static final ObservableList<RequisitionMaster> requisitionMastersList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<RequisitionMaster> requisitions =
            new SimpleListProperty<>(requisitionMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final RequisitionsRepositoryImpl requisitionsRepository = new RequisitionsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        RequisitionMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        RequisitionMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Supplier getSupplier() {
        return supplier.get();
    }

    public static void setSupplier(Supplier supplier) {
        RequisitionMasterViewModel.supplier.set(supplier);
    }

    public static ObjectProperty<Supplier> supplierProperty() {
        return supplier;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        RequisitionMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        RequisitionMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static String getNotes() {
        return note.get();
    }

    public static void setNote(String note) {
        RequisitionMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<RequisitionMaster> getRequisitions() {
        return requisitions.get();
    }

    public static void setRequisitions(ObservableList<RequisitionMaster> requisitions) {
        RequisitionMasterViewModel.requisitions.set(requisitions);
    }

    public static ListProperty<RequisitionMaster> requisitionsProperty() {
        return requisitions;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setDate("");
                    setSupplier(null);
                    setBranch(null);
                    setStatus("");
                    setNote("");
                    PENDING_DELETES.clear();
                });
    }

    public static void saveRequisitionMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var requisitionMaster = RequisitionMaster.builder()
//                .ref(getReference())
                .date(getDate())
                .supplier(getSupplier())
//                .taxRate(getTaxRate())
//                .netTax(getNetTax())
//                .discount(getDiscount())
//                .shipping(getShipping())
//                .paid(getAmountPaid())
//                .total(getTotalAmount())
//                .due(getDueAmount())
                .status(getStatus())
//                .paymentStatus(getPaymentStatus())
                .notes(getNotes())
                .build();

        if (!RequisitionDetailViewModel.requisitionDetailsList.isEmpty()) {
            RequisitionDetailViewModel.requisitionDetailsList.forEach(
                    requisitionDetail -> requisitionDetail.setRequisition(requisitionMaster));
            requisitionMaster.setRequisitionDetails(RequisitionDetailViewModel.requisitionDetailsList);
        }

        var task = requisitionsRepository.postMaster(requisitionMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            RequisitionDetailViewModel.saveRequisitionDetails(onActivity, null, onFailed);
            onSuccess.run();
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        Platform.runLater(AdjustmentMasterViewModel::resetProperties);

        // resetProperties();
        // getRequisitionMasters();
    }

    public static void getRequisitionMasters(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        Type listType = new TypeToken<ArrayList<RequisitionMaster>>() {
        }.getType();
        var task = requisitionsRepository.fetchAllMaster();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            requisitionMastersList.clear();
            ArrayList<RequisitionMaster> requisitionMasterList = new ArrayList<>();
            try {
                requisitionMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
            }
            requisitionMastersList.addAll(requisitionMasterList);


            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getRequisitionMaster(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = requisitionsRepository.fetchMaster(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> {
                RequisitionMaster requisitionMaster = new RequisitionMaster();
                try {
                    requisitionMaster = gson.fromJson(task.get().body(), RequisitionMaster.class);
                } catch (InterruptedException | ExecutionException e) {
                    SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
                }

                setId(requisitionMaster.getId());
                setNote(requisitionMaster.getNotes());
                setDate(requisitionMaster.getLocaleDate());
                RequisitionDetailViewModel.requisitionDetailsList.clear();
                RequisitionDetailViewModel.requisitionDetailsList.addAll(requisitionMaster.getRequisitionDetails());

                onSuccess.run();
            });
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        SpotyThreader.spotyThreadPool(task);
        // getRequisitionMasters();
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        var task = requisitionsRepository.searchMaster(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<RequisitionMaster>>() {
            }.getType();
            ArrayList<RequisitionMaster> requisitionMasterList = new ArrayList<>();
            try {
                requisitionMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
            }

            requisitionMastersList.clear();
            requisitionMastersList.addAll(requisitionMasterList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var requisitionMaster = RequisitionMaster.builder()
                .id(getId())
//                .ref(getReference())
                .date(getDate())
                .supplier(getSupplier())
//                .taxRate(getTaxRate())
//                .netTax(getNetTax())
//                .discount(getDiscount())
//                .shipping(getShipping())
//                .paid(getAmountPaid())
//                .total(getTotalAmount())
//                .due(getDueAmount())
                .status(getStatus())
//                .paymentStatus(getPaymentStatus())
                .notes(getNotes())
                .build();

        if (!PENDING_DELETES.isEmpty()) {
            RequisitionDetailViewModel.deleteRequisitionDetails(PENDING_DELETES, onActivity, null, onFailed);
        }

        if (!RequisitionDetailViewModel.getRequisitionDetailsList().isEmpty()) {
            RequisitionDetailViewModel.getRequisitionDetailsList()
                    .forEach(adjustmentDetail -> adjustmentDetail.setRequisition(requisitionMaster));

            requisitionMaster.setRequisitionDetails(RequisitionDetailViewModel.getRequisitionDetails());
        }

        var task = requisitionsRepository.putMaster(requisitionMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> RequisitionDetailViewModel.updateRequisitionDetails(onActivity, onSuccess, onFailed));
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        // getRequisitionMasters();
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = requisitionsRepository.deleteMaster(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        // getRequisitionMasters();
    }
}
