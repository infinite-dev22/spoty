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

package inc.nomard.spoty.core.viewModels.returns.purchases;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import java.text.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;

public class PurchaseReturnMasterViewModel {
    public static final ObservableList<PurchaseReturnMaster> purchaseReturnMasterList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<PurchaseReturnMaster> purchaseReturns =
            new SimpleListProperty<>(purchaseReturnMasterList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
//    private static PurchaseReturnsRepositoryImpl purchaseReturnsRepository = new PurchaseReturnsRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        PurchaseReturnMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, PurchaseReturnMasterViewModel.class);
        }
        return null;
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

    public static ObservableList<PurchaseReturnMaster> getPurchaseReturns() {
        return purchaseReturns.get();
    }

    public static void setPurchaseReturns(ObservableList<PurchaseReturnMaster> purchaseReturns) {
        PurchaseReturnMasterViewModel.purchaseReturns.set(purchaseReturns);
    }

    public static ListProperty<PurchaseReturnMaster> purchaseReturnsProperty() {
        return purchaseReturns;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setBranch(null);
        setNote("");
        setStatus("");
        setTotalCost("");
    }

    public static void savePurchaseReturnMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
//        var PurchaseReturnsMaster = PurchaseReturnMaster.builder()
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .PurchaseReturnsStatus(getPurchaseReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .date(getDate())
//                .build();
//
//        if (!PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.isEmpty()) {
//            PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.forEach(
//                    PurchaseReturnsDetail -> PurchaseReturnsDetail.setPurchaseReturn(PurchaseReturnsMaster));
//
//            PurchaseReturnsMaster.setPurchaseReturnDetails(PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList);
//        }
//
//        var task = PurchaseReturnsRepository.postMaster(PurchaseReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> {
//            PurchaseReturnDetailViewModel.savePurchaseReturnDetails(onActivity, null, onFailed);
//            onSuccess.run();
//        });
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void getPurchaseReturnMasters(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
//        Dao<PurchaseReturnMaster, Long> purchaseReturnMasterDao =
//                DaoManager.createDao(connectionSource, PurchaseReturnMaster.class);
//
//        Platform.runLater(
//                () -> {
//                    purchaseReturnMasterList.clear();
//
//                    try {
//                        purchaseReturnMasterList.addAll(purchaseReturnMasterDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, PurchaseReturnMasterViewModel.class);
//                    }
//                });
    }

    public static ObservableList<PurchaseReturnMaster> getPurchaseReturnMasterList() {
        return purchaseReturnMasterList;
    }

    public static void getItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
//        var findModel = FindModel.builder().id(index).build();
//        var task = PurchaseReturnsRepository.fetchMaster(findModel);
//
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            PurchaseReturnMaster PurchaseReturnsMaster = new PurchaseReturnMaster();
//            try {
//                PurchaseReturnsMaster = gson.fromJson(task.get().body(), PurchaseReturnMaster.class);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, PurchaseReturnMasterViewModel.class);
//            }
//
//            setId(PurchaseReturnsMaster.getId());
//            setDate(PurchaseReturnsMaster.getLocaleDate());
//            setCustomer(PurchaseReturnsMaster.getCustomer());
//            setNote(PurchaseReturnsMaster.getNotes());
//            setPurchaseReturnStatus(PurchaseReturnsMaster.getPurchaseReturnStatus());
//            setPayStatus(PurchaseReturnsMaster.getPaymentStatus());
//            PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.clear();
//            PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.addAll(PurchaseReturnsMaster.getPurchaseReturnDetails());
//
//            if (Objects.nonNull(onSuccess)) {
//                onSuccess.run();
//            }
//        });
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
//        var searchModel = SearchModel.builder().search(search).build();
//        var task = PurchaseReturnsRepository.searchMaster(searchModel);
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            Type listType = new TypeToken<ArrayList<PurchaseReturnMaster>>() {
//            }.getType();
//            ArrayList<PurchaseReturnMaster> PurchaseReturnsMasterList = new ArrayList<>();
//            try {
//                PurchaseReturnsMasterList = gson.fromJson(
//                        task.get().body(), listType);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, PurchaseReturnMasterViewModel.class);
//            }
//
//            PurchaseReturnsMastersList.clear();
//            PurchaseReturnsMastersList.addAll(PurchaseReturnsMasterList);
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
//        var PurchaseReturnsMaster = PurchaseReturnMaster.builder()
//                .id(getId())
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .PurchaseReturnsStatus(getPurchaseReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .date(getDate())
//                .build();
//
//        if (!PENDING_DELETES.isEmpty()) {
//            PurchaseReturnDetailViewModel.deletePurchaseReturnDetails(PENDING_DELETES, onActivity, null, onFailed);
//        }
//
//        if (!PurchaseReturnDetailViewModel.getPurchaseReturnDetailsList().isEmpty()) {
//            PurchaseReturnDetailViewModel.getPurchaseReturnDetailsList()
//                    .forEach(PurchaseReturnsDetail -> PurchaseReturnsDetail.setPurchaseReturn(PurchaseReturnsMaster));
//
//            PurchaseReturnsMaster.setPurchaseReturnDetails(PurchaseReturnDetailViewModel.getPurchaseReturnDetailsList());
//        }
//
//        var task = PurchaseReturnsRepository.putMaster(PurchaseReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> PurchaseReturnDetailViewModel.updatePurchaseReturnDetails(onActivity, onSuccess, onFailed));
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

//        var task = PurchaseReturnsRepository.deleteMaster(findModel);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }
}
