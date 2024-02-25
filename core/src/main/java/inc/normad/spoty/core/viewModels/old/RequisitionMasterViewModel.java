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

package inc.normad.spoty.core.viewModels.old;

import inc.normad.spoty.network_bridge.dtos.Branch;
import inc.normad.spoty.network_bridge.dtos.Supplier;
import inc.normad.spoty.network_bridge.dtos.requisitions.RequisitionMaster;
import inc.normad.spoty.utils.SpotyLogger;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static inc.normad.spoty.core.values.SharedResources.PENDING_DELETES;

public class RequisitionMasterViewModel {
    @Getter
    public static final ObservableList<RequisitionMaster> requisitionMasterList =
            FXCollections.observableArrayList();
    private static final ListProperty<RequisitionMaster> requisitions =
            new SimpleListProperty<>(requisitionMasterList);
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

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
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
                    setId(0);
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

    public static void saveRequisitionMaster() throws Exception {
//        Dao<RequisitionMaster, Long> requisitionMasterDao =
//                DaoManager.createDao(connectionSource, RequisitionMaster.class);
//
//        RequisitionMaster requisitionMaster =
//                new RequisitionMaster(
//                        getDate(),
//                        getSupplier(),
//                        getBranch(),
//                        getShipVia(),
//                        getShipMethod(),
//                        getShippingTerms(),
//                        getDeliveryDate(),
//                        getNote(),
//                        getStatus(),
//                        getTotalCost());
//
//        if (!RequisitionDetailViewModel.requisitionDetailList.isEmpty()) {
//            RequisitionDetailViewModel.requisitionDetailList.forEach(
//                    requisitionDetail -> requisitionDetail.setRequisition(requisitionMaster));
//
//            requisitionMaster.setRequisitionDetails(
//                    RequisitionDetailViewModel.getRequisitionDetailList());
//        }
//
//        requisitionMasterDao.create(requisitionMaster);
//        RequisitionDetailViewModel.saveRequisitionDetails();
        getRequisitionMasters();
    }

    public static void getRequisitionMasters() throws Exception {
//        Dao<RequisitionMaster, Long> requisitionMasterDao =
//                DaoManager.createDao(connectionSource, RequisitionMaster.class);
//
//        Platform.runLater(
//                () -> {
//                    requisitionMasterList.clear();
//
//                    try {
//                        requisitionMasterList.addAll(requisitionMasterDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
//                    }
//                });
    }

    public static void getItem(long index) throws Exception {
//        Dao<RequisitionMaster, Long> requisitionMasterDao =
//                DaoManager.createDao(connectionSource, RequisitionMaster.class);
//
//        RequisitionMaster requisitionMaster = requisitionMasterDao.queryForId(index);
//
//        setId(requisitionMaster.getId());
//        setSupplier(requisitionMaster.getSupplier());
//        setBranch(requisitionMaster.getBranch());
//        setShipVia(requisitionMaster.getShipVia());
//        setShipMethod(requisitionMaster.getShipMethod());
//        setShippingTerms(requisitionMaster.getShippingTerms());
//        setNote(requisitionMaster.getNotes());
//        setStatus(requisitionMaster.getStatus());
//        setTotalCost(String.valueOf(requisitionMaster.getTotalCost()));
//        setDate(requisitionMaster.getLocaleDate());
//
//        RequisitionDetailViewModel.requisitionDetailList.clear();
//        RequisitionDetailViewModel.requisitionDetailList.addAll(
//                requisitionMaster.getRequisitionDetails());

        getRequisitionMasters();
    }

    public static void updateItem(long index) throws Exception {
//        Dao<RequisitionMaster, Long> requisitionMasterDao =
//                DaoManager.createDao(connectionSource, RequisitionMaster.class);
//
//        RequisitionMaster requisitionMaster = requisitionMasterDao.queryForId(index);
//        requisitionMaster.setDate(getDate());
//        requisitionMaster.setSupplier(getSupplier());
//        requisitionMaster.setBranch(getBranch());
//        requisitionMaster.setShipVia(getShipVia());
//        requisitionMaster.setShipMethod(getShipMethod());
//        requisitionMaster.setShippingTerms(getShippingTerms());
//        requisitionMaster.setDeliveryDate(getDeliveryDate());
//        requisitionMaster.setNotes(getNote());
//        requisitionMaster.setStatus(getStatus());
//        requisitionMaster.setTotalCost(getTotalCost());
//
//        RequisitionDetailViewModel.deleteRequisitionDetails(PENDING_DELETES);
//        requisitionMaster.setRequisitionDetails(
//                RequisitionDetailViewModel.getRequisitionDetailList());
//
//        requisitionMasterDao.update(requisitionMaster);
//        RequisitionDetailViewModel.updateRequisitionDetails();
        getRequisitionMasters();
    }

    public static void deleteItem(long index) throws Exception {
//        Dao<RequisitionMaster, Long> requisitionMasterDao =
//                DaoManager.createDao(connectionSource, RequisitionMaster.class);
//
//        requisitionMasterDao.deleteById(index);

        getRequisitionMasters();
    }
}
