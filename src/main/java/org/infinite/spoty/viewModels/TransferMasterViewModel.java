package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.TransferMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.TransferMaster;
import org.infinite.spoty.database.models.Supplier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferMasterViewModel {
    public static final ObservableList<TransferMaster> transferMasterList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> fromBranch = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> toBranch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        TransferMasterViewModel.id.set(id);
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
        TransferMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Branch getFromBranch() {
        return fromBranch.get();
    }

    public static void setFromBranch(Branch fromBranch) {
        TransferMasterViewModel.fromBranch.set(fromBranch);
    }

    public static ObjectProperty<Branch> fromBranchProperty() {
        return fromBranch;
    }

    public static Branch getToBranch() {
        return toBranch.get();
    }

    public static void setToBranch(Branch toBranch) {
        TransferMasterViewModel.toBranch.set(toBranch);
    }

    public static ObjectProperty<Branch> toBranchProperty() {
        return toBranch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        TransferMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        TransferMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        TransferMasterViewModel.totalCost.set(totalCost);
    }

    public static StringProperty totalCostProperty() {
        return totalCost;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setFromBranch(null);
        setToBranch(null);
        setNote("");
        setStatus("");
        setTotalCost("");
    }

    public static void saveTransferMaster() {
        TransferMaster transferMaster = new TransferMaster(getDate(),
                getFromBranch(),
                getToBranch(),
                getTotalCost(),
                getStatus(),
                getNote());
        transferMaster.setTransferDetails(TransferDetailViewModel.transferDetailsTempList);
        TransferMasterDao.saveTransferMaster(transferMaster);
        resetProperties();
        TransferDetailViewModel.transferDetailsTempList.clear();
        getTransferMasters();
    }

    public static ObservableList<TransferMaster> getTransferMasters() {
        transferMasterList.clear();
        transferMasterList.addAll(TransferMasterDao.fetchTransferMasters());
        return transferMasterList;
    }

}
