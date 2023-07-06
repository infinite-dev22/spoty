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

package org.infinite.spoty.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "quotation_detail")
public class QuotationDetail implements Serializable {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(canBeNull = false)
  private double price = 0;

  @DatabaseField(foreign = true, columnName = "sale_unit_id", canBeNull = false)
  private UnitOfMeasure saleUnit;

  @DatabaseField(foreign = true, columnName = "product_id", canBeNull = false)
  private ProductDetail product;

  @DatabaseField(
      foreign = true,
      columnName = "quotation_id",
      canBeNull = false,
      foreignAutoCreate = true,
      foreignAutoRefresh = true,
      columnDefinition =
          "INTEGER CONSTRAINT FK_NAME REFERENCES quotation_master(id) ON DELETE CASCADE")
  private QuotationMaster quotation;

  @DatabaseField(columnName = "net_tax")
  private double netTax;

  @DatabaseField(columnName = "tax_type")
  private String taxType;

  @DatabaseField private double discount;

  @DatabaseField(columnName = "discount_type")
  private String discountType;

  @DatabaseField(canBeNull = false)
  private double total = 0;

  @DatabaseField(canBeNull = false)
  private long quantity = 0;

  @DatabaseField(columnName = "serial_number")
  private String serialNumber;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public QuotationDetail(
      ProductDetail product,
      UnitOfMeasure saleUnit,
      double netTax,
      double discount,
      long quantity) {
    this.product = product;
    this.saleUnit = saleUnit;
    this.netTax = netTax;
    this.discount = discount;
    this.quantity = quantity;
  }

  public QuotationDetail() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public UnitOfMeasure getSaleUnit() {
    return saleUnit;
  }

  public void setSaleUnit(UnitOfMeasure saleUnit) {
    this.saleUnit = saleUnit;
  }

  public ProductDetail getProduct() {
    return product;
  }

  public void setProduct(ProductDetail product) {
    this.product = product;
  }

  public String getProductName() {
    return (product != null)
        ? product.getProduct().getBrand().getName()
            + " "
            + product.getProduct().getName()
            + " "
            + product.getName()
            + " "
            + product.getUnit().getName()
        : null;
  }

  public QuotationMaster getQuotation() {
    return quotation;
  }

  public void setQuotation(QuotationMaster quotation) {
    this.quotation = quotation;
  }

  public double getNetTax() {
    return netTax;
  }

  public void setNetTax(double netTax) {
    this.netTax = netTax;
  }

  public String getTaxType() {
    return taxType;
  }

  public void setTaxType(String taxType) {
    this.taxType = taxType;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public String getDiscountType() {
    return discountType;
  }

  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
