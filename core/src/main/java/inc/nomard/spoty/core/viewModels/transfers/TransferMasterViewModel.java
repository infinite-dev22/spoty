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

package inc.nomard.spoty.core.viewModels.transfers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferMaster;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.TransfersRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

public class TransferMasterViewModel {
    @Getter
    public static final ObservableList<TransferMaster> transferMastersList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
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

    public static void saveTransferMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
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

        var task = transfersRepository.postMaster(transferMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            TransferDetailViewModel.saveTransferDetails(onActivity, null, onFailed);
            onSuccess.run();
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getTransferMasters(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var task = transfersRepository.fetchAllMaster();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<TransferMaster>>() {
            }.getType();

            try {
                ArrayList<TransferMaster> transferMasterList = gson.fromJson(
                        task.get().body(), listType);

                transferMastersList.clear();
                transferMastersList.addAll(transferMasterList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();
        var task = transfersRepository.fetchMaster(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                TransferMaster transferMaster = gson.fromJson(task.get().body(), TransferMaster.class);

                setId(transferMaster.getId());
                setDate(transferMaster.getLocaleDate());
                setFromBranch(transferMaster.getFromBranch());
                setToBranch(transferMaster.getToBranch());
                setTotalCost(String.valueOf(transferMaster.getTotal()));
                setNote(transferMaster.getNotes());
                setStatus(transferMaster.getStatus());

                TransferDetailViewModel.transferDetailsList.clear();
                TransferDetailViewModel.transferDetailsList.addAll(transferMaster.getTransferDetails());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, TransferMasterViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = transfersRepository.searchMaster(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<TransferMaster>>() {
            }.getType();

            try {
                ArrayList<TransferMaster> transferMasterList = gson.fromJson(
                        task.get().body(), listType);

                transferMastersList.clear();
                transferMastersList.addAll(transferMasterList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, TransferMasterViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var transferMaster = TransferMaster.builder()
                .id(getId())
                .date(getDate())
                .fromBranch(getFromBranch())
                .toBranch(getToBranch())
                .total(getTotalCost())
                .status(getStatus())
                .notes(getNote())
                .build();

        if (!PENDING_DELETES.isEmpty()) {
            TransferDetailViewModel.deleteTransferDetails(PENDING_DELETES, onActivity, null, onFailed);
        }

        if (!TransferDetailViewModel.getTransferDetails().isEmpty()) {
            TransferDetailViewModel.getTransferDetails()
                    .forEach(adjustmentDetail -> adjustmentDetail.setTransfer(transferMaster));

            transferMaster.setTransferDetails(TransferDetailViewModel.getTransferDetails());
        }

        var task = transfersRepository.putMaster(transferMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> TransferDetailViewModel.updateTransferDetails(onActivity, onSuccess, onFailed));
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = transfersRepository.deleteMaster(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
