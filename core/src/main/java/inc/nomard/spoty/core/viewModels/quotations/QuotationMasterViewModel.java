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

package inc.nomard.spoty.core.viewModels.quotations;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;

import lombok.extern.slf4j.*;

@Slf4j
public class QuotationMasterViewModel {
    @Getter
    public static final ObservableList<QuotationMaster> quotationMastersList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<QuotationMaster> quotations =
            new SimpleListProperty<>(quotationMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final QuotationsRepositoryImpl quotationRepository = new QuotationsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        QuotationMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        QuotationMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        QuotationMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        QuotationMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        QuotationMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static ObservableList<QuotationMaster> getQuotations() {
        return quotations.get();
    }

    public static void setQuotations(ObservableList<QuotationMaster> quotations) {
        QuotationMasterViewModel.quotations.set(quotations);
    }

    public static ListProperty<QuotationMaster> quotationsProperty() {
        return quotations;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setCustomer(null);
                    setBranch(null);
                    setStatus("");
                    setNote("");
                    PENDING_DELETES.clear();
                });
    }

    public static void saveQuotationMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var quotationMaster = QuotationMaster.builder()
                .customer(getCustomer())
                .status(getStatus())
                .notes(getNote())
                .build();

        if (!QuotationDetailViewModel.quotationDetailsList.isEmpty()) {
            quotationMaster.setQuotationDetails(
                    QuotationDetailViewModel.getQuotationDetailsList());
        }

        System.out.println(new Gson().toJson(quotationMaster));

        var task = quotationRepository.postMaster(quotationMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            QuotationDetailViewModel.saveQuotationDetails(onActivity, null, onFailed);
            onSuccess.run();
        });
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllQuotationMasters(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = quotationRepository.fetchAllMaster();
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
            Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
            }.getType();
            ArrayList<QuotationMaster> quotationMasterList = new ArrayList<>();
            try {
                quotationMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, QuotationMasterViewModel.class);
            }

            quotationMastersList.clear();
            quotationMastersList.addAll(quotationMasterList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getQuotationMaster(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = quotationRepository.fetchMaster(findModel);
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
            try {
                var quotationMaster = gson.fromJson(task.get().body(), QuotationMaster.class);

                setId(quotationMaster.getId());
                setNote(quotationMaster.getNotes());
                QuotationDetailViewModel.quotationDetailsList.clear();
                QuotationDetailViewModel.quotationDetailsList.addAll(quotationMaster.getQuotationDetails());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, QuotationMasterViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
//        getQuotationMasters();
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        var task = quotationRepository.searchMaster(searchModel);
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
            Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
            }.getType();
            ArrayList<QuotationMaster> quotationMasterList = new ArrayList<>();
            try {
                quotationMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, QuotationMasterViewModel.class);
            }

            quotationMastersList.clear();
            quotationMastersList.addAll(quotationMasterList);

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
        var quotationMaster = QuotationMaster.builder()
                .id(getId())
                .customer(getCustomer())
                .status(getStatus())
                .notes(getNote())
                .build();

        if (!PENDING_DELETES.isEmpty()) {
            QuotationDetailViewModel.deleteQuotationDetails(PENDING_DELETES, onActivity, null, onFailed);
        }

        if (!QuotationDetailViewModel.getQuotationDetailsList().isEmpty()) {
            quotationMaster.setQuotationDetails(QuotationDetailViewModel.getQuotationDetailsList());
        }
        var task = quotationRepository.putMaster(quotationMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> QuotationDetailViewModel.updateQuotationDetails(onActivity, onSuccess, onFailed));
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
        // getQuotationMasters();
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = quotationRepository.deleteMaster(findModel);
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
