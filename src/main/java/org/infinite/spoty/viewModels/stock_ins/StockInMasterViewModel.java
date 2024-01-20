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

package org.infinite.spoty.viewModels.stock_ins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.dtos.stock_ins.StockInMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.StockInsRepositoryImpl;
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

public class StockInMasterViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    @Getter
    public static final ObservableList<StockInMaster> stockInMastersList =
            FXCollections.observableArrayList();
    private static final ListProperty<StockInMaster> stockIns =
            new SimpleListProperty<>(stockInMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final StockInsRepositoryImpl stockInsRepository = new StockInsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        StockInMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, StockInMasterViewModel.class);
        }
        return null;
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

    public static ObservableList<StockInMaster> getStockIns() {
        return stockIns.get();
    }

    public static void setStockIns(ObservableList<StockInMaster> stockIns) {
        StockInMasterViewModel.stockIns.set(stockIns);
    }

    public static ListProperty<StockInMaster> stockInsProperty() {
        return stockIns;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setDate("");
                    setBranch(null);
                    setNote("");
                    setStatus("");
                    setTotalCost("");
                    PENDING_DELETES.clear();
                });
    }

    public static void saveStockInMaster() throws IOException, InterruptedException {
        var stockInMaster = StockInMaster.builder()
                .date(getDate())
                .branch(getBranch())
                .status(getStatus())
                .notes(getNote())
                .build();

        if (!StockInDetailViewModel.stockInDetailsList.isEmpty()) {
            StockInDetailViewModel.stockInDetailsList.forEach(
                    stockInDetail -> stockInDetail.setStockIn(stockInMaster));
            stockInMaster.setStockInDetails(StockInDetailViewModel.stockInDetailsList);
        }

        stockInsRepository.postMaster(stockInMaster);
        StockInDetailViewModel.createStockInDetails();
        resetProperties();
        getStockInMasters();
    }

    public static void getStockInMasters() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<StockInMaster>>() {
        }.getType();
        stockInMastersList.clear();
        ArrayList<StockInMaster> stockInMasterList = gson.fromJson(
                stockInsRepository.fetchAllMaster().body(), listType);
        stockInMastersList.addAll(stockInMasterList);
    }

    public static void getItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = stockInsRepository.fetchMaster(findModel).body();
        var stockInMaster = gson.fromJson(response, StockInMaster.class);

        setId(stockInMaster.getId());
        setDate(stockInMaster.getLocaleDate());
        setBranch(stockInMaster.getBranch());
        setStatus(stockInMaster.getStatus());
        setNote(stockInMaster.getNotes());

        StockInDetailViewModel.stockInDetailsList.clear();
        StockInDetailViewModel.stockInDetailsList.addAll(stockInMaster.getStockInDetails());

        getStockInMasters();
    }

    public static void searchItem(String search) throws IOException, InterruptedException {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<StockInMaster>>() {
        }.getType();

        stockInMastersList.clear();
        ArrayList<StockInMaster> stockInMasterList = gson.fromJson(
                stockInsRepository.searchMaster(searchModel).body(), listType);
        stockInMastersList.addAll(stockInMasterList);
    }

    public static void updateItem() throws IOException, InterruptedException {
        var stockInMaster = StockInMaster.builder()
                .id(getId())
                .date(getDate())
                .branch(getBranch())
                .status(getStatus())
                .notes(getNote())
                .build();

        StockInDetailViewModel.deleteStockInDetails(PENDING_DELETES);
        stockInMaster.setStockInDetails(StockInDetailViewModel.stockInDetailsList);
        stockInsRepository.putDetail(stockInMaster);
        StockInDetailViewModel.updateStockInDetails();
        resetProperties();
        getStockInMasters();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        stockInsRepository.deleteMaster(findModel);
        getStockInMasters();
    }
}
