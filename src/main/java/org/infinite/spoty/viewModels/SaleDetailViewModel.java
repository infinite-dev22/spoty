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
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Product;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.database.models.SaleTransaction;
import org.jetbrains.annotations.NotNull;

public class SaleDetailViewModel {
  public static final ObservableList<SaleDetail> saleDetailList =
      FXCollections.observableArrayList();
  private static final ListProperty<SaleDetail> saleDetails =
      new SimpleListProperty<>(saleDetailList);
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty ref = new SimpleStringProperty();
  private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
  private static final StringProperty serial = new SimpleStringProperty();
  private static final DoubleProperty subTotalPrice = new SimpleDoubleProperty();
  private static final StringProperty netTax = new SimpleStringProperty();
  private static final StringProperty taxType = new SimpleStringProperty();
  private static final StringProperty discount = new SimpleStringProperty();
  private static final StringProperty discountType = new SimpleStringProperty();
  private static final DoubleProperty price = new SimpleDoubleProperty();
  private static final StringProperty quantity = new SimpleStringProperty();
  private static final SQLiteConnection connection = SQLiteConnection.getInstance();
  private static final ConnectionSource connectionSource = connection.getConnection();

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    SaleDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getRef() {
    return ref.get();
  }

  public static void setRef(String ref) {
    SaleDetailViewModel.ref.set(ref);
  }

  public static StringProperty refProperty() {
    return ref;
  }

  public static Product getProduct() {
    return product.get();
  }

  public static void setProduct(Product product) {
    SaleDetailViewModel.product.set(product);
  }

  public static ObjectProperty<Product> productProperty() {
    return product;
  }

  public static String getSerial() {
    return serial.get();
  }

  public static void setSerial(String serial) {
    SaleDetailViewModel.serial.set(serial);
  }

  public static StringProperty serialProperty() {
    return serial;
  }

  public static double getSubTotalPrice() {
    return subTotalPrice.get();
  }

  public static void setSubTotalPrice(double price) {
    SaleDetailViewModel.subTotalPrice.set(price);
  }

  public static DoubleProperty subTotalPriceProperty() {
    return subTotalPrice;
  }

  public static double getNetTax() {
    return (Objects.equals(netTax.get(), null) || netTax.get().isBlank())
        ? 0.0
        : Double.parseDouble(netTax.get());
  }

  public static void setNetTax(String netTax) {
    SaleDetailViewModel.netTax.set(netTax);
  }

  public static StringProperty netTaxProperty() {
    return netTax;
  }

  public static String getTaxType() {
    return taxType.get();
  }

  public static void setTaxType(String taxType) {
    SaleDetailViewModel.taxType.set(taxType);
  }

  public static StringProperty taxTypeProperty() {
    return taxType;
  }

  public static double getDiscount() {
    return (Objects.equals(discount.get(), null) || discount.get().isBlank())
        ? 0.0
        : Double.parseDouble(discount.get());
  }

  public static void setDiscount(String discount) {
    SaleDetailViewModel.discount.set(discount);
  }

  public static StringProperty discountProperty() {
    return discount;
  }

  public static String getDiscountType() {
    return discountType.get();
  }

  public static void setDiscountType(String discountType) {
    SaleDetailViewModel.discountType.set(discountType);
  }

  public static StringProperty discountTypeProperty() {
    return discountType;
  }

  public static double getPrice() {
    return price.get();
  }

  public static void setPrice(double price) {
    SaleDetailViewModel.price.set(price);
  }

  public static DoubleProperty totalProperty() {
    return price;
  }

  public static long getQuantity() {
    return Long.parseLong(quantity.get());
  }

  public static void setQuantity(long quantity) {
    SaleDetailViewModel.quantity.set((quantity < 1) ? "" : String.valueOf(quantity));
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static ObservableList<SaleDetail> getSaleDetails() {
    return saleDetails.get();
  }

  public static void setSaleDetails(ObservableList<SaleDetail> saleDetails) {
    SaleDetailViewModel.saleDetails.set(saleDetails);
  }

  public static ListProperty<SaleDetail> saleDetailsProperty() {
    return saleDetails;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setSerial("");
    setNetTax("");
    setTaxType("");
    setDiscount("");
    setQuantity(0);
    setDiscountType("");
    setPrice(0);
    setSubTotalPrice(0);
  }

  public static void addSaleDetail() {
    SaleDetail saleDetail =
        new SaleDetail(
            getProduct(),
            getQuantity(),
            getSerial(),
            getNetTax(),
            getTaxType(),
            getDiscount(),
            getDiscountType(),
            getPrice(),
            getSubTotalPrice());

    Platform.runLater(
        () -> {
          saleDetailList.add(saleDetail);
          resetProperties();
        });
  }

  public static void saveSaleDetails() throws SQLException {
    Dao<SaleDetail, Long> saleDetailDao = DaoManager.createDao(connectionSource, SaleDetail.class);

    saleDetailDao.create(saleDetailList);

    setProductQuantity();

    Platform.runLater(saleDetailList::clear);
  }

  private static void setProductQuantity() {
    saleDetailList.forEach(
        saleDetail -> {
          long productDetailQuantity =
              saleDetail.getProduct().getQuantity() - saleDetail.getQuantity();

          ProductViewModel.setQuantity(productDetailQuantity);

          try {
            ProductViewModel.updateProductQuantity(saleDetail.getProduct().getId());
            createSaleTransaction(saleDetail);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private static void updateProductQuantity() {
    saleDetailList.forEach(
        saleDetail -> {
          try {
            SaleTransaction saleTransaction = getSaleTransaction(saleDetail.getId());

            long adjustQuantity = saleTransaction.getSaleQuantity();
            long currentProductQuantity = saleDetail.getProduct().getQuantity();
            long productQuantity =
                (currentProductQuantity + adjustQuantity) - saleDetail.getQuantity();

            ProductViewModel.setQuantity(productQuantity);

            ProductViewModel.updateProductQuantity(saleDetail.getProduct().getId());

            updateSaleTransaction(saleDetail);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static void updateSaleDetail(long index) throws SQLException {
    SaleDetail saleDetail = getSaleDetails().get((int) index);

    saleDetail.setProduct(getProduct());
    saleDetail.setQuantity(getQuantity());
    saleDetail.setSerialNumber(getSerial());
    saleDetail.setPrice(getPrice());
    saleDetail.setNetTax(getNetTax());
    saleDetail.setTaxType(getTaxType());
    saleDetail.setSubTotalPrice(getSubTotalPrice());
    saleDetail.setDiscount(getDiscount());
    saleDetail.setDiscountType(getDiscountType());

    Platform.runLater(
        () -> {
          getSaleDetails().remove(getTempId());
          getSaleDetails().add(getTempId(), saleDetail);

          resetProperties();
        });
  }

  public static void getSaleDetail(SaleDetail saleDetail) throws SQLException {
    Platform.runLater(
        () -> {
          setTempId(getSaleDetails().indexOf(saleDetail));
          setId(saleDetail.getId());
          setProduct(saleDetail.getProduct());
          setSerial(saleDetail.getSerialNumber());
          setNetTax(String.valueOf(saleDetail.getNetTax()));
          setTaxType(saleDetail.getTaxType());
          setDiscount(String.valueOf(saleDetail.getDiscount()));
          setDiscountType(saleDetail.getDiscountType());
          setQuantity(saleDetail.getQuantity());
          setProduct(saleDetail.getProduct());
          setPrice(saleDetail.getPrice());
          setSubTotalPrice(saleDetail.getSubTotalPrice());
        });
  }

  public static void updateItem(long index) throws SQLException {
    Dao<SaleDetail, Long> saleDetailDao = DaoManager.createDao(connectionSource, SaleDetail.class);

    SaleDetail saleDetail = saleDetailDao.queryForId(index);

    saleDetail.setProduct(getProduct());
    saleDetail.setQuantity(getQuantity());
    saleDetail.setSerialNumber(getSerial());
    saleDetail.setNetTax(getNetTax());
    saleDetail.setTaxType(getTaxType());
    saleDetail.setDiscount(getDiscount());
    saleDetail.setDiscountType(getDiscountType());

    saleDetailDao.update(saleDetail);

    getAllSaleDetails();
  }

  public static void updateSaleDetails() throws SQLException {
    Dao<SaleDetail, Long> saleDetailDao = DaoManager.createDao(connectionSource, SaleDetail.class);

    saleDetailList.forEach(
        saleDetail -> {
          try {
            saleDetailDao.update(saleDetail);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });

    updateProductQuantity();

    getAllSaleDetails();
  }

  public static void getAllSaleDetails() throws SQLException {
    Dao<SaleDetail, Long> saleDetailDao = DaoManager.createDao(connectionSource, SaleDetail.class);

    Platform.runLater(
        () -> {
          saleDetailList.clear();

          try {
            saleDetailList.addAll(saleDetailDao.queryForAll());
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static void removeSaleDetail(long index, int tempIndex) {
    Platform.runLater(() -> saleDetailList.remove(tempIndex));
    PENDING_DELETES.add(index);
  }

  public static void deleteSaleDetails(LinkedList<Long> indexes) {
    indexes.forEach(
        index -> {
          try {
            Dao<SaleDetail, Long> saleDetailDao =
                DaoManager.createDao(connectionSource, SaleDetail.class);

            saleDetailDao.deleteById(index);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static ObservableList<SaleDetail> getSaleDetailList() {
    return saleDetailList;
  }

  private static SaleTransaction getSaleTransaction(long saleIndex) throws SQLException {
    Dao<SaleTransaction, Long> saleTransactionDao =
        DaoManager.createDao(connectionSource, SaleTransaction.class);

    PreparedQuery<SaleTransaction> preparedQuery =
        saleTransactionDao
            .queryBuilder()
            .where()
            .eq("sale_detail_id", saleIndex)
            .prepare();

    return saleTransactionDao.queryForFirst(preparedQuery);
  }

  private static void createSaleTransaction(@NotNull SaleDetail saleDetail)
      throws SQLException {
    Dao<SaleTransaction, Long> saleTransactionDao =
        DaoManager.createDao(connectionSource, SaleTransaction.class);

    SaleTransaction saleTransaction = new SaleTransaction();
    saleTransaction.setBranch(saleDetail.getSaleMaster().getBranch());
    saleTransaction.setSaleDetail(saleDetail);
    saleTransaction.setProduct(saleDetail.getProduct());
    saleTransaction.setSaleQuantity(saleDetail.getQuantity());
    saleTransaction.setDate(new Date());

    saleTransactionDao.create(saleTransaction);
  }

  private static void updateSaleTransaction(@NotNull SaleDetail saleDetail)
      throws SQLException {
    Dao<SaleTransaction, Long> saleTransactionDao =
        DaoManager.createDao(connectionSource, SaleTransaction.class);

    SaleTransaction saleTransaction = getSaleTransaction(saleDetail.getId());
    saleTransaction.setBranch(saleDetail.getSaleMaster().getBranch());
    saleTransaction.setSaleDetail(saleDetail);
    saleTransaction.setProduct(saleDetail.getProduct());
    saleTransaction.setSaleQuantity(saleDetail.getQuantity());
    saleTransaction.setDate(new Date());

    saleTransactionDao.update(saleTransaction);
  }
}
