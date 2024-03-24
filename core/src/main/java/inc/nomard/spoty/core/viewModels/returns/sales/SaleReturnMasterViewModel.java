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

package inc.nomard.spoty.core.viewModels.returns.sales;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.SaleReturnMaster;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleReturnMasterViewModel {
        private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    @Getter
    public static final ObservableList<SaleReturnMaster> saleReturnMasterList =
            FXCollections.observableArrayList();
    private static final ListProperty<SaleReturnMaster> saleReturns = new SimpleListProperty<>(saleReturnMasterList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
//    private static SaleReturnsRepositoryImpl saleReturnsRepository = new SaleReturnsRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        SaleReturnMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
        }
        return null;
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

    public static ObservableList<SaleReturnMaster> getSaleReturns() {
        return saleReturns.get();
    }

    public static void setSaleReturns(ObservableList<SaleReturnMaster> saleReturns) {
        SaleReturnMasterViewModel.saleReturns.set(saleReturns);
    }

    public static ListProperty<SaleReturnMaster> saleReturnsProperty() {
        return saleReturns;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setBranch(null);
        setNote("");
        setStatus("");
        setTotalCost("");
    }

    public static void saveSaleReturnMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
//        var saleReturnsMaster = SaleReturnMaster.builder()
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .SaleReturnsStatus(getSaleReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .date(getDate())
//                .build();
//
//        if (!SaleReturnDetailViewModel.SaleReturnsDetailsList.isEmpty()) {
//            SaleReturnDetailViewModel.SaleReturnsDetailsList.forEach(
//                    SaleReturnsDetail -> SaleReturnsDetail.setSaleReturn(SaleReturnsMaster));
//
//            SaleReturnsMaster.setSaleReturnDetails(SaleReturnDetailViewModel.SaleReturnsDetailsList);
//        }
//
//        var task = SaleReturnsRepository.postMaster(SaleReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> {
//            SaleReturnDetailViewModel.saveSaleReturnDetails(onActivity, null, onFailed);
//            onSuccess.run();
//        });
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void getSaleReturnMasters(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
//        Dao<SaleReturnMaster, Long> saleReturnMasterDao =
//                DaoManager.createDao(connectionSource, SaleReturnMaster.class);
//
//        Platform.runLater(
//                () -> {
//                    saleReturnMasterList.clear();
//
//                    try {
//                        saleReturnMasterList.addAll(saleReturnMasterDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
//                    }
//                });
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
//        var findModel = FindModel.builder().id(index).build();
//        var task = SaleReturnsRepository.fetchMaster(findModel);
//
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            SaleReturnMaster SaleReturnsMaster = new SaleReturnMaster();
//            try {
//                SaleReturnsMaster = gson.fromJson(task.get().body(), SaleReturnMaster.class);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
//            }
//
//            setId(SaleReturnsMaster.getId());
//            setDate(SaleReturnsMaster.getLocaleDate());
//            setCustomer(SaleReturnsMaster.getCustomer());
//            setNote(SaleReturnsMaster.getNotes());
//            setSaleReturnStatus(SaleReturnsMaster.getSaleReturnStatus());
//            setPayStatus(SaleReturnsMaster.getPaymentStatus());
//            SaleReturnDetailViewModel.SaleReturnsDetailsList.clear();
//            SaleReturnDetailViewModel.SaleReturnsDetailsList.addAll(SaleReturnsMaster.getSaleReturnDetails());
//
//            if (Objects.nonNull(onSuccess)) {
//                onSuccess.run();
//            }
//        });
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
//        var searchModel = SearchModel.builder().search(search).build();
//        var task = SaleReturnsRepository.searchMaster(searchModel);
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            Type listType = new TypeToken<ArrayList<SaleReturnMaster>>() {
//            }.getType();
//            ArrayList<SaleReturnMaster> SaleReturnsMasterList = new ArrayList<>();
//            try {
//                SaleReturnsMasterList = gson.fromJson(
//                        task.get().body(), listType);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
//            }
//
//            SaleReturnsMastersList.clear();
//            SaleReturnsMastersList.addAll(SaleReturnsMasterList);
//
//            if (Objects.nonNull(onSuccess)) {
//                onSuccess.run();
//            }
//        });
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
//        var saleReturnsMaster = SaleReturnMaster.builder()
//                .id(getId())
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .SaleReturnsStatus(getSaleReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .date(getDate())
//                .build();
//
//        if (!PENDING_DELETES.isEmpty()) {
//            SaleReturnDetailViewModel.deleteSaleReturnDetails(PENDING_DELETES, onActivity, null, onFailed);
//        }
//
//        if (!SaleReturnDetailViewModel.getSaleReturnDetailsList().isEmpty()) {
//            SaleReturnDetailViewModel.getSaleReturnDetailsList()
//                    .forEach(SaleReturnsDetail -> SaleReturnsDetail.setSaleReturn(SaleReturnsMaster));
//
//            SaleReturnsMaster.setSaleReturnDetails(SaleReturnDetailViewModel.getSaleReturnDetailsList());
//        }
//
//        var task = SaleReturnsRepository.putMaster(SaleReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> SaleReturnDetailViewModel.updateSaleReturnDetails(onActivity, onSuccess, onFailed));
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

//        var task = SaleReturnsRepository.deleteMaster(findModel);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }
}
