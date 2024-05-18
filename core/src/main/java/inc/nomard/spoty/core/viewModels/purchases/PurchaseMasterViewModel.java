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

package inc.nomard.spoty.core.viewModels.purchases;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;

public class PurchaseMasterViewModel {
    @Getter
    public static final ObservableList<PurchaseMaster> purchaseMastersList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<PurchaseMaster> purchases =
            new SimpleListProperty<>(purchaseMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final PurchasesRepositoryImpl purchasesRepository = new PurchasesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        PurchaseMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Supplier getSupplier() {
        return supplier.get();
    }

    public static void setSupplier(Supplier supplier) {
        PurchaseMasterViewModel.supplier.set(supplier);
    }

    public static ObjectProperty<Supplier> supplierProperty() {
        return supplier;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        PurchaseMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        PurchaseMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static String getNotes() {
        return note.get();
    }

    public static void setNote(String note) {
        PurchaseMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<PurchaseMaster> getPurchases() {
        return purchases.get();
    }

    public static void setPurchases(ObservableList<PurchaseMaster> purchases) {
        PurchaseMasterViewModel.purchases.set(purchases);
    }

    public static ListProperty<PurchaseMaster> purchasesProperty() {
        return purchases;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setDate("");
                    setSupplier(null);
                    setBranch(null);
                    setStatus("");
                    setNote("");
                    PENDING_DELETES.clear();
                    PurchaseDetailViewModel.getPurchaseDetails().clear();
                });
    }

    public static void savePurchaseMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var purchaseMaster = PurchaseMaster.builder()
                .date(getDate())
                .supplier(getSupplier())
                .taxRate(0)
                .netTax(0)
                .discount(0)
                .amountPaid(0)
                .total(0)
                .amountDue(0)
                .paymentStatus("")
                .purchaseStatus(getStatus())
                .notes(getNotes())
                .build();

        if (!PurchaseDetailViewModel.purchaseDetailsList.isEmpty()) {
            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.purchaseDetailsList);
        }

        var task = purchasesRepository.postMaster(purchaseMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllPurchaseMasters(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
        }.getType();
        var task = purchasesRepository.fetchAllMaster();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            purchaseMastersList.clear();
            ArrayList<PurchaseMaster> purchaseMasterList = new ArrayList<>();
            try {
                purchaseMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
            }
            purchaseMastersList.addAll(purchaseMasterList);


            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getPurchaseMaster(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = purchasesRepository.fetchMaster(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> {
                PurchaseMaster purchaseMaster = new PurchaseMaster();
                try {
                    purchaseMaster = gson.fromJson(task.get().body(), PurchaseMaster.class);
                } catch (InterruptedException | ExecutionException e) {
                    SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
                }

                setId(purchaseMaster.getId());
                setNote(purchaseMaster.getNotes());
                setDate(purchaseMaster.getLocaleDate());
                PurchaseDetailViewModel.purchaseDetailsList.clear();
                PurchaseDetailViewModel.purchaseDetailsList.addAll(purchaseMaster.getPurchaseDetails());

                onSuccess.run();
            });
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        var task = purchasesRepository.searchMaster(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
            }.getType();
            ArrayList<PurchaseMaster> purchaseMasterList = new ArrayList<>();
            try {
                purchaseMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
            }

            purchaseMastersList.clear();
            purchaseMastersList.addAll(purchaseMasterList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var purchaseMaster = PurchaseMaster.builder()
                .id(getId())
                .date(getDate())
                .supplier(getSupplier())
                .taxRate(0)
                .netTax(0)
                .discount(0)
                .amountPaid(0)
                .total(0)
                .amountDue(0)
                .paymentStatus("")
                .purchaseStatus(getStatus())
                .notes(getNotes())
                .build();

        if (!PENDING_DELETES.isEmpty()) {
            PurchaseDetailViewModel.deletePurchaseDetails(PENDING_DELETES, onActivity, null, onFailed);
        }

        if (!PurchaseDetailViewModel.getPurchaseDetailsList().isEmpty()) {
            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.getPurchaseDetails());
        }

        var task = purchasesRepository.putMaster(purchaseMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> PurchaseDetailViewModel.updatePurchaseDetails(onActivity, onSuccess, onFailed));
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
        // getPurchaseMasters();
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = purchasesRepository.deleteMaster(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }
}
