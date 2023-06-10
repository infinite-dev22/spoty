package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.StockInMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.StockInMaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockInMasterViewModel {
    public static final ObservableList<StockInMaster> stockInMasterList = FXCollections.observableArrayList();
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
        StockInMasterViewModel.id.set(id);
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
        StockInMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        StockInMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        StockInMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        StockInMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        StockInMasterViewModel.totalCost.set(totalCost);
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

    public static void saveStockInMaster() {
        StockInMaster stockInMaster = new StockInMaster(getDate(),
                getBranch(),
                getStatus(),
                getNote());
        stockInMaster.setStockInDetails(StockInDetailViewModel.stockInDetailsTempList);
        StockInMasterDao.saveStockInMaster(stockInMaster);
        resetProperties();
        StockInDetailViewModel.stockInDetailsTempList.clear();
        getStockInMasters();
    }

    public static ObservableList<StockInMaster> getStockInMasters() {
        stockInMasterList.clear();
        stockInMasterList.addAll(StockInMasterDao.fetchStockInMasters());
        return stockInMasterList;
    }

    public static void getItem(int stockInMasterID) {
        StockInMaster stockInMaster = StockInMasterDao.findStockInMaster(stockInMasterID);
        setId(stockInMaster.getId());
        setDate(stockInMaster.getLocaleDate());
        setBranch(stockInMaster.getBranch());
        setTotalCost(String.valueOf(stockInMaster.getTotalCost()));
        setStatus(stockInMaster.getStatus());
        setNote(stockInMaster.getNotes());
        StockInDetailViewModel.stockInDetailsTempList.addAll(stockInMaster.getStockInDetails());
        stockInMaster.getStockInDetails().forEach(System.out::println);
        getStockInMasters();
    }

    public static void updateItem(int stockInMasterID) {
        StockInMaster stockInMaster = new StockInMaster(getDate(),
                getBranch(),
                getStatus(),
                getNote());
        stockInMaster.setStockInDetails(StockInDetailViewModel.stockInDetailsTempList);
        StockInMasterDao.updateStockInMaster(stockInMaster, stockInMasterID);
        resetProperties();
        StockInDetailViewModel.stockInDetailsTempList.clear();
        getStockInMasters();
    }
}
