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
import jakarta.persistence.*;
import java.util.Date;

@DatabaseTable(tableName = "leave_type")
public class LeaveType {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(canBeNull = false)
  private String name;

  @DatabaseField(columnName = "created_at")
  private Date createdAt = null;

  @DatabaseField(columnName = "created_by")
  private String created_by = null;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt = null;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy = null;

  public LeaveType(
      String name, Date createdAt, String created_by, Date updatedAt, String updatedBy) {
    this.name = name;
    this.createdAt = createdAt;
    this.created_by = created_by;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
  }

  public LeaveType() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getCreated_by() {
    return created_by;
  }

  public void setCreated_by(String created_by) {
    this.created_by = created_by;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getUpdated_by() {
    return updatedBy;
  }

  public void setUpdated_by(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
