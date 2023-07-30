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
import java.util.Objects;

@DatabaseTable(tableName = "adjustment_details")
public class AdjustmentDetail implements Serializable {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(foreign = true, columnName = "product_id", canBeNull = false, foreignAutoRefresh = true)
  private Product product;

  @DatabaseField(
      foreign = true,
      columnName = "adjustment_master_id",
      canBeNull = false,
      foreignAutoRefresh = true,
      foreignAutoCreate = true,
      columnDefinition = "INTEGER CONSTRAINT FK_NAME REFERENCES adjustment(id) ON DELETE CASCADE")
  private AdjustmentMaster adjustment;

  @DatabaseField(columnName = "quantity")
  private long quantity;

  @DatabaseField(columnName = "adjustment_type")
  private String adjustmentType;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public AdjustmentDetail() {}

  public AdjustmentDetail(Product product, long quantity, String adjustmentType) {
    this.product = product;
    this.quantity = quantity;
    this.adjustmentType = adjustmentType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getProductName() {
    return (product != null)
        ? product.getBrand().getName()
            + " "
            + product.getName()
            + (Objects.equals(product.getUnit(), null) ? "" : " " + product.getUnit().getName())
        : null;
  }

  public AdjustmentMaster getAdjustment() {
    return adjustment;
  }

  public void setAdjustment(AdjustmentMaster adjustment) {
    this.adjustment = adjustment;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public String getAdjustmentType() {
    return adjustmentType;
  }

  public void setAdjustmentType(String adjustmentType) {
    this.adjustmentType = adjustmentType;
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
