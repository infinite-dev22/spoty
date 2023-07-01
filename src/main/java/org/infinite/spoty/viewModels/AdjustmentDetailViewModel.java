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
import org.infinite.spoty.database.dao.AdjustmentDetailDao;
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.models.ProductDetail;

public class AdjustmentDetailViewModel {
  public static final ObservableList<AdjustmentDetail> adjustmentDetailsList =
      FXCollections.observableArrayList();
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<AdjustmentMaster> adjustment = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty("");
  private static final StringProperty adjustmentType = new SimpleStringProperty("");

  public static int getId() {
    return id.get();
  }

  public static void setId(int id) {
    AdjustmentDetailViewModel.id.set(id);
  }

  public static IntegerProperty idProperty() {
    return id;
  }

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    AdjustmentDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
    return product;
  }

  public static AdjustmentMaster getAdjustment() {
    return adjustment.get();
  }

  public static void setAdjustment(AdjustmentMaster adjustment) {
    AdjustmentDetailViewModel.adjustment.set(adjustment);
  }

  public static ObjectProperty<AdjustmentMaster> adjustmentProperty() {
    return adjustment;
  }

  public static int getQuantity() {
    return Integer.parseInt(quantity.get());
  }

  public static void setQuantity(String quantity) {
    AdjustmentDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static String getAdjustmentType() {
    return adjustmentType.get();
  }

  public static void setAdjustmentType(String adjustmentType) {
    AdjustmentDetailViewModel.adjustmentType.set(adjustmentType);
  }

  public static StringProperty adjustmentTypeProperty() {
    return adjustmentType;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setAdjustment(null);
    setAdjustmentType("");
    setQuantity("");
  }

  public static void addAdjustmentDetails() {
    AdjustmentDetail adjustmentDetail =
        new AdjustmentDetail(getProduct(), getQuantity(), getAdjustmentType());
    adjustmentDetailsList.add(adjustmentDetail);
    resetProperties();
  }

  public static void updateAdjustmentDetail(int index) {
    AdjustmentDetail adjustmentDetail = adjustmentDetailsList.get(getTempId());
//    AdjustmentDetail adjustmentDetail = AdjustmentDetailDao.findAdjustmentDetail(index);
    adjustmentDetail.setProduct(getProduct());
    adjustmentDetail.setQuantity(getQuantity());
    adjustmentDetail.setAdjustmentType(getAdjustmentType());
//    adjustmentDetailsList.remove((int) getTempId());
//    adjustmentDetailsList.add(getTempId(), adjustmentDetail);
    resetProperties();
  }

  public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
    adjustmentDetailsList.clear();
    adjustmentDetailsList.addAll(AdjustmentDetailDao.getAdjustmentDetail());
    return adjustmentDetailsList;
  }

  public static void getItem(int index, int tempIndex) {
    AdjustmentDetail adjustmentDetail = AdjustmentDetailDao.findAdjustmentDetail(index);
    setTempId(tempIndex);
    setProduct(adjustmentDetail.getProduct());
    setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
    setAdjustmentType(adjustmentDetail.getAdjustmentType());
  }

  public static void updateItem(int adjustmentDetailID) {
    AdjustmentDetail adjustmentDetail =
        new AdjustmentDetail(getProduct(), getQuantity(), getAdjustmentType());
    AdjustmentDetailDao.updateAdjustmentDetail(adjustmentDetail, adjustmentDetailID);
    getAdjustmentDetails();
  }

  public static void removeAdjustmentDetail(int index, int tempIndex) {
    adjustmentDetailsList.remove(tempIndex);
    PENDING_DELETES.add(index);
  }

  public static void deleteAdjustmentDetails(LinkedList<Integer> indexes) {
    indexes.forEach(AdjustmentDetailDao::deleteAdjustmentDetail);
  }
}
