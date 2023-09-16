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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.models.AdjustmentTransaction;
import org.infinite.spoty.database.models.Product;
import org.jetbrains.annotations.NotNull;

public class AdjustmentDetailViewModel {
  public static final ObservableList<AdjustmentDetail> adjustmentDetailsList =
      FXCollections.observableArrayList();
  private static final ListProperty<AdjustmentDetail> adjustmentDetails =
      new SimpleListProperty<>(adjustmentDetailsList);
  private static final LongProperty id = new SimpleLongProperty();
  private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<AdjustmentMaster> adjustment = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty();
  private static final StringProperty adjustmentType = new SimpleStringProperty();
  private static final SQLiteConnection connection = SQLiteConnection.getInstance();
  private static final ConnectionSource connectionSource = connection.getConnection();

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    AdjustmentDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static Product getProduct() {
    return product.get();
  }

  public static void setProduct(Product product) {
    AdjustmentDetailViewModel.product.set(product);
  }

  public static ObjectProperty<Product> productProperty() {
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

  public static long getQuantity() {
    return Long.parseLong(quantity.get());
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

  public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
    return adjustmentDetails.get();
  }

  public static void setAdjustmentDetails(ObservableList<AdjustmentDetail> adjustmentDetails) {
    AdjustmentDetailViewModel.adjustmentDetails.set(adjustmentDetails);
  }

  public static ListProperty<AdjustmentDetail> adjustmentDetailsProperty() {
    return adjustmentDetails;
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

    Platform.runLater(
        () -> {
          adjustmentDetailsList.add(adjustmentDetail);
          resetProperties();
        });
  }

  public static void saveAdjustmentDetails() throws SQLException {
    Dao<AdjustmentDetail, Long> adjustmentDetailDao =
        DaoManager.createDao(connectionSource, AdjustmentDetail.class);

    adjustmentDetailDao.create(adjustmentDetailsList);

    setProductQuantity();

    Platform.runLater(adjustmentDetailsList::clear);
  }

  private static void setProductQuantity() {
    adjustmentDetailsList.forEach(
        adjustmentDetail -> {
          long productQuantity = 0;

          try {

            if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("INCREMENT")) {
              productQuantity =
                  adjustmentDetail.getProduct().getQuantity() + adjustmentDetail.getQuantity();
            } else if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("DECREMENT")) {
              productQuantity =
                  adjustmentDetail.getProduct().getQuantity() - adjustmentDetail.getQuantity();
            }

            createAdjustmentTransaction(adjustmentDetail);

            ProductViewModel.setQuantity(productQuantity);

            ProductViewModel.updateProductQuantity(adjustmentDetail.getProduct().getId());
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private static void updateProductQuantity() {
    adjustmentDetailsList.forEach(
        adjustmentDetail -> {
          long productQuantity = 0;
          long currentProductQuantity = adjustmentDetail.getProduct().getQuantity();

          try {
            AdjustmentTransaction adjustmentTransaction =
                getAdjustmentTransaction(adjustmentDetail.getId());

            long adjustQuantity = adjustmentTransaction.getAdjustQuantity();

            if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("INCREMENT")) {
              productQuantity =
                  (currentProductQuantity - adjustQuantity) + adjustmentDetail.getQuantity();

              adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
            } else if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("DECREMENT")) {
              productQuantity =
                  (currentProductQuantity + adjustQuantity) - adjustmentDetail.getQuantity();

              adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
            }

            ProductViewModel.setQuantity(productQuantity);
            ProductViewModel.updateProductQuantity(adjustmentDetail.getProduct().getId());

            updateAdjustmentTransaction(adjustmentDetail);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static void updateAdjustmentDetail(long index) {
    AdjustmentDetail adjustmentDetail = adjustmentDetailsList.get((int) index);
    adjustmentDetail.setProduct(getProduct());
    adjustmentDetail.setQuantity(getQuantity());
    adjustmentDetail.setAdjustmentType(getAdjustmentType());

    Platform.runLater(
        () -> {
          adjustmentDetailsList.remove(getTempId());
          adjustmentDetailsList.add(getTempId(), adjustmentDetail);

          resetProperties();
        });
  }

  public static void getAllAdjustmentDetails() throws SQLException {
    Dao<AdjustmentDetail, Long> adjustmentDetailDao =
        DaoManager.createDao(connectionSource, AdjustmentDetail.class);

    Platform.runLater(
        () -> {
          adjustmentDetailsList.clear();

          try {
            adjustmentDetailsList.addAll(adjustmentDetailDao.queryForAll());
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static void getAdjustmentDetail(AdjustmentDetail adjustmentDetail) throws SQLException {
    Platform.runLater(
        () -> {
          setTempId(getAdjustmentDetails().indexOf(adjustmentDetail));
          setProduct(adjustmentDetail.getProduct());
          setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
          setAdjustmentType(adjustmentDetail.getAdjustmentType());
        });
  }

  public static void updateAdjustmentDetails() throws SQLException {
    Dao<AdjustmentDetail, Long> adjustmentDetailDao =
        DaoManager.createDao(connectionSource, AdjustmentDetail.class);

    adjustmentDetailsList.forEach(
        adjustmentDetail -> {
          try {
            adjustmentDetailDao.update(adjustmentDetail);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });

    updateProductQuantity();

    getAllAdjustmentDetails();
  }

  public static void removeAdjustmentDetail(long index, int tempIndex) {
    Platform.runLater(() -> adjustmentDetailsList.remove(tempIndex));
    PENDING_DELETES.add(index);
  }

  public static void deleteAdjustmentDetails(@NotNull LinkedList<Long> indexes) {
    indexes.forEach(
        index -> {
          try {
            Dao<AdjustmentDetail, Long> adjustmentDetailDao =
                DaoManager.createDao(connectionSource, AdjustmentDetail.class);

            adjustmentDetailDao.deleteById(index);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static ObservableList<AdjustmentDetail> getAdjustmentDetailsList() {
    return adjustmentDetailsList;
  }

  private static AdjustmentTransaction getAdjustmentTransaction(long adjustmentIndex)
      throws SQLException {
    Dao<AdjustmentTransaction, Long> adjustmentTransactionDao =
        DaoManager.createDao(connectionSource, AdjustmentTransaction.class);

    PreparedQuery<AdjustmentTransaction> preparedQuery =
        adjustmentTransactionDao
            .queryBuilder()
            .where()
            .eq("adjustment_detail_id", adjustmentIndex)
            .prepare();

    return adjustmentTransactionDao.queryForFirst(preparedQuery);
  }

  private static void createAdjustmentTransaction(@NotNull AdjustmentDetail adjustmentDetail)
      throws SQLException {
    Dao<AdjustmentTransaction, Long> adjustmentTransactionDao =
        DaoManager.createDao(connectionSource, AdjustmentTransaction.class);

    AdjustmentTransaction adjustmentTransaction = new AdjustmentTransaction();
    adjustmentTransaction.setBranch(adjustmentDetail.getAdjustment().getBranch());
    adjustmentTransaction.setAdjustmentDetail(adjustmentDetail);
    adjustmentTransaction.setProduct(adjustmentDetail.getProduct());
    adjustmentTransaction.setAdjustQuantity(adjustmentDetail.getQuantity());
    adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
    adjustmentTransaction.setDate(new Date());

    adjustmentTransactionDao.create(adjustmentTransaction);
  }

  private static void updateAdjustmentTransaction(@NotNull AdjustmentDetail adjustmentDetail)
      throws SQLException {
    Dao<AdjustmentTransaction, Long> adjustmentTransactionDao =
        DaoManager.createDao(connectionSource, AdjustmentTransaction.class);

    AdjustmentTransaction adjustmentTransaction =
        getAdjustmentTransaction(adjustmentDetail.getId());
    adjustmentTransaction.setBranch(adjustmentDetail.getAdjustment().getBranch());
    adjustmentTransaction.setAdjustmentDetail(adjustmentDetail);
    adjustmentTransaction.setProduct(adjustmentDetail.getProduct());
    adjustmentTransaction.setAdjustQuantity(adjustmentDetail.getQuantity());
    adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
    adjustmentTransaction.setDate(new Date());

    adjustmentTransactionDao.update(adjustmentTransaction);
  }
}
