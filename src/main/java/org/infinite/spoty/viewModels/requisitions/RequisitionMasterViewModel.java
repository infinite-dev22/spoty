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

package org.infinite.spoty.viewModels.requisitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.dtos.Supplier;
import org.infinite.spoty.data_source.dtos.requisitions.RequisitionMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.RequisitionsRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

public class RequisitionMasterViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    @Getter
    public static final ObservableList<RequisitionMaster> requisitionMastersList =
            FXCollections.observableArrayList();
    private static final ListProperty<RequisitionMaster> requisitions =
            new SimpleListProperty<>(requisitionMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty shipVia = new SimpleStringProperty("");
    private static final StringProperty shipMethod = new SimpleStringProperty("");
    private static final StringProperty shippingTerms = new SimpleStringProperty("");
    private static final StringProperty deliveryDate = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty totalCost = new SimpleStringProperty("");
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

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        RequisitionMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        RequisitionMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
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

    public static String getShipVia() {
        return shipVia.get();
    }

    public static void setShipVia(String shipVia) {
        RequisitionMasterViewModel.shipVia.set(shipVia);
    }

    public static StringProperty shipViaProperty() {
        return shipVia;
    }

    public static String getShipMethod() {
        return shipMethod.get();
    }

    public static void setShipMethod(String shipMethod) {
        RequisitionMasterViewModel.shipMethod.set(shipMethod);
    }

    public static StringProperty shipMethodProperty() {
        return shipMethod;
    }

    public static String getShippingTerms() {
        return shippingTerms.get();
    }

    public static void setShippingTerms(String shippingTerms) {
        RequisitionMasterViewModel.shippingTerms.set(shippingTerms);
    }

    public static StringProperty shippingTermsProperty() {
        return shippingTerms;
    }

    public static @Nullable Date getDeliveryDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setDeliveryDate(String deliveryDate) {
        RequisitionMasterViewModel.deliveryDate.set(deliveryDate);
    }

    public static StringProperty deliveryDateProperty() {
        return deliveryDate;
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

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        RequisitionMasterViewModel.totalCost.set(totalCost);
    }

    public static StringProperty totalCostProperty() {
        return totalCost;
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
                    setShipVia("");
                    setShipMethod("");
                    setShippingTerms("");
                    setDeliveryDate("");
                    setNote("");
                    setStatus("");
                    setTotalCost("");
                    PENDING_DELETES.clear();
                });
    }

    public static void saveRequisitionMaster() throws IOException, InterruptedException {
        var requisitionMaster = RequisitionMaster.builder()
                .date(getDate())
                .supplier(getSupplier())
                .branch(getBranch())
                .shipVia(getShipVia())
                .shipMethod(getShipMethod())
                .shippingTerms(getShippingTerms())
                .deliveryDate(getDeliveryDate())
                .notes(getNote())
                .status(getStatus())
                .totalCost(getTotalCost())
                .build();

        if (!RequisitionDetailViewModel.requisitionDetailsList.isEmpty()) {
            RequisitionDetailViewModel.requisitionDetailsList.forEach(
                    requisitionDetail -> requisitionDetail.setRequisition(requisitionMaster));

            requisitionMaster.setRequisitionDetails(
                    RequisitionDetailViewModel.getRequisitionDetailsList());
        }

        requisitionsRepository.postMaster(requisitionMaster);
        RequisitionDetailViewModel.saveRequisitionDetails();
        resetProperties();
        getRequisitionMasters();
    }

    public static void getRequisitionMasters() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<RequisitionMaster>>() {
        }.getType();
        requisitionMastersList.clear();
        ArrayList<RequisitionMaster> requisitionMasterList = gson.fromJson(
                requisitionsRepository.fetchAllMaster().body(), listType);
        requisitionMastersList.addAll(requisitionMasterList);
    }

    public static void getItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = requisitionsRepository.fetchMaster(findModel).body();
        var requisitionMaster = gson.fromJson(response, RequisitionMaster.class);

        setId(requisitionMaster.getId());
        setSupplier(requisitionMaster.getSupplier());
        setBranch(requisitionMaster.getBranch());
        setShipVia(requisitionMaster.getShipVia());
        setShipMethod(requisitionMaster.getShipMethod());
        setShippingTerms(requisitionMaster.getShippingTerms());
        setNote(requisitionMaster.getNotes());
        setStatus(requisitionMaster.getStatus());
        setTotalCost(String.valueOf(requisitionMaster.getTotalCost()));
        setDate(requisitionMaster.getLocaleDate());
        RequisitionDetailViewModel.requisitionDetailsList.clear();
        RequisitionDetailViewModel.requisitionDetailsList.addAll(
                requisitionMaster.getRequisitionDetails());
        getRequisitionMasters();
    }

    public static void searchItem(String search) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<RequisitionMaster>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    requisitionMastersList.clear();

                    try {
                        ArrayList<RequisitionMaster> requisitionMasterList = gson.fromJson(
                                requisitionsRepository.searchMaster(searchModel).body(), listType);
                        requisitionMastersList.addAll(requisitionMasterList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
                    }
                });
    }

    public static void updateItem(Long index) throws IOException, InterruptedException {
        var requisitionMaster = RequisitionMaster.builder()
                .id(index)
                .date(getDate())
                .supplier(getSupplier())
                .branch(getBranch())
                .shipVia(getShipVia())
                .shipMethod(getShipMethod())
                .shippingTerms(getShippingTerms())
                .deliveryDate(getDeliveryDate())
                .notes(getNote())
                .status(getStatus())
                .totalCost(getTotalCost())
                .build();

        RequisitionDetailViewModel.deleteRequisitionDetails(PENDING_DELETES);
        requisitionMaster.setRequisitionDetails(
                RequisitionDetailViewModel.getRequisitionDetailsList());

        requisitionsRepository.putMaster(requisitionMaster);
        RequisitionDetailViewModel.updateRequisitionDetails();

        resetProperties();
        getRequisitionMasters();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        requisitionsRepository.deleteMaster(findModel);
        getRequisitionMasters();
    }
}
