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
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class SaleReturnMaster implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne private User user_detail;

  @Column(nullable = false)
  private Date date;

  private String ref;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "customer_id")
  private Customer customer;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "branch_id")
  private Branch branch;

  @OneToMany(mappedBy = "saleReturn", fetch = FetchType.LAZY)
  @Cascade({CascadeType.ALL})
  private List<SaleReturnDetail> saleReturnDetails;

  private double taxRate;
  private double netTax;
  private double discount;

  @Column(nullable = false)
  private double total;

  @Column(nullable = false)
  private double paid;

  @Column(nullable = false)
  private String paymentStatus;

  @Column(nullable = false)
  private String status;

  private String notes;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "updated_by")
  private String updatedBy;

  public SaleReturnMaster() {}

  public SaleReturnMaster(
      Date date,
      Customer customer,
      Branch branch,
      double taxRate,
      double netTax,
      double discount,
      double total,
      double paid,
      String paymentStatus,
      String status,
      String notes) {
    this.date = date;
    this.customer = customer;
    this.branch = branch;
    this.taxRate = taxRate;
    this.netTax = netTax;
    this.discount = discount;
    this.total = total;
    this.paid = paid;
    this.paymentStatus = paymentStatus;
    this.status = status;
    this.notes = notes;
  }

  public User getUser() {
    return user_detail;
  }

  public void setUser(User user_detail) {
    this.user_detail = user_detail;
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

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getCustomerName() {
    return (customer != null) ? customer.getName() : null;
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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<SaleReturnDetail> getSaleReturnDetails() {
    return saleReturnDetails;
  }

  public void setSaleReturnDetails(List<SaleReturnDetail> saleReturnDetails) {
    this.saleReturnDetails = saleReturnDetails;
  }

  public double getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(double taxRate) {
    this.taxRate = taxRate;
  }

  public double getNetTax() {
    return netTax;
  }

  public void setNetTax(double netTax) {
    this.netTax = netTax;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public double getPaid() {
    return paid;
  }

  public void setPaid(double paid) {
    this.paid = paid;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
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
