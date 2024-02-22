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

package inc.normad.spoty.core.viewModels.purchases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import inc.normad.spoty.network_bridge.dtos.Branch;
import inc.normad.spoty.network_bridge.dtos.Supplier;
import inc.normad.spoty.network_bridge.dtos.purchases.PurchaseMaster;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.PurchasesRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.normad.spoty.core.viewModels.adjustments.AdjustmentMasterViewModel;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.normad.spoty.core.values.SharedResources.PENDING_DELETES;

public class PurchaseMasterViewModel {
    @Getter
    public static final ObservableList<PurchaseMaster> purchaseMastersList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<PurchaseMaster> purchases =
            new SimpleListProperty<>(purchaseMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final PurchasesRepositoryImpl purchasesRepository = new PurchasesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        PurchaseMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Supplier getSupplier() {
        return supplier.get();
    }

    public static void setSupplier(Supplier supplier) {
        PurchaseMasterViewModel.supplier.set(supplier);
    }

    public static ObjectProperty<Supplier> supplierProperty() {
        return supplier;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        PurchaseMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        PurchaseMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static String getNotes() {
        return note.get();
    }

    public static void setNote(String note) {
        PurchaseMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<PurchaseMaster> getPurchases() {
        return purchases.get();
    }

    public static void setPurchases(ObservableList<PurchaseMaster> purchases) {
        PurchaseMasterViewModel.purchases.set(purchases);
    }

    public static ListProperty<PurchaseMaster> purchasesProperty() {
        return purchases;
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

    public static void savePurchaseMaster(ParameterlessConsumer onActivity, ParameterlessConsumer onSuccess, ParameterlessConsumer onFailed) throws IOException, InterruptedException {
        var purchaseMaster = PurchaseMaster.builder()
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

        if (!PurchaseDetailViewModel.purchaseDetailsList.isEmpty()) {
            PurchaseDetailViewModel.purchaseDetailsList.forEach(
                    purchaseDetail -> purchaseDetail.setPurchase(purchaseMaster));
            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.purchaseDetailsList);
        }

        var task = purchasesRepository.postMaster(purchaseMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            PurchaseDetailViewModel.savePurchaseDetails(onActivity, null, onFailed);
            onSuccess.run();
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        Platform.runLater(AdjustmentMasterViewModel::resetProperties);

        // resetProperties();
        // getPurchaseMasters();
    }

    public static void getPurchaseMasters(ParameterlessConsumer onActivity, ParameterlessConsumer onFailed) {
        Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
        }.getType();
        var task = purchasesRepository.fetchAllMaster();
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            purchaseMastersList.clear();
            ArrayList<PurchaseMaster> purchaseMasterList = new ArrayList<>();
            try {
                purchaseMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
            }
            purchaseMastersList.addAll(purchaseMasterList);
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getPurchaseMaster(Long index, ParameterlessConsumer onActivity, ParameterlessConsumer onSuccess, ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = purchasesRepository.fetchMaster(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> {
                PurchaseMaster purchaseMaster = new PurchaseMaster();
                try {
                    purchaseMaster = gson.fromJson(task.get().body(), PurchaseMaster.class);
                } catch (InterruptedException | ExecutionException e) {
                    SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
                }

                setId(purchaseMaster.getId());
                setNote(purchaseMaster.getNotes());
                setDate(purchaseMaster.getLocaleDate());
                PurchaseDetailViewModel.purchaseDetailsList.clear();
                PurchaseDetailViewModel.purchaseDetailsList.addAll(purchaseMaster.getPurchaseDetails());

                onSuccess.run();
            });
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        SpotyThreader.spotyThreadPool(task);
        // getPurchaseMasters();
    }

    public static void searchItem(String search, @Nullable ParameterlessConsumer onActivity, @Nullable ParameterlessConsumer onFailed) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        var task = purchasesRepository.searchMaster(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
            }.getType();
            ArrayList<PurchaseMaster> purchaseMasterList = new ArrayList<>();
            try {
                purchaseMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
            }

            purchaseMastersList.clear();
            purchaseMastersList.addAll(purchaseMasterList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(ParameterlessConsumer onActivity, ParameterlessConsumer onSuccess, ParameterlessConsumer onFailed) throws IOException, InterruptedException {
        var purchaseMaster = PurchaseMaster.builder()
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
            PurchaseDetailViewModel.deletePurchaseDetails(PENDING_DELETES, onActivity, null, onFailed);
        }

        if (!PurchaseDetailViewModel.getPurchaseDetailsList().isEmpty()) {
            PurchaseDetailViewModel.getPurchaseDetailsList()
                    .forEach(adjustmentDetail -> adjustmentDetail.setPurchase(purchaseMaster));

            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.getPurchaseDetails());
        }

        var task = purchasesRepository.putMaster(purchaseMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> PurchaseDetailViewModel.updatePurchaseDetails(onActivity, onSuccess, onFailed));
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        // getPurchaseMasters();
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = purchasesRepository.deleteMaster(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
        // getPurchaseMasters();
    }
}
