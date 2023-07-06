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

@DatabaseTable(tableName = "stock_in_detail")
public class StockInDetail implements Serializable {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(
      foreign = true,
      columnName = "stock_in_master_id",
      canBeNull = false,
      foreignAutoCreate = true,
      foreignAutoRefresh = true,
      columnDefinition =
          "INTEGER CONSTRAINT FK_NAME REFERENCES stock_in_master(id) ON DELETE CASCADE")
  private StockInMaster stockIn;

  @DatabaseField(foreign = true, columnName = "product_id", canBeNull = false)
  private ProductDetail product;

  @DatabaseField(canBeNull = false)
  private long quantity;

  @DatabaseField(columnName = "serial_number")
  private String serialNo;

  @DatabaseField private String description;
  @DatabaseField private String location;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public StockInDetail() {}

  public StockInDetail(
      ProductDetail product, long quantity, String serialNo, String description, String location) {
    this.product = product;
    this.quantity = quantity;
    this.serialNo = serialNo;
    this.description = description;
    this.location = location;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public StockInMaster getStockIn() {
    return stockIn;
  }

  public void setStockIn(StockInMaster stockIn) {
    this.stockIn = stockIn;
  }

  public ProductDetail getProduct() {
    return product;
  }

  public void setProduct(ProductDetail product) {
    this.product = product;
  }

  public String getProductDetailName() {
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

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public String getSerialNo() {
    return serialNo;
  }

  public void setSerialNo(String serialNo) {
    this.serialNo = serialNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
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
