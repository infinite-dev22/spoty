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
import org.infinite.spoty.database.dao.QuotationDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.QuotationDetail;
import org.infinite.spoty.database.models.QuotationMaster;
import org.infinite.spoty.database.models.UnitOfMeasure;

public class QuotationDetailViewModel {
  // TODO: Add more fields according to DB design and necessity.
  public static final ObservableList<QuotationDetail> quotationDetailsList =
      FXCollections.observableArrayList();
  public static final ObservableList<QuotationDetail> quotationDetailTempList =
      FXCollections.observableArrayList();
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>();
  private static final ObjectProperty<QuotationMaster> quotation = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty();
  private static final StringProperty tax = new SimpleStringProperty();
  private static final StringProperty discount = new SimpleStringProperty();

  public static int getId() {
    return id.get();
  }

  public static void setId(int id) {
    QuotationDetailViewModel.id.set(id);
  }

  public static IntegerProperty idProperty() {
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

  public static int getQuantity() {
    return Integer.parseInt(quantity.get());
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

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setTax("");
    setDiscount("");
    setQuantity("");
  }

  public static void addQuotationDetails() {
    QuotationDetail quotationDetail =
        new QuotationDetail(
            getProduct(), getProduct().getSaleUnit(), getTax(), getDiscount(), getQuantity());
    quotationDetailTempList.add(quotationDetail);
    resetProperties();
  }

  public static void updateQuotationDetail(int index) {
    QuotationDetail quotationDetail = QuotationDetailDao.findQuotationDetail(index);
    quotationDetail.setProduct(getProduct());
    quotationDetail.setSaleUnit(getSaleUnit());
    quotationDetail.setNetTax(getTax());
    quotationDetail.setDiscount(getDiscount());
    quotationDetail.setQuantity(getQuantity());
    quotationDetail.setId(getId());
    quotationDetail.setQuotation(getQuotation());
    QuotationDetailDao.updateQuotationDetail(quotationDetail, getId());
    quotationDetailTempList.remove((int) getTempId());
    quotationDetailTempList.add(getTempId(), quotationDetail);
    resetProperties();
  }

  public static ObservableList<QuotationDetail> getQuotationDetails() {
    quotationDetailsList.clear();
    quotationDetailsList.addAll(QuotationDetailDao.fetchQuotationDetails());
    return quotationDetailsList;
  }

  public static void getItem(int index, int tempIndex) {
    QuotationDetail quotationDetail = QuotationDetailDao.findQuotationDetail(index);
    setTempId(tempIndex);
    setId(quotationDetail.getId());
    setProduct(quotationDetail.getProduct());
    setSaleUnit(quotationDetail.getProduct().getSaleUnit());
    setTax(String.valueOf(quotationDetail.getNetTax()));
    setDiscount(String.valueOf(quotationDetail.getDiscount()));
    setQuantity(String.valueOf(quotationDetail.getQuantity()));
    setQuotation(quotationDetail.getQuotation());
    getQuotationDetails();
  }

  public static void updateItem(int index) {
    QuotationDetail quotationDetail = QuotationDetailDao.findQuotationDetail(index);
    quotationDetail.setProduct(getProduct());
    quotationDetail.setSaleUnit(getSaleUnit());
    quotationDetail.setNetTax(getTax());
    quotationDetail.setDiscount(getDiscount());
    quotationDetail.setQuantity(getQuantity());
    QuotationDetailDao.updateQuotationDetail(quotationDetail, index);
    getQuotationDetails();
  }

  public static void removeQuotationDetail(int index, int tempIndex) {
    quotationDetailTempList.remove(tempIndex);
    PENDING_DELETES.add(index);
  }

  public static void deleteQuotationDetails(LinkedList<Integer> indexes) {
    indexes.forEach(QuotationDetailDao::deleteQuotationDetail);
  }
}
