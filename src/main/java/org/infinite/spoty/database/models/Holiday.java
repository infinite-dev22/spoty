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

@DatabaseTable(tableName = "holiday")
public class Holiday implements Serializable {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(canBeNull = false)
  private String title;
  @DatabaseField(foreign = true, canBeNull = false, columnName = "company_id", foreignAutoRefresh = true)
  private Company company;

  @DatabaseField(columnName = "start_date")
  private Date startDate;

  @DatabaseField(columnName = "end_date")
  private Date endDate;
  @DatabaseField
  private String description;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public Holiday(
      String title,
      Company company,
      Date startDate,
      Date endDate,
      String description,
      Date createdAt,
      String createdBy,
      Date updatedAt,
      String updatedBy) {
    this.title = title;
    this.company = company;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
  }

  public Holiday(String title, Company company, Date startDate, Date endDate, String description) {
    this.title = title;
    this.company = company;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
  }

  public Holiday(String title, Company company, Date startDate, Date endDate) {
    this.title = title;
    this.company = company;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Holiday() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
