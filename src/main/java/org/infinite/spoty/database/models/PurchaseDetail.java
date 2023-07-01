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

// TODO: Remove total and Quantity and add them to PurchaseMaster.
// TODO: In place of total create purchasePrice.
@Entity
public class PurchaseDetail implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private double cost;

  @ManyToOne
  @JoinColumn(name = "purchaseMaster_id", nullable = false)
  private PurchaseMaster purchase;

  private double netTax;
  private String taxType;
  private double discount;
  private String discountType;

  @ManyToOne(optional = false)
  private ProductDetail product;

  private String serialNumber;
  private double total;
  private int quantity;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "updated_by")
  private String updatedBy;

  public PurchaseDetail() {}

  public PurchaseDetail(PurchaseMaster purchase, double cost, ProductDetail product, int quantity) {
    this.cost = cost;
    this.purchase = purchase;
    this.product = product;
    this.quantity = quantity;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
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

  public ProductDetail getProduct() {
    return product;
  }

  public void setProduct(ProductDetail product) {
    this.product = product;
  }

  public String getProductName() {
    return (product != null)
        ? product.getProduct().getBrand().getName()
            + " "
            + product.getProduct().getName()
            + " "
            + product.getName()
            + " "
            + product.getUnit().getName()
        : null;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
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
