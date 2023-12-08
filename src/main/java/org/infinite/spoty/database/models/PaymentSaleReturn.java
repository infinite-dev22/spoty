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

@DatabaseTable(tableName = "payments_sale_returns")
public class PaymentSaleReturn implements Serializable {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true, columnName = "user_id", foreignAutoRefresh = true)
    private User user;

    @DatabaseField(canBeNull = false)
    private Date date;

    @DatabaseField(columnName = "reference_number")
    private String ref;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "sale_return_id", foreignAutoRefresh = true)
    private SaleReturnMaster saleReturn;

    @DatabaseField(canBeNull = false)
    private String paymentMethod;

    @DatabaseField(canBeNull = false)
    private double amount;

    @DatabaseField(canBeNull = false)
    private double change;

    @DatabaseField
    private String notes;

    @DatabaseField(columnName = "created_at")
    private Date createdAt;

    @DatabaseField(columnName = "created_by")
    private String createdBy;

    @DatabaseField(columnName = "updated_at")
    private Date updatedAt;

    @DatabaseField(columnName = "updated_by")
    private String updatedBy;

    public PaymentSaleReturn(
            User user,
            Date date,
            String ref,
            SaleReturnMaster saleReturn,
            String paymentMethod,
            double amount,
            double change,
            String notes) {
        this.user = user;
        this.date = date;
        this.ref = ref;
        this.saleReturn = saleReturn;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.change = change;
        this.notes = notes;
    }

    public PaymentSaleReturn() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public SaleReturnMaster getSaleReturn() {
        return saleReturn;
    }

    public void setSaleReturn(SaleReturnMaster saleReturn) {
        this.saleReturn = saleReturn;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
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
