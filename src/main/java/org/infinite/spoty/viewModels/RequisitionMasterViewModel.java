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

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.RequisitionMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.RequisitionMaster;
import org.infinite.spoty.database.models.Supplier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequisitionMasterViewModel {
    public static final ObservableList<RequisitionMaster> requisitionMasterList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
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

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        RequisitionMasterViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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

    public static Date getDeliveryDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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

    public static void resetProperties() {
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
    }

    public static void saveRequisitionMaster() {
        RequisitionMaster requisitionMaster = new RequisitionMaster(getDate(),
                getSupplier(),
                getBranch(),
                getShipVia(),
                getShipMethod(),
                getShippingTerms(),
                getDeliveryDate(),
                getNote(),
                getStatus(),
                getTotalCost());
        requisitionMaster.setRequisitionDetails(RequisitionDetailViewModel.requisitionDetailsTempList);
        RequisitionMasterDao.saveRequisitionMaster(requisitionMaster);
        resetProperties();
        RequisitionDetailViewModel.requisitionDetailsTempList.clear();
        getRequisitionMasters();
    }

    public static ObservableList<RequisitionMaster> getRequisitionMasters() {
        requisitionMasterList.clear();
        requisitionMasterList.addAll(RequisitionMasterDao.fetchRequisitionMasters());
        return requisitionMasterList;
    }

    public static void getItem(int requisitionMasterID) {
        RequisitionMaster requisitionMaster = RequisitionMasterDao.findRequisitionMaster(requisitionMasterID);
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
        RequisitionDetailViewModel.requisitionDetailsTempList.addAll(requisitionMaster.getRequisitionDetails());
        getRequisitionMasters();
    }

    public static void updateItem(int requisitionMasterID) {
        RequisitionMaster requisitionMaster = new RequisitionMaster(getDate(),
                getSupplier(),
                getBranch(),
                getShipVia(),
                getShipMethod(),
                getShippingTerms(),
                getDeliveryDate(),
                getNote(),
                getStatus(),
                getTotalCost());
        requisitionMaster.setRequisitionDetails(RequisitionDetailViewModel.requisitionDetailsTempList);
        RequisitionMasterDao.updateRequisitionMaster(requisitionMaster, requisitionMasterID);
        resetProperties();
        RequisitionDetailViewModel.requisitionDetailsTempList.clear();
        getRequisitionMasters();
    }
}
