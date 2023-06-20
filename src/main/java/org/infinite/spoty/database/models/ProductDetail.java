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

@Entity
public class ProductDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductMaster product;
    @ManyToMany(targetEntity = Branch.class)
    private List<Branch> branch;
    @ManyToOne
    private UnitOfMeasure unit;
    @ManyToOne
    private UnitOfMeasure saleUnit;
    @ManyToOne
    private UnitOfMeasure purchaseUnit;
    private String name;
    private int quantity;
    private double cost;
    private double price;
    private double netTax;
    @Column(name = "tax_type")
    private String taxType;
    @Column(name = "stock_alert")
    private int stockAlert;
    private String serialNumber;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public ProductDetail() {}

    public ProductDetail(UnitOfMeasure unit,
                         UnitOfMeasure saleUnit,
                         UnitOfMeasure purchaseUnit,
                         String name,
                         int quantity,
                         double cost,
                         double price,
                         double netTax,
                         String taxType,
                         int stockAlert,
                         String serialNumber) {
        this.unit = unit;
        this.saleUnit = saleUnit;
        this.purchaseUnit = purchaseUnit;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.price = price;
        this.netTax = netTax;
        this.taxType = taxType;
        this.stockAlert = stockAlert;
        this.serialNumber = serialNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductMaster getProduct() {
        return product;
    }

    public void setProduct(ProductMaster product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Branch> getBranch() {
        return branch;
    }

    public void setBranch(List<Branch> branch) {
        this.branch = branch;
    }

    public UnitOfMeasure getUnit() {
        return unit;
    }

    public String getUnitName() {
        return unit != null ? unit.getName() : null;
    }

    public void setUnit(UnitOfMeasure unit) {
        this.unit = unit;
    }

    public UnitOfMeasure getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(UnitOfMeasure saleUnit) {
        this.saleUnit = saleUnit;
    }

    public UnitOfMeasure getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(UnitOfMeasure purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
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

    public int getStockAlert() {
        return stockAlert;
    }

    public void setStockAlert(int stockAlert) {
        this.stockAlert = stockAlert;
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
