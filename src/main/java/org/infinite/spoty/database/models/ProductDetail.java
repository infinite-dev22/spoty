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

@DatabaseTable(tableName = "product_details")
public class ProductDetail implements Serializable {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(
      columnName = "product_id",
      foreign = true,
      canBeNull = false,
      foreignAutoCreate = true,
      foreignAutoRefresh = true,
      columnDefinition =
          "INTEGER CONSTRAINT FK_NAME REFERENCES product_masters(id) ON DELETE CASCADE")
  private ProductMaster product;

  @DatabaseField(foreign = true, columnName = "branch_id") private Branch branch;

  @DatabaseField(foreign = true, columnName = "unit_id")
  private UnitOfMeasure unit;

  @DatabaseField(foreign = true, columnName = "sale_unit_id")
  private UnitOfMeasure saleUnit;

  @DatabaseField(foreign = true, columnName = "purchase_unit_id")
  private UnitOfMeasure purchaseUnit;

  @DatabaseField private String name;
  @DatabaseField private long quantity;
  @DatabaseField private double cost;
  @DatabaseField private double price;

  @DatabaseField(columnName = "net_tax")
  private double netTax;

  @DatabaseField(columnName = "tax_type")
  private String taxType;

  @DatabaseField(columnName = "stock_alert")
  private long stockAlert;

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

  public ProductDetail() {}

  public ProductDetail(
      UnitOfMeasure unit,
      String name,
      long quantity,
      double cost,
      double price,
      double netTax,
      String taxType,
      long stockAlert,
      String serialNumber) {
    this.unit = unit;
    this.name = name;
    this.quantity = quantity;
    this.cost = cost;
    this.price = price;
    this.netTax = netTax;
    this.taxType = taxType;
    this.stockAlert = stockAlert;
    this.serialNumber = serialNumber;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public ProductMaster getProduct() {
    return product;
  }

  public void setProduct(ProductMaster product) {
    this.product = product;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public Branch getBranch() {
    return branch;
  }

  public void setBranch(Branch branch) {
    this.branch = branch;
  }

  public UnitOfMeasure getUnit() {
    return unit;
  }

  public void setUnit(UnitOfMeasure unit) {
    this.unit = unit;
  }

  public String getUnitName() {
    return unit != null ? unit.getName() : null;
  }

  public UnitOfMeasure getSaleUnit() {
    return saleUnit;
  }

  public void setSaleUnit(UnitOfMeasure saleUnit) {
    this.saleUnit = saleUnit;
  }

  public UnitOfMeasure getPurchaseUnit() {
    return purchaseUnit;
  }

  public void setPurchaseUnit(UnitOfMeasure purchaseUnit) {
    this.purchaseUnit = purchaseUnit;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
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

  public long getStockAlert() {
    return stockAlert;
  }

  public void setStockAlert(long stockAlert) {
    this.stockAlert = stockAlert;
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
