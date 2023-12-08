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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "users")
public class User implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "first_name", canBeNull = false)
    private String firstName;

    @DatabaseField(columnName = "last_name", canBeNull = false)
    private String lastName;

    @DatabaseField(columnName = "username", canBeNull = false, unique = true)
    private String userName;

    @DatabaseField
    private String email;
    @DatabaseField
    private String password;
    @DatabaseField
    private String phone;

    @DatabaseField(foreign = true, columnName = "role_id", canBeNull = false, foreignAutoRefresh = true)
    private Role role;

    @DatabaseField(foreign = true, columnName = "branch_id", canBeNull = false, foreignAutoRefresh = true)
    private Branch branch;

    @DatabaseField(canBeNull = false)
    private boolean active;

    @DatabaseField(columnName = "access_all_branches", canBeNull = false)
    private boolean accessAllBranches;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] avatar;

    @DatabaseField(columnName = "created_at")
    private Date createdAt = null;

    @DatabaseField(columnName = "created_by")
    private String createdBy = null;

    @DatabaseField(columnName = "updated_at")
    private Date updatedAt = null;

    @DatabaseField(columnName = "updated_by")
    private String updatedBy = null;

    public User() {
    }

    public User(
            String firstName,
            String lastName,
            String userName,
            Role role,
            boolean active,
            boolean accessAllBranches) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.role = role;
        this.active = active;
        this.accessAllBranches = accessAllBranches;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean canAccessAllBranches() {
        return accessAllBranches;
    }

    public void setAccessAllBranches(boolean accessAllBranches) {
        this.accessAllBranches = accessAllBranches;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
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
