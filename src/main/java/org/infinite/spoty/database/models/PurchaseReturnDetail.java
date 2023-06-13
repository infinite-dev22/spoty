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

@Entity
public class PurchaseReturnDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double cost;
    @ManyToOne
    private UnitOfMeasure purchaseUnit;
    @ManyToOne
    @JoinColumn(name = "purchaseReturnMaster_id")
    private PurchaseReturnMaster purchaseReturn;
    @OneToOne
    private ProductDetail product;
    private double netTax;
    private String taxType;
    private double discount;
    private String discountType;
    private int quantity;
    private double total;
    private String serialNumber;
    @Column(name = "created_at")
    private Date createdAt;

    public PurchaseReturnDetail(double cost, UnitOfMeasure purchaseUnit, PurchaseReturnMaster purchaseReturn, ProductDetail product, double netTax, String taxType, double discount, String discountType, int quantity, double total, String serialNumber) {
        this.cost = cost;
        this.purchaseUnit = purchaseUnit;
        this.purchaseReturn = purchaseReturn;
        this.product = product;
        this.netTax = netTax;
        this.taxType = taxType;
        this.discount = discount;
        this.discountType = discountType;
        this.quantity = quantity;
        this.total = total;
        this.serialNumber = serialNumber;
    }

    public PurchaseReturnDetail() {
    }

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

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

    public UnitOfMeasure getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(UnitOfMeasure purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public PurchaseReturnMaster getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(PurchaseReturnMaster purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
