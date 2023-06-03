package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.PurchaseReturnMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.PurchaseReturnMaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PurchaseReturnMasterViewModel {
    public static final ObservableList<PurchaseReturnMaster> purchaseReturnMasterList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        PurchaseReturnMasterViewModel.id.set(id);
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
        PurchaseReturnMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        PurchaseReturnMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        PurchaseReturnMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        PurchaseReturnMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        PurchaseReturnMasterViewModel.totalCost.set(totalCost);
    }

    public static StringProperty totalCostProperty() {
        return totalCost;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setBranch(null);
        setNote("");
        setStatus("");
        setTotalCost("");
    }

//    public static void savePurchaseReturnMaster() {
//        PurchaseReturnMaster purchaseReturnMaster = new PurchaseReturnMaster(getDate(),
//                getBranch(),
//                getStatus(),
//                getNote());
//        purchaseReturnMaster.setPurchaseReturnDetails(PurchaseReturnDetailViewModel.purchaseReturnDetailsTempList);
//        PurchaseReturnMasterDao.savePurchaseReturnMaster(purchaseReturnMaster);
//        resetProperties();
//        PurchaseReturnDetailViewModel.purchaseReturnDetailsTempList.clear();
//        getPurchaseReturnMasters();
//    }

    public static ObservableList<PurchaseReturnMaster> getPurchaseReturnMasters() {
        purchaseReturnMasterList.clear();
        purchaseReturnMasterList.addAll(PurchaseReturnMasterDao.fetchPurchaseReturnMasters());
        return purchaseReturnMasterList;
    }

}
