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

package org.infinite.spoty.viewModels.quotations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.dtos.Customer;
import org.infinite.spoty.data_source.dtos.quotations.QuotationMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.QuotationsRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

public class QuotationMasterViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    @Getter
    public static final ObservableList<QuotationMaster> quotationMastersList =
            FXCollections.observableArrayList();
    private static final ListProperty<QuotationMaster> quotations =
            new SimpleListProperty<>(quotationMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
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

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, QuotationMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        QuotationMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
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
                    setDate("");
                    setCustomer(null);
                    setBranch(null);
                    setStatus("");
                    setNote("");
                    PENDING_DELETES.clear();
                });
    }

    public static void saveQuotationMaster() throws IOException, InterruptedException {
        var quotationMaster = QuotationMaster.builder()
                .date(getDate())
                .customer(getCustomer())
                .branch(getBranch())
                .status(getStatus())
                .notes(getNote())
                .build();

        if (!QuotationDetailViewModel.quotationDetailsList.isEmpty()) {
            QuotationDetailViewModel.quotationDetailsList.forEach(
                    quotationDetail -> quotationDetail.setQuotation(quotationMaster));
            quotationMaster.setQuotationDetails(
                    QuotationDetailViewModel.getQuotationDetailsList());
        }

        quotationRepository.postMaster(quotationMaster);
        QuotationDetailViewModel.saveQuotationDetails();

        resetProperties();
        getQuotationMasters();
    }

    public static void getQuotationMasters() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
        }.getType();
        quotationMastersList.clear();
        ArrayList<QuotationMaster> quotationMasterList = gson.fromJson(
                quotationRepository.fetchAllMaster().body(), listType);
        quotationMastersList.addAll(quotationMasterList);
    }

    public static void getQuotationMaster(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = quotationRepository.fetchMaster(findModel).body();
        var quotationMaster = gson.fromJson(response, QuotationMaster.class);

        setId(quotationMaster.getId());
        setBranch(quotationMaster.getBranch());
        setNote(quotationMaster.getNotes());
        setDate(quotationMaster.getLocaleDate());
        QuotationDetailViewModel.quotationDetailsList.clear();
        QuotationDetailViewModel.quotationDetailsList.addAll(quotationMaster.getQuotationDetails());
        getQuotationMasters();
    }

    public static void searchItem(String search) throws IOException, InterruptedException {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
        }.getType();
        quotationMastersList.clear();

        ArrayList<QuotationMaster> quotationMasterList = gson.fromJson(
                quotationRepository.searchMaster(searchModel).body(), listType);
        quotationMastersList.addAll(quotationMasterList);
    }

    public static void updateItem() throws IOException, InterruptedException {
        var quotationMaster = QuotationMaster.builder()
                .id(getId())
                .date(getDate())
                .customer(getCustomer())
                .branch(getBranch())
                .status(getStatus())
                .notes(getNote())
                .build();

        QuotationDetailViewModel.deleteQuotationDetails(PENDING_DELETES);

        if (!QuotationDetailViewModel.getQuotationDetailsList().isEmpty()) {
            QuotationDetailViewModel.getQuotationDetailsList()
                    .forEach(quotationDetail -> quotationDetail.setQuotation(quotationMaster));

            quotationMaster.setQuotationDetails(QuotationDetailViewModel.getQuotationDetailsList());
        }
        quotationRepository.putMaster(quotationMaster);
        QuotationDetailViewModel.updateQuotationDetails();
        resetProperties();
        getQuotationMasters();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        quotationRepository.deleteMaster(findModel);
        getQuotationMasters();
    }

}
