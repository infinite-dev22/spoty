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

@DatabaseTable(tableName = "products")
public class Product implements Serializable {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(foreign = true, columnName = "unit_id", foreignAutoRefresh = true)
  private UnitOfMeasure unit;

  @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
  private ProductCategory category;

  @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
  private Brand brand;

  @DatabaseField(columnName = "barcode_type")
  private String barcodeType;

  @DatabaseField(columnName = "product_type")
  private String productType;

  @DatabaseField private String name;
  @DatabaseField private long quantity;
  @DatabaseField private double cost;
  @DatabaseField private double price;
  @DatabaseField private double discount;

  @DatabaseField(columnName = "net_tax")
  private double netTax;

  @DatabaseField(columnName = "tax_type")
  private String taxType;

  @DatabaseField(columnName = "stock_alert")
  private long stockAlert;

  @DatabaseField(columnName = "serial_number")
  private String serialNumber;

  @DatabaseField
  private String image;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public Product() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public UnitOfMeasure getUnit() {
    return unit;
  }

  public void setUnit(UnitOfMeasure unit) {
    this.unit = unit;
  }

  public String getUnitName() {
    return unit != null ? unit.getName() : null;
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

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
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

  public ProductCategory getCategory() {
    return category;
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  public Brand getBrand() {
    return brand;
  }

  public void setBrand(Brand brand) {
    this.brand = brand;
  }

  public String getBarcodeType() {
    return barcodeType;
  }

  public void setBarcodeType(String barcodeType) {
    this.barcodeType = barcodeType;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getBrandName() {
    return brand.getName();
  }

  public String getCategoryName() {
    return category.getName();
  }
}
