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
import org.infinite.spoty.database.dao.SaleMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.SaleMaster;

public class SaleMasterViewModel {
  public static final ObservableList<SaleMaster> saleMasterList =
      FXCollections.observableArrayList();
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final StringProperty date = new SimpleStringProperty("");
  private static final StringProperty ref = new SimpleStringProperty("");
  private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
  private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
  private static final StringProperty saleStatus = new SimpleStringProperty("");
  private static final StringProperty payStatus = new SimpleStringProperty("");
  private static final StringProperty note = new SimpleStringProperty("");

  public static int getId() {
    return id.get();
  }

  public static void setId(int id) {
    SaleMasterViewModel.id.set(id);
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
    SaleMasterViewModel.date.set(date);
  }

  public static StringProperty dateProperty() {
    return date;
  }

  public static Customer getCustomer() {
    return customer.get();
  }

  public static void setCustomer(Customer customer) {
    SaleMasterViewModel.customer.set(customer);
  }

  public static ObjectProperty<Customer> customerProperty() {
    return customer;
  }

  public static Branch getBranch() {
    return branch.get();
  }

  public static void setBranch(Branch branch) {
    SaleMasterViewModel.branch.set(branch);
  }

  public static ObjectProperty<Branch> branchProperty() {
    return branch;
  }

  public static String getSaleStatus() {
    return saleStatus.get();
  }

  public static void setSaleStatus(String saleStatus) {
    SaleMasterViewModel.saleStatus.set(saleStatus);
  }

  public static StringProperty saleStatusProperty() {
    return saleStatus;
  }

  public static String getPayStatus() {
    return payStatus.get();
  }

  public static void setPayStatus(String payStatus) {
    SaleMasterViewModel.payStatus.set(payStatus);
  }

  public static StringProperty payStatusProperty() {
    return payStatus;
  }

  public static String getNote() {
    return note.get();
  }

  public static void setNote(String note) {
    SaleMasterViewModel.note.set(note);
  }

  public static StringProperty noteProperty() {
    return note;
  }

  public static void resetProperties() {
    setId(0);
    setDate("");
    setCustomer(null);
    setBranch(null);
    setSaleStatus("");
    setNote("");
    PENDING_DELETES.clear();
    SaleDetailViewModel.saleDetailTempList.clear();
  }

  public static void saveSaleMaster() {
    SaleMaster saleMaster =
        new SaleMaster(
            getCustomer(), getBranch(), getSaleStatus(), getPayStatus(), getNote(), getDate());
    if (!SaleDetailViewModel.saleDetailTempList.isEmpty()) {
      SaleDetailViewModel.saleDetailTempList.forEach(
          saleDetail -> saleDetail.setSaleMaster(saleMaster));
      saleMaster.setSaleDetails(SaleDetailViewModel.saleDetailTempList);
    }
    SaleMasterDao.saveSaleMaster(saleMaster);
    resetProperties();
    getSaleMasters();
  }

  public static ObservableList<SaleMaster> getSaleMasters() {
    saleMasterList.clear();
    saleMasterList.addAll(SaleMasterDao.fetchSaleMasters());
    return saleMasterList;
  }

  public static void getItem(int index) {
    SaleMaster saleMaster = SaleMasterDao.findSaleMaster(index);
    setId(saleMaster.getId());
    setDate(saleMaster.getLocaleDate());
    setCustomer(saleMaster.getCustomer());
    setBranch(saleMaster.getBranch());
    setNote(saleMaster.getNotes());
    setSaleStatus(saleMaster.getSaleStatus());
    setPayStatus(saleMaster.getPaymentStatus());
    SaleDetailViewModel.saleDetailTempList.addAll(saleMaster.getSaleDetails());
    getSaleMasters();
  }

  public static void updateItem(int index) {
    SaleMaster saleMaster = SaleMasterDao.findSaleMaster(index);
    saleMaster.setCustomer(getCustomer());
    saleMaster.setBranch(getBranch());
    saleMaster.setSaleStatus(getSaleStatus());
    saleMaster.setPaymentStatus(getPayStatus());
    saleMaster.setNotes(getNote());
    saleMaster.setDate(getDate());
    SaleDetailViewModel.deleteSaleDetails(PENDING_DELETES);
    saleMaster.setSaleDetails(SaleDetailViewModel.saleDetailTempList);
    SaleMasterDao.updateSaleMaster(saleMaster, index);
    resetProperties();
    getSaleMasters();
  }
}
