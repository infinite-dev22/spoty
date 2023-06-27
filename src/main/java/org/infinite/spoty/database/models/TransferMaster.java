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
public class TransferMaster implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne private User user_detail;
  private String ref;

  @Column(nullable = false)
  private Date date;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "from_branch_id")
  private Branch fromBranch;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "to_branch_id")
  private Branch toBranch;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "transfer")
  private List<TransferDetail> transferDetails;

  private String shipping;

  @Column(nullable = false)
  private double total;

  @Column(nullable = false)
  private String status;

  @ManyToOne
  @JoinColumn(name = "approved_by_id")
  private User approvedBy;

  @ManyToOne
  @JoinColumn(name = "received_by_id")
  private User receivedBy;

  private Date approvalDate;
  private Date receiveDate;
  private String notes;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "updated_by")
  private String updatedBy;

  public TransferMaster() {}

  public TransferMaster(
      Date date, Branch fromBranch, Branch toBranch, double total, String status, String notes) {
    this.date = date;
    this.fromBranch = fromBranch;
    this.toBranch = toBranch;
    this.total = total;
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

  public Branch getFromBranch() {
    return fromBranch;
  }

  public void setFromBranch(Branch fromBranch) {
    this.fromBranch = fromBranch;
  }

  public String getFromBranchName() {
    return (fromBranch != null) ? fromBranch.getName() : null;
  }

  public Branch getToBranch() {
    return toBranch;
  }

  public void setToBranch(Branch toBranch) {
    this.toBranch = toBranch;
  }

  public String getToBranchName() {
    return (toBranch != null) ? toBranch.getName() : null;
  }

  public String getShipping() {
    return shipping;
  }

  public void setShipping(String shipping) {
    this.shipping = shipping;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
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

  public List<TransferDetail> getTransferDetails() {
    return transferDetails;
  }

  public void setTransferDetails(List<TransferDetail> transferDetails) {
    this.transferDetails = transferDetails;
  }

  public User getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(User approvedBy) {
    this.approvedBy = approvedBy;
  }

  public User getReceivedBy() {
    return receivedBy;
  }

  public void setReceivedBy(User receivedBy) {
    this.receivedBy = receivedBy;
  }

  public Date getApprovalDate() {
    return approvalDate;
  }

  public void setApprovalDate(Date approvalDate) {
    this.approvalDate = approvalDate;
  }

  public Date getReceiveDate() {
    return receiveDate;
  }

  public void setReceiveDate(Date receiveDate) {
    this.receiveDate = receiveDate;
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
