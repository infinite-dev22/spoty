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

package org.infinite.spoty.viewModels.purchases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.daos.Branch;
import org.infinite.spoty.data_source.daos.Supplier;
import org.infinite.spoty.data_source.daos.purchases.PurchaseMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.PurchasesRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

public class PurchaseMasterViewModel {
    @Getter
    public static final ObservableList<PurchaseMaster> purchaseMastersList =
            FXCollections.observableArrayList();
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

    public static void savePurchaseMaster() throws IOException, InterruptedException {
        var purchaseMaster = PurchaseMaster.builder()
//                .ref(getReference())
                .date(getDate())
                .supplier(getSupplier())
                .branch(getBranch())
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

        purchasesRepository.postMaster(purchaseMaster);
        PurchaseDetailViewModel.savePurchaseDetails();

        resetProperties();
        getPurchaseMasters();
    }

    public static void getPurchaseMasters() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
        }.getType();
        purchaseMastersList.clear();
        ArrayList<PurchaseMaster> purchaseMasterList = new Gson().fromJson(
                purchasesRepository.fetchAllMaster().body(), listType);
        purchaseMastersList.addAll(purchaseMasterList);
    }

    public static void getPurchaseMaster(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = purchasesRepository.fetchMaster(findModel).body();
        var purchaseMaster = new Gson().fromJson(response, PurchaseMaster.class);

        setId(purchaseMaster.getId());
        setBranch(purchaseMaster.getBranch());
        setNote(purchaseMaster.getNotes());
        setDate(purchaseMaster.getLocaleDate());
        PurchaseDetailViewModel.purchaseDetailsList.clear();
        PurchaseDetailViewModel.purchaseDetailsList.addAll(purchaseMaster.getPurchaseDetails());
        getPurchaseMasters();
    }

    public static void searchItem(String search) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    purchaseMastersList.clear();

                    try {
                        ArrayList<PurchaseMaster> purchaseMasterList = new Gson().fromJson(
                                purchasesRepository.searchMaster(searchModel).body(), listType);
                        purchaseMastersList.addAll(purchaseMasterList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
                    }
                });
    }

    public static void updateItem() throws IOException, InterruptedException {
        var purchaseMaster = PurchaseMaster.builder()
                .id(getId())
//                .ref(getReference())
                .date(getDate())
                .supplier(getSupplier())
                .branch(getBranch())
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

        PurchaseDetailViewModel.deletePurchaseDetails(PENDING_DELETES);

        if (!PurchaseDetailViewModel.getPurchaseDetailsList().isEmpty()) {
            PurchaseDetailViewModel.getPurchaseDetailsList()
                    .forEach(adjustmentDetail -> adjustmentDetail.setPurchase(purchaseMaster));

            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.getPurchaseDetails());
        }
        purchasesRepository.putMaster(purchaseMaster);
        PurchaseDetailViewModel.updatePurchaseDetails();
        Platform.runLater(PurchaseMasterViewModel::resetProperties);
        resetProperties();
        getPurchaseMasters();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        purchasesRepository.deleteMaster(findModel);
        getPurchaseMasters();
    }
}
