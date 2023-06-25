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

// TODO: Remove uUser Property.

@Entity
public class Expense implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private Date date;
  private String ref;
  private String name;
  @ManyToOne private User user_detail;
  @ManyToOne private ExpenseCategory expenseCategory;
  @ManyToOne private Branch branch;
  private String details;
  private double amount;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "updated_by")
  private String updatedBy;

  public Expense(
      Date date,
      String name,
      ExpenseCategory expenseCategory,
      Branch branch,
      String details,
      double amount) {
    this.date = date;
    this.name = name;
    this.expenseCategory = expenseCategory;
    this.branch = branch;
    this.details = details;
    this.amount = amount;
  }

  public Expense(Date date, ExpenseCategory expenseCategory, double amount) {
    this.date = date;
    this.expenseCategory = expenseCategory;
    this.amount = amount;
  }

  public Expense() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getUser_detail() {
    return user_detail;
  }

  public void setUser_detail(User user_detail) {
    this.user_detail = user_detail;
  }

  public ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }

  public void setExpenseCategory(ExpenseCategory expenseCategory) {
    this.expenseCategory = expenseCategory;
  }

  public String getExpenseCategoryName() {
    return expenseCategory.getName();
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

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
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
