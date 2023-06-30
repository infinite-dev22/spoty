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
import org.infinite.spoty.database.dao.SaleDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.database.models.UnitOfMeasure;

public class SaleDetailViewModel {
  public static final ObservableList<SaleDetail> saleDetailTempList =
      FXCollections.observableArrayList();
  public static final ObservableList<SaleDetail> saleDetailList =
      FXCollections.observableArrayList();
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final StringProperty ref = new SimpleStringProperty();
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final StringProperty serial = new SimpleStringProperty();
  private static final StringProperty price = new SimpleStringProperty();
  private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>();
  private static final StringProperty netTax = new SimpleStringProperty();
  private static final StringProperty taxType = new SimpleStringProperty();
  private static final StringProperty discount = new SimpleStringProperty();
  private static final StringProperty discountType = new SimpleStringProperty();
  private static final StringProperty total = new SimpleStringProperty();
  private static final StringProperty quantity = new SimpleStringProperty();

  public static int getId() {
    return id.get();
  }

  public static void setId(int id) {
    SaleDetailViewModel.id.set(id);
  }

  public static IntegerProperty idProperty() {
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

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    SaleDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
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

  public static String getPrice() {
    return price.get();
  }

  public static void setPrice(String price) {
    SaleDetailViewModel.price.set(price);
  }

  public static StringProperty priceProperty() {
    return price;
  }

  public static UnitOfMeasure getSaleUnit() {
    return saleUnit.get();
  }

  public static void setSaleUnit(UnitOfMeasure saleUnit) {
    SaleDetailViewModel.saleUnit.set(saleUnit);
  }

  public static ObjectProperty<UnitOfMeasure> saleUnitProperty() {
    return saleUnit;
  }

  public static String getNetTax() {
    return netTax.get();
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

  public static String getDiscount() {
    return discount.get();
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

  public static String getTotal() {
    return total.get();
  }

  public static void setTotal(String total) {
    SaleDetailViewModel.total.set(total);
  }

  public static StringProperty totalProperty() {
    return total;
  }

  public static String getQuantity() {
    return quantity.get();
  }

  public static void setQuantity(String quantity) {
    SaleDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setSerial("");
    setSaleUnit(null);
    setNetTax("");
    setTaxType("");
    setDiscount("");
    setQuantity("");
    setDiscountType("");
  }

  public static void addSaleDetail() {
    SaleDetail saleDetail =
        new SaleDetail(
            getProduct(),
            Integer.parseInt(getQuantity()),
            getSerial(),
            Double.parseDouble(getNetTax()),
            getTaxType(),
            Double.parseDouble(getDiscount()),
            getDiscountType());
    saleDetailTempList.add(saleDetail);
    saleDetailTempList.forEach(System.out::println);
    resetProperties();
  }

  public static void updateSaleDetail(int index) {
    SaleDetail saleDetail = SaleDetailDao.findSaleDetail(index);
    saleDetail.setProduct(getProduct());
    saleDetail.setQuantity(Integer.parseInt(getQuantity()));
    saleDetail.setSerialNumber(getSerial());
    saleDetail.setNetTax(Double.parseDouble(getNetTax()));
    saleDetail.setTaxType(getTaxType());
    saleDetail.setDiscount(Double.parseDouble(getDiscount()));
    saleDetail.setDiscountType(getDiscountType());
    saleDetailTempList.remove((int) getTempId());
    saleDetailTempList.add(getTempId(), saleDetail);
    resetProperties();
  }

  public static void getItem(int index, int tempIndex) {
    SaleDetail saleDetail = SaleDetailDao.findSaleDetail(index);
    setTempId(tempIndex);
    setId(saleDetail.getId());
    setProduct(saleDetail.getProduct());
    setSerial(saleDetail.getSerialNumber());
    setNetTax(String.valueOf(saleDetail.getNetTax()));
    setTaxType(saleDetail.getTaxType());
    setDiscount(String.valueOf(saleDetail.getDiscount()));
    setDiscountType(saleDetail.getDiscountType());
    setQuantity(String.valueOf(saleDetail.getQuantity()));
    setProduct(saleDetail.getProduct());
    setTotal(String.valueOf(saleDetail.getTotal()));
    setQuantity(String.valueOf(saleDetail.getQuantity()));
    setPrice(String.valueOf(saleDetail.getPrice()));
  }

  public static void updateItem(int index) {
    SaleDetail saleDetail = SaleDetailDao.findSaleDetail(index);
    saleDetail.setProduct(getProduct());
    saleDetail.setQuantity(Integer.parseInt(getQuantity()));
    saleDetail.setSerialNumber(getSerial());
    saleDetail.setNetTax(Double.parseDouble(getNetTax()));
    saleDetail.setTaxType(getTaxType());
    saleDetail.setDiscount(Double.parseDouble(getDiscount()));
    saleDetail.setDiscountType(getDiscountType());
    SaleDetailDao.updateSaleDetail(saleDetail, index);
    getSaleDetails();
  }

  public static ObservableList<SaleDetail> getSaleDetails() {
    saleDetailList.clear();
    saleDetailList.addAll(SaleDetailDao.fetchSaleDetails());
    return saleDetailList;
  }

  public static void removeSaleDetail(int index, int tempIndex) {
    saleDetailTempList.remove(tempIndex);
    PENDING_DELETES.add(index);
  }

  public static void deleteSaleDetails(LinkedList<Integer> indexes) {
    indexes.forEach(SaleDetailDao::deleteSaleDetail);
  }
}
