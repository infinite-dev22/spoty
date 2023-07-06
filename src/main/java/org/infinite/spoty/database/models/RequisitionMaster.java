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

import static org.infinite.spoty.GlobalActions.fineDate;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import jakarta.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

@DatabaseTable(tableName = "requisition_master")
public class RequisitionMaster implements Serializable {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(columnName = "reference_number")
  private String ref;

  @DatabaseField(canBeNull = false)
  private Date date;

  @DatabaseField(foreign = true, columnName = "user_id")
  private User user;

  @DatabaseField(foreign = true, columnName = "supplier_id", canBeNull = false)
  private Supplier supplier;

  @DatabaseField(foreign = true, columnName = "branch_id", canBeNull = false)
  private Branch branch;

  @ForeignCollectionField
  private Collection<RequisitionDetail> requisitionDetails = new LinkedList<>();

  @DatabaseField(columnName = "ship_via")
  private String shipVia;

  @DatabaseField(columnName = "ship_method")
  private String shipMethod;

  @DatabaseField(columnName = "shipping_terms")
  private String shippingTerms;

  @DatabaseField(columnName = "delivery_date")
  private Date deliveryDate;

  @DatabaseField private String notes;

  @DatabaseField(canBeNull = false)
  private String status;

  @DatabaseField(canBeNull = false, columnName = "total_cost")
  private double totalCost;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public RequisitionMaster() {}

  public RequisitionMaster(
      Date date,
      Supplier supplier,
      Branch branch,
      String shipVia,
      String shipMethod,
      String shippingTerms,
      Date deliveryDate,
      String notes,
      String status,
      double totalCost) {
    this.date = date;
    this.supplier = supplier;
    this.branch = branch;
    this.shipVia = shipVia;
    this.shipMethod = shipMethod;
    this.shippingTerms = shippingTerms;
    this.deliveryDate = deliveryDate;
    this.notes = notes;
    this.status = status;
    this.totalCost = totalCost;
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

  public Collection<RequisitionDetail> getRequisitionDetails() {
    return requisitionDetails;
  }

  public void setRequisitionDetails(Collection<RequisitionDetail> requisitionDetails) {
    this.requisitionDetails = requisitionDetails;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
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

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public String getSupplierName() {
    return (supplier != null) ? supplier.getName() : null;
  }

  public String getShipVia() {
    return shipVia;
  }

  public void setShipVia(String shipVia) {
    this.shipVia = shipVia;
  }

  public String getShipMethod() {
    return shipMethod;
  }

  public void setShipMethod(String shipMethod) {
    this.shipMethod = shipMethod;
  }

  public String getShippingTerms() {
    return shippingTerms;
  }

  public void setShippingTerms(String shippingTerms) {
    this.shippingTerms = shippingTerms;
  }

  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public String getDeliveryLocaleDate() {
    return DateFormat.getDateInstance().format(deliveryDate);
  }

  public String getDeliveryDateString() {
    return fineDate(deliveryDate.toString()).toString();
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public double getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(double totalCost) {
    this.totalCost = totalCost;
  }

  public String getLocaleDate() {
    return DateFormat.getDateInstance().format(date);
  }
}
