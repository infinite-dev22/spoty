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

package org.infinite.spoty.viewModels.transfers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.dtos.transfers.TransferMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.TransfersRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

public class TransferMasterViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    @Getter
    public static final ObservableList<TransferMaster> transferMastersList =
            FXCollections.observableArrayList();
    private static final ListProperty<TransferMaster> transfers =
            new SimpleListProperty<>(transferMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> fromBranch = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> toBranch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final TransfersRepositoryImpl transfersRepository = new TransfersRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        TransferMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, TransferMasterViewModel.class);
        }
        return null;
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

    public static ObservableList<TransferMaster> getTransfers() {
        return transfers.get();
    }

    public static void setTransfers(ObservableList<TransferMaster> transfers) {
        TransferMasterViewModel.transfers.set(transfers);
    }

    public static ListProperty<TransferMaster> transfersProperty() {
        return transfers;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setDate("");
                    setFromBranch(null);
                    setToBranch(null);
                    setNote("");
                    setStatus("");
                    setTotalCost("");
                    PENDING_DELETES.clear();
                });
    }

    public static void saveTransferMaster() throws IOException, InterruptedException {
        var transferMaster = TransferMaster.builder()
                .date(getDate())
                .fromBranch(getFromBranch())
                .toBranch(getToBranch())
                .total(getTotalCost())
                .status(getStatus())
                .notes(getNote())
                .build();
        if (!TransferDetailViewModel.transferDetailsList.isEmpty()) {
            TransferDetailViewModel.transferDetailsList.forEach(
                    transferDetail -> transferDetail.setTransfer(transferMaster));
            transferMaster.setTransferDetails(TransferDetailViewModel.getTransferDetails());
        }
        transfersRepository.postMaster(transferMaster);
        TransferDetailViewModel.saveTransferDetails();
        TransferMasterViewModel.resetProperties();
        getTransferMasters();
    }

    public static void getTransferMasters() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<TransferMaster>>() {
        }.getType();
        transferMastersList.clear();
        ArrayList<TransferMaster> transferMasterList = gson.fromJson(
                transfersRepository.fetchAllMaster().body(), listType);
        transferMastersList.addAll(transferMasterList);
    }

    public static void getItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = transfersRepository.fetchMaster(findModel).body();
        var transferMaster = gson.fromJson(response, TransferMaster.class);

        setId(transferMaster.getId());
        setDate(transferMaster.getLocaleDate());
        setFromBranch(transferMaster.getFromBranch());
        setToBranch(transferMaster.getToBranch());
        setTotalCost(String.valueOf(transferMaster.getTotal()));
        setNote(transferMaster.getNotes());
        setStatus(transferMaster.getStatus());

        TransferDetailViewModel.transferDetailsList.clear();
        TransferDetailViewModel.transferDetailsList.addAll(transferMaster.getTransferDetails());

        getTransferMasters();
    }

    public static void searchItem(String search) throws IOException, InterruptedException {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<TransferMaster>>() {
        }.getType();

        transferMastersList.clear();
        ArrayList<TransferMaster> transferMasterList = gson.fromJson(
                transfersRepository.searchMaster(searchModel).body(), listType);
        transferMastersList.addAll(transferMasterList);
    }

    public static void updateItem() throws IOException, InterruptedException {
        var transferMaster = TransferMaster.builder()
                .id(getId())
                .date(getDate())
                .fromBranch(getFromBranch())
                .toBranch(getToBranch())
                .total(getTotalCost())
                .status(getStatus())
                .notes(getNote())
                .build();

        TransferDetailViewModel.deleteTransferDetails(PENDING_DELETES);
        transferMaster.setTransferDetails(TransferDetailViewModel.getTransferDetails());

        transfersRepository.putMaster(transferMaster);
        TransferDetailViewModel.updateTransferDetails();

        TransferMasterViewModel.resetProperties();

        getTransferMasters();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        transfersRepository.deleteMaster(findModel);
        getTransferMasters();
    }
}
