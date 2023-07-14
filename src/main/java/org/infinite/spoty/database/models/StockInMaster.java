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
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

@DatabaseTable(tableName = "stock_in_master")
public class StockInMaster implements Serializable {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(foreign = true, columnName = "user_id", foreignAutoRefresh = true)
  private User user;

  @DatabaseField(columnName = "reference_number")
  private String ref;

  @DatabaseField(canBeNull = false)
  private Date date;

  @DatabaseField(foreign = true, columnName = "branch_id", canBeNull = false, foreignAutoRefresh = true)
  private Branch branch;

  @ForeignCollectionField private Collection<StockInDetail> stockInDetails;

  @DatabaseField private String shipping;

  @DatabaseField(canBeNull = false, columnName = "total_cost")
  private double totalCost;

  @DatabaseField(canBeNull = false)
  private String status;

  @DatabaseField(foreign = true, columnName = "approved_by_id")
  private User approvedBy;

  @DatabaseField(foreign = true, columnName = "recorded_by_id")
  private User recordedBy;

  @DatabaseField(columnName = "approval_date")
  private Date approvalDate;

  @DatabaseField(columnName = "record_date")
  private Date recordDate;

  @DatabaseField private String notes;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public StockInMaster() {}

  public StockInMaster(Date date, Branch branch, String status, String notes) {
    this.date = date;
    this.branch = branch;
    this.status = status;
    this.notes = notes;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user_detail) {
    this.user = user_detail;
  }

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Branch getBranch() {
    return branch;
  }

  public void setBranch(Branch branch) {
    this.branch = branch;
  }

  public String getBranchName() {
    return (branch != null) ? branch.getName() : null;
  }

  public String getShipping() {
    return shipping;
  }

  public void setShipping(String shipping) {
    this.shipping = shipping;
  }

  public double getTotal() {
    return totalCost;
  }

  public void setTotal(double totalCost) {
    this.totalCost = totalCost;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Collection<StockInDetail> getStockInDetails() {
    return stockInDetails;
  }

  public void setStockInDetails(Collection<StockInDetail> stockInDetails) {
    this.stockInDetails = stockInDetails;
  }

  public User getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(User approvedBy) {
    this.approvedBy = approvedBy;
  }

  public User getReceivedBy() {
    return recordedBy;
  }

  public void setReceivedBy(User recordedBy) {
    this.recordedBy = recordedBy;
  }

  public Date getApprovalDate() {
    return approvalDate;
  }

  public void setApprovalDate(Date approvalDate) {
    this.approvalDate = approvalDate;
  }

  public Date getReceiveDate() {
    return recordDate;
  }

  public void setReceiveDate(Date recordDate) {
    this.recordDate = recordDate;
  }

  public double getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(double totalCost) {
    this.totalCost = totalCost;
  }

  public User getRecordedBy() {
    return recordedBy;
  }

  public void setRecordedBy(User recordedBy) {
    this.recordedBy = recordedBy;
  }

  public Date getRecordDate() {
    return recordDate;
  }

  public void setRecordDate(Date recordDate) {
    this.recordDate = recordDate;
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

  public String getLocaleDate() {
    return DateFormat.getDateInstance().format(date);
  }
}
