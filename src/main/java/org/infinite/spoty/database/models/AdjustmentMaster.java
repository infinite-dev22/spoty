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

@DatabaseTable(tableName = "adjustment_masters")
public class AdjustmentMaster implements Serializable {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(foreign = true, columnName = "user_id", foreignAutoRefresh = true)
  private User user;

  @DatabaseField private Date date;
  @DatabaseField private String ref;

  @DatabaseField(foreign = true, columnName = "branch_id", canBeNull = false, foreignAutoRefresh = true)
  private Branch branch;

  @ForeignCollectionField
  private Collection<AdjustmentDetail> adjustmentDetails;

  @DatabaseField
  private String notes;

  @DatabaseField
  private String status;

  @DatabaseField(columnName = "total_amount")
  private double totalAmount;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public AdjustmentMaster() {}

  public AdjustmentMaster(Branch branch, String notes, Date date) {
    this.date = date;
    this.branch = branch;
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getLocaleDate() {
    return DateFormat.getDateInstance().format(date);
  }

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public Branch getBranch() {
    return branch;
  }

  public void setBranch(Branch branch) {
    this.branch = branch;
  }

  public String getBranchName() {
    if (branch != null) return branch.getName();
    else return null;
  }

  public Collection<AdjustmentDetail> getAdjustmentDetails() {
    return adjustmentDetails;
  }

  public void setAdjustmentDetails(Collection<AdjustmentDetail> adjustmentDetails) {
    this.adjustmentDetails = adjustmentDetails;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
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
