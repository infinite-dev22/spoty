package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.SaleReturnMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.SaleReturnMaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleReturnMasterViewModel {
    public static final ObservableList<SaleReturnMaster> saleReturnMasterList = FXCollections.observableArrayList();
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
        SaleReturnMasterViewModel.id.set(id);
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
        SaleReturnMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        SaleReturnMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        SaleReturnMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        SaleReturnMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        SaleReturnMasterViewModel.totalCost.set(totalCost);
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

//    public static void saveSaleReturnMaster() {
//        SaleReturnMaster saleReturnMaster = new SaleReturnMaster(getDate(),
//                getBranch(),
//                getStatus(),
//                getNote());
//        saleReturnMaster.setSaleReturnDetails(SaleReturnDetailViewModel.saleReturnDetailsTempList);
//        SaleReturnMasterDao.saveSaleReturnMaster(saleReturnMaster);
//        resetProperties();
//        SaleReturnDetailViewModel.saleReturnDetailsTempList.clear();
//        getSaleReturnMasters();
//    }

    public static ObservableList<SaleReturnMaster> getSaleReturnMasters() {
        saleReturnMasterList.clear();
        saleReturnMasterList.addAll(SaleReturnMasterDao.fetchSaleReturnMasters());
        return saleReturnMasterList;
    }

}
