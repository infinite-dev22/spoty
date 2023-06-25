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

import static org.infinite.spoty.values.SharedResources.*;

import java.util.LinkedList;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.TransferDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.TransferDetail;
import org.infinite.spoty.database.models.TransferMaster;

public class TransferDetailViewModel {
  public static final ObservableList<TransferDetail> transferDetailsList =
      FXCollections.observableArrayList();
  public static final ObservableList<TransferDetail> transferDetailsTempList =
      FXCollections.observableArrayList();
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<TransferMaster> transfer = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty("");
  private static final StringProperty serial = new SimpleStringProperty("");
  private static final StringProperty description = new SimpleStringProperty("");
  private static final StringProperty price = new SimpleStringProperty("");
  private static final StringProperty total = new SimpleStringProperty("");

  public static int getId() {
    return id.get();
  }

  public static void setId(int id) {
    TransferDetailViewModel.id.set(id);
  }

  public static IntegerProperty idProperty() {
    return id;
  }

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    TransferDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
    return product;
  }

  public static TransferMaster getTransfer() {
    return transfer.get();
  }

  public static void setTransfer(TransferMaster transfer) {
    TransferDetailViewModel.transfer.set(transfer);
  }

  public static ObjectProperty<TransferMaster> transferProperty() {
    return transfer;
  }

  public static int getQuantity() {
    return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
  }

  public static void setQuantity(String quantity) {
    TransferDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static String getDescription() {
    return description.get();
  }

  public static void setDescription(String description) {
    TransferDetailViewModel.description.set(description);
  }

  public static StringProperty descriptionProperty() {
    return description;
  }

  public static String getSerial() {
    return serial.get();
  }

  public static void setSerial(String serial) {
    TransferDetailViewModel.serial.set(serial);
  }

  public static StringProperty serialProperty() {
    return serial;
  }

  public static double getPrice() {
    return Double.parseDouble(!price.get().isEmpty() ? price.get() : "0");
  }

  public static void setPrice(String price) {
    TransferDetailViewModel.price.set(price);
  }

  public static StringProperty priceProperty() {
    return price;
  }

  public static double getTotal() {
    return Double.parseDouble(!total.get().isEmpty() ? total.get() : "0");
  }

  public static void setTotal(String total) {
    TransferDetailViewModel.total.set(total);
  }

  public static StringProperty totalProperty() {
    return total;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setQuantity("");
    setSerial("");
    setDescription("");
    setPrice("");
    setTotal("");
  }

  public static void addTransferDetails() {
    TransferDetail transferDetail =
        new TransferDetail(
            getProduct(), getQuantity(), getSerial(), getDescription(), getPrice(), getTotal());
    transferDetailsTempList.add(transferDetail);
    resetProperties();
  }

  public static ObservableList<TransferDetail> getTransferDetails() {
    transferDetailsList.clear();
    transferDetailsList.addAll(TransferDetailDao.fetchTransferDetails());
    return transferDetailsList;
  }

  public static void updateTransferDetail(int index) {
    TransferDetail transferDetail = TransferDetailDao.findTransferDetail(index);
    transferDetail.setProduct(getProduct());
    transferDetail.setQuantity(getQuantity());
    transferDetail.setSerialNo(getSerial());
    transferDetail.setDescription(getDescription());
    transferDetail.setPrice(getPrice());
    transferDetail.setTotal(getTotal());
    transferDetailsTempList.remove((int) getTempId());
    transferDetailsTempList.add(getTempId(), transferDetail);
    resetProperties();
  }

  public static void getItem(int index, int tempIndex) {
    TransferDetail transferDetail = TransferDetailDao.findTransferDetail(index);
    setTempId(tempIndex);
    setId(transferDetail.getId());
    setProduct(transferDetail.getProduct());
    setQuantity(String.valueOf(transferDetail.getQuantity()));
    setSerial(transferDetail.getSerialNo());
    setDescription(transferDetail.getDescription());
    setPrice(String.valueOf(transferDetail.getPrice()));
    setTotal(String.valueOf(transferDetail.getTotal()));
  }

  public static void updateItem(int index) {
    TransferDetail transferDetail = TransferDetailDao.findTransferDetail(index);
    transferDetail.setProduct(getProduct());
    transferDetail.setQuantity(getQuantity());
    transferDetail.setSerialNo(getSerial());
    transferDetail.setDescription(getDescription());
    transferDetail.setPrice(getPrice());
    transferDetail.setTotal(getTotal());
    TransferDetailDao.updateTransferDetail(transferDetail, index);
    getTransferDetails();
  }

  public static void removeTransferDetail(int index, int tempIndex) {
    transferDetailsTempList.remove(tempIndex);
    PENDING_DELETES.add(index);
  }

  public static void deleteTransferDetails(LinkedList<Integer> indexes) {
    indexes.forEach(TransferDetailDao::deleteTransferDetail);
  }
}
