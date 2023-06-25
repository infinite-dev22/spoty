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
import org.infinite.spoty.database.dao.PurchaseMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.PurchaseMaster;
import org.infinite.spoty.database.models.Supplier;

public class PurchaseMasterViewModel {
  public static final ObservableList<PurchaseMaster> purchaseMasterList =
      FXCollections.observableArrayList();
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final StringProperty date = new SimpleStringProperty("");
  private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
  private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
  private static final StringProperty status = new SimpleStringProperty("");
  private static final StringProperty note = new SimpleStringProperty("");

  public static int getId() {
    return id.get();
  }

  public static void setId(int id) {
    PurchaseMasterViewModel.id.set(id);
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

  public static String getNote() {
    return note.get();
  }

  public static void setNote(String note) {
    PurchaseMasterViewModel.note.set(note);
  }

  public static StringProperty noteProperty() {
    return note;
  }

  public static void resetProperties() {
    setId(0);
    setDate("");
    setSupplier(null);
    setBranch(null);
    setStatus("");
    setNote("");
    PENDING_DELETES.clear();
    PurchaseDetailViewModel.purchaseDetailTempList.clear();
  }

  public static void savePurchaseMaster() {
    PurchaseMaster purchaseMaster =
        new PurchaseMaster(getSupplier(), getBranch(), getStatus(), getNote(), getDate());
    if (!PurchaseDetailViewModel.purchaseDetailTempList.isEmpty()) {
      PurchaseDetailViewModel.purchaseDetailTempList.forEach(
          purchaseDetail -> purchaseDetail.setPurchase(purchaseMaster));
      purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.purchaseDetailTempList);
    }
    PurchaseMasterDao.savePurchaseMaster(purchaseMaster);
    resetProperties();
    getPurchaseMasters();
  }

  public static ObservableList<PurchaseMaster> getPurchaseMasters() {
    purchaseMasterList.clear();
    purchaseMasterList.addAll(PurchaseMasterDao.fetchPurchaseMasters());
    return purchaseMasterList;
  }

  public static void getItem(int index) {
    PurchaseMaster purchaseMaster = PurchaseMasterDao.findPurchaseMaster(index);
    setId(purchaseMaster.getId());
    setDate(purchaseMaster.getLocaleDate());
    setSupplier(purchaseMaster.getSupplier());
    setBranch(purchaseMaster.getBranch());
    setStatus(purchaseMaster.getStatus());
    setNote(purchaseMaster.getNotes());
    PurchaseDetailViewModel.purchaseDetailTempList.addAll(purchaseMaster.getPurchaseDetails());
    getPurchaseMasters();
  }

  public static void updateItem(int index) {
    PurchaseMaster purchaseMaster = PurchaseMasterDao.findPurchaseMaster(index);
    purchaseMaster.setSupplier(getSupplier());
    purchaseMaster.setBranch(getBranch());
    purchaseMaster.setStatus(getStatus());
    purchaseMaster.setNotes(getNote());
    purchaseMaster.setDate(getDate());
    PurchaseDetailViewModel.deletePurchaseDetails(PENDING_DELETES);
    purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.purchaseDetailTempList);
    PurchaseMasterDao.updatePurchaseMaster(purchaseMaster, index);
    resetProperties();
    getPurchaseMasters();
  }
}
