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

@DatabaseTable(tableName = "purchase_masters")
public class PurchaseMaster implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, columnName = "user_id", foreignAutoRefresh = true)
    private User user;

    @DatabaseField
    private String ref;

    @DatabaseField(canBeNull = false)
    private Date date;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "supplier_id", foreignAutoRefresh = true)
    private Supplier supplier;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "branch_id", foreignAutoRefresh = true)
    private Branch branch;

    @ForeignCollectionField
    private Collection<PurchaseDetail> purchaseDetails;

    @DatabaseField(columnName = "tax_rate")
    private double taxRate;

    @DatabaseField(columnName = "net_tax")
    private double netTax;

    @DatabaseField
    private double discount;
    @DatabaseField
    private String shipping;
    @DatabaseField
    private double paid;
    @DatabaseField
    private double total;
    @DatabaseField
    private double due;
    @DatabaseField
    private String status;

    @DatabaseField(columnName = "payment_status")
    private String paymentStatus;

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

    public PurchaseMaster() {
    }

    public PurchaseMaster(Supplier supplier, Branch branch, String status, String notes, Date date) {
        this.date = date;
        this.supplier = supplier;
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
        return user;
    }

    public void setUser(User user_detail) {
        this.user = user_detail;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getSupplierName() {
        if (supplier != null) return supplier.getName();
        else return null;
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

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public Collection<PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails;
    }

    public void setPurchaseDetails(Collection<PurchaseDetail> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
    }

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        this.due = due;
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
