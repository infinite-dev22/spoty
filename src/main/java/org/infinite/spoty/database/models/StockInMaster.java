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

import jakarta.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class StockInMaster implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne private User user_detail;
  private String ref;
  private Date date;
  @ManyToOne private Branch branch;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "stockIn")
  private List<StockInDetail> stockInDetails;

  private String shipping;
  private double totalCost;
  private String status;
  private User approvedBy;
  private User recordedBy;
  private Date approvalDate;
  private Date recordDate;
  private String notes;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "updated_by")
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
    return user_detail;
  }

  public void setUser(User user_detail) {
    this.user_detail = user_detail;
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

  public List<StockInDetail> getStockInDetails() {
    return stockInDetails;
  }

  public void setStockInDetails(List<StockInDetail> stockInDetails) {
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
