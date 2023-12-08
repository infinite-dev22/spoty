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
import java.util.Objects;

// TODO: Remove totalPrice and Quantity and add them to PurchaseMaster.
// TODO: In place of totalPrice create purchasePrice.
@DatabaseTable(tableName = "purchase_details")
public class PurchaseDetail implements Serializable {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private double cost;

    @DatabaseField(
            foreign = true,
            columnName = "purchase_master_id",
            canBeNull = false,
            foreignAutoCreate = true,
            foreignAutoRefresh = true,
            columnDefinition =
                    "INTEGER CONSTRAINT FK_NAME REFERENCES purchase_masters(id) ON DELETE CASCADE")
    private PurchaseMaster purchase;

    @DatabaseField(columnName = "net_tax")
    private double netTax;

    @DatabaseField(columnName = "tax_type")
    private String taxType;

    @DatabaseField
    private double discount;

    @DatabaseField(columnName = "discount_type")
    private String discountType;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private Product product;

    @DatabaseField(columnName = "serial_number")
    private String serialNumber;

    @DatabaseField
    private double totalPrice;
    @DatabaseField
    private double price;
    @DatabaseField
    private long quantity;

    @DatabaseField(columnName = "created_at")
    private Date createdAt;

    @DatabaseField(columnName = "created_by")
    private String createdBy;

    @DatabaseField(columnName = "updated_at")
    private Date updatedAt;

    @DatabaseField(columnName = "updated_by")
    private String updatedBy;

    public PurchaseDetail() {
    }

    public PurchaseDetail(
            PurchaseMaster purchase, double cost, Product product, long quantity) {
        this.cost = cost;
        this.purchase = purchase;
        this.product = product;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PurchaseMaster getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseMaster purchase) {
        this.purchase = purchase;
    }

    public double getNetTax() {
        return netTax;
    }

    public void setNetTax(double netTax) {
        this.netTax = netTax;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return (product != null)
                ? product.getBrand().getName()
                + " "
                + product.getName()
                + " "
                + (Objects.equals(product.getUnit(), null) ? "" : product.getUnit().getName())
                : null;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
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
