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
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.*;

public class QuotationDetailViewModel {
  // TODO: Add more fields according to DB design and necessity.
  public static final ObservableList<QuotationDetail> quotationDetailsList =
      FXCollections.observableArrayList();
  private static final ListProperty<QuotationDetail> quotationDetails =
      new SimpleListProperty<>(quotationDetailsList);
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>();
  private static final ObjectProperty<QuotationMaster> quotation = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty();
  private static final StringProperty tax = new SimpleStringProperty();
  private static final StringProperty discount = new SimpleStringProperty();

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    QuotationDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    QuotationDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
    return product;
  }

  public static UnitOfMeasure getSaleUnit() {
    return saleUnit.get();
  }

  public static void setSaleUnit(UnitOfMeasure saleUnit) {
    QuotationDetailViewModel.saleUnit.set(saleUnit);
  }

  public static ObjectProperty<UnitOfMeasure> saleUnitProperty() {
    return saleUnit;
  }

  public static QuotationMaster getQuotation() {
    return quotation.get();
  }

  public static void setQuotation(QuotationMaster quotation) {
    QuotationDetailViewModel.quotation.set(quotation);
  }

  public static ObjectProperty<QuotationMaster> quotationProperty() {
    return quotation;
  }

  public static long getQuantity() {
    return Long.parseLong(quantity.get());
  }

  public static void setQuantity(String quantity) {
    QuotationDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static double getTax() {
    return Double.parseDouble(tax.get());
  }

  public static void setTax(String tax) {
    QuotationDetailViewModel.tax.set(tax);
  }

  public static StringProperty taxProperty() {
    return tax;
  }

  public static double getDiscount() {
    return Double.parseDouble(discount.get());
  }

  public static void setDiscount(String discount) {
    QuotationDetailViewModel.discount.set(discount);
  }

  public static StringProperty discountProperty() {
    return discount;
  }

  public static ObservableList<QuotationDetail> getQuotationDetails() {
    return quotationDetails.get();
  }

  public static void setQuotationDetails(ObservableList<QuotationDetail> quotationDetails) {
    QuotationDetailViewModel.quotationDetails.set(quotationDetails);
  }

  public static ListProperty<QuotationDetail> quotationDetailsProperty() {
    return quotationDetails;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setTax("");
    setDiscount("");
    setQuantity("");
  }

  public static void addQuotationDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            QuotationDetail quotationDetail =
                new QuotationDetail(getProduct(), getTax(), getDiscount(), getQuantity());

            Platform.runLater(() -> quotationDetailsList.add(quotationDetail));

            return null;
          }
        };

    task.setOnSucceeded(event -> resetProperties());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void saveQuotationDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<QuotationDetail, Long> quotationDetailDao =
                DaoManager.createDao(connectionSource, QuotationDetail.class);

            quotationDetailDao.create(quotationDetailsList);

            return null;
          }
        };

    task.setOnSucceeded(event -> quotationDetailsList.clear());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void updateQuotationDetail(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<QuotationDetail, Long> quotationDetailDao =
                DaoManager.createDao(connectionSource, QuotationDetail.class);

            QuotationDetail quotationDetail = quotationDetailDao.queryForId(index);
            quotationDetail.setProduct(getProduct());
            quotationDetail.setSaleUnit(getSaleUnit());
            quotationDetail.setNetTax(getTax());
            quotationDetail.setDiscount(getDiscount());
            quotationDetail.setQuantity(getQuantity());
            quotationDetail.setId(getId());
            quotationDetail.setQuotation(getQuotation());

            Platform.runLater(
                () -> {
                  quotationDetailsList.remove((int) getTempId());
                  quotationDetailsList.add(getTempId(), quotationDetail);
                });

            return null;
          }
        };

    task.setOnSucceeded(event -> resetProperties());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void getAllQuotationDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<QuotationDetail, Long> quotationDetailDao =
                DaoManager.createDao(connectionSource, QuotationDetail.class);

            quotationDetailsList.clear();
            quotationDetailsList.addAll(quotationDetailDao.queryForAll());
            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void getItem(long index, int tempIndex) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<QuotationDetail, Long> quotationDetailDao =
                DaoManager.createDao(connectionSource, QuotationDetail.class);

            QuotationDetail quotationDetail = quotationDetailDao.queryForId(index);
            setTempId(tempIndex);
            setId(quotationDetail.getId());
            setProduct(quotationDetail.getProduct());
            setSaleUnit(quotationDetail.getProduct().getSaleUnit());
            setTax(String.valueOf(quotationDetail.getNetTax()));
            setDiscount(String.valueOf(quotationDetail.getDiscount()));
            setQuantity(String.valueOf(quotationDetail.getQuantity()));
            setQuotation(quotationDetail.getQuotation());

            return null;
          }
        };

    task.setOnSucceeded(event -> getAllQuotationDetails());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void updateItem(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<QuotationDetail, Long> quotationDetailDao =
                DaoManager.createDao(connectionSource, QuotationDetail.class);

            QuotationDetail quotationDetail = quotationDetailDao.queryForId(index);
            quotationDetail.setProduct(getProduct());
            quotationDetail.setSaleUnit(getSaleUnit());
            quotationDetail.setNetTax(getTax());
            quotationDetail.setDiscount(getDiscount());
            quotationDetail.setQuantity(getQuantity());

            quotationDetailDao.update(quotationDetail);

            return null;
          }
        };

    task.setOnSucceeded(event -> getAllQuotationDetails());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void updateQuotationDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<QuotationDetail, Long> quotationDetailDao =
                DaoManager.createDao(connectionSource, QuotationDetail.class);

            quotationDetailsList.forEach(
                quotationDetail -> {
                  try {
                    quotationDetailDao.update(quotationDetail);
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });

            return null;
          }
        };

    task.setOnSucceeded(event -> getAllQuotationDetails());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void removeQuotationDetail(long index, int tempIndex) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            Platform.runLater(() -> quotationDetailsList.remove(tempIndex));
            PENDING_DELETES.add(index);
            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void deleteQuotationDetails(LinkedList<Long> indexes) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            indexes.forEach(
                index -> {
                  try {
                    SQLiteConnection connection = SQLiteConnection.getInstance();
                    ConnectionSource connectionSource = connection.getConnection();

                    Dao<QuotationDetail, Long> quotationDetailDao =
                        DaoManager.createDao(connectionSource, QuotationDetail.class);

                    quotationDetailDao.deleteById(index);
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });
            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static ObservableList<QuotationDetail> getQuotationDetailsList() {
    return quotationDetailsList;
  }
}
