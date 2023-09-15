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
import java.util.Date;

@DatabaseTable(tableName = "stock_in_transactions")
public class StockInTransaction {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
  private Branch branch;

  @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
  private Product product;

  @DatabaseField(
      foreign = true,
      columnName = "stock_in_detail_id",
      canBeNull = false,
      foreignAutoRefresh = true)
  private StockInDetail stockInDetail;

  @DatabaseField private Date date;

  @DatabaseField(columnName = "stock_in_quantity")
  private long stockInQuantity;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public StockInTransaction() {}

  /**
   * get field @DatabaseField(generatedId = true)
   *
   * @return id @DatabaseField(generatedId = true)
   */
  public int getId() {
    return this.id;
  }

  /**
   * set field @DatabaseField(generatedId = true)
   *
   * @param id @DatabaseField(generatedId = true)
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * get field @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   *
   * @return branch @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   */
  public Branch getBranch() {
    return this.branch;
  }

  /**
   * set field @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   *
   * @param branch @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   */
  public void setBranch(Branch branch) {
    this.branch = branch;
  }

  /**
   * get field @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   *
   * @return product @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   */
  public Product getProduct() {
    return this.product;
  }

  /**
   * set field @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   *
   * @param product @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * get field @DatabaseField( foreign = true, columnName = "stock_in_detail", canBeNull = false,
   * foreignAutoRefresh = true)
   *
   * @return stockInDetail @DatabaseField( foreign = true, columnName = "stock_in_detail", canBeNull
   *     = false, foreignAutoRefresh = true)
   */
  public StockInDetail getStockInDetail() {
    return this.stockInDetail;
  }

  /**
   * set field @DatabaseField( foreign = true, columnName = "stock_in_detail", canBeNull = false,
   * foreignAutoRefresh = true)
   *
   * @param stockInDetail @DatabaseField( foreign = true, columnName = "stock_in_detail", canBeNull
   *     = false, foreignAutoRefresh = true)
   */
  public void setStockInDetail(StockInDetail stockInDetail) {
    this.stockInDetail = stockInDetail;
  }

  /**
   * get field
   *
   * @return date
   */
  public Date getDate() {
    return this.date;
  }

  /**
   * set field
   *
   * @param date
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * get field @DatabaseField(columnName = "stock_in_quantity")
   *
   * @return stockInQuantity @DatabaseField(columnName = "stock_in_quantity")
   */
  public long getStockInQuantity() {
    return this.stockInQuantity;
  }

  /**
   * set field @DatabaseField(columnName = "stock_in_quantity")
   *
   * @param stockInQuantity @DatabaseField(columnName = "stock_in_quantity")
   */
  public void setStockInQuantity(long stockInQuantity) {
    this.stockInQuantity = stockInQuantity;
  }

  /**
   * get field @DatabaseField(columnName = "created_at")
   *
   * @return createdAt @DatabaseField(columnName = "created_at")
   */
  public Date getCreatedAt() {
    return this.createdAt;
  }

  /**
   * set field @DatabaseField(columnName = "created_at")
   *
   * @param createdAt @DatabaseField(columnName = "created_at")
   */
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * get field @DatabaseField(columnName = "created_by")
   *
   * @return createdBy @DatabaseField(columnName = "created_by")
   */
  public String getCreatedBy() {
    return this.createdBy;
  }

  /**
   * set field @DatabaseField(columnName = "created_by")
   *
   * @param createdBy @DatabaseField(columnName = "created_by")
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * get field @DatabaseField(columnName = "updated_at")
   *
   * @return updatedAt @DatabaseField(columnName = "updated_at")
   */
  public Date getUpdatedAt() {
    return this.updatedAt;
  }

  /**
   * set field @DatabaseField(columnName = "updated_at")
   *
   * @param updatedAt @DatabaseField(columnName = "updated_at")
   */
  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   * get field @DatabaseField(columnName = "updated_by")
   *
   * @return updatedBy @DatabaseField(columnName = "updated_by")
   */
  public String getUpdatedBy() {
    return this.updatedBy;
  }

  /**
   * set field @DatabaseField(columnName = "updated_by")
   *
   * @param updatedBy @DatabaseField(columnName = "updated_by")
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
