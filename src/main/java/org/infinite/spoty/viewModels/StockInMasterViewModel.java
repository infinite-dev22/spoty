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

package org.infinite.spoty.viewModels;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.StockInMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.StockInMaster;

public class StockInMasterViewModel {
  public static final ObservableList<StockInMaster> stockInMasterList =
      FXCollections.observableArrayList();
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
    PENDING_DELETES.clear();
    StockInDetailViewModel.stockInDetailsList.clear();
  }

  public static void saveStockInMaster() {
    StockInMaster stockInMaster = new StockInMaster(getDate(), getBranch(), getStatus(), getNote());
    if (!StockInDetailViewModel.stockInDetailsList.isEmpty()) {
      StockInDetailViewModel.stockInDetailsList.forEach(
          stockInDetail -> stockInDetail.setStockIn(stockInMaster));
      stockInMaster.setStockInDetails(StockInDetailViewModel.stockInDetailsList);
    }
    StockInMasterDao.saveStockInMaster(stockInMaster);
    resetProperties();
    getStockInMasters();
  }

  public static ObservableList<StockInMaster> getStockInMasters() {
    stockInMasterList.clear();
    stockInMasterList.addAll(StockInMasterDao.fetchStockInMasters());
    return stockInMasterList;
  }

  public static void getItem(int index) {
    StockInMaster stockInMaster = StockInMasterDao.findStockInMaster(index);
    setId(stockInMaster.getId());
    setDate(stockInMaster.getLocaleDate());
    setBranch(stockInMaster.getBranch());
    setTotalCost(String.valueOf(stockInMaster.getTotalCost()));
    setStatus(stockInMaster.getStatus());
    setNote(stockInMaster.getNotes());
    StockInDetailViewModel.stockInDetailsList.clear();
    StockInDetailViewModel.stockInDetailsList.addAll(stockInMaster.getStockInDetails());
    getStockInMasters();
  }

  public static void updateItem(int index) {
    StockInMaster stockInMaster = StockInMasterDao.findStockInMaster(index);
    stockInMaster.setDate(getDate());
    stockInMaster.setBranch(getBranch());
    stockInMaster.setStatus(getStatus());
    stockInMaster.setNotes(getNote());
    StockInDetailViewModel.deleteStockInDetails(PENDING_DELETES);
    stockInMaster.setStockInDetails(StockInDetailViewModel.stockInDetailsList);
    StockInMasterDao.updateStockInMaster(stockInMaster, index);
    resetProperties();
    getStockInMasters();
  }
}
