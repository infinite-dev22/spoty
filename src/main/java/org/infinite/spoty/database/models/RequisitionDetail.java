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
public class RequisitionDetail implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "product_detail_id")
  private ProductDetail product;

  @ManyToOne(optional = false)
  @JoinColumn(name = "requisition_id", nullable = false)
  private RequisitionMaster requisition;

  @Column(nullable = false)
  private int quantity;

  private String description;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "updated_by")
  private String updatedBy;

  public RequisitionDetail() {}

  public RequisitionDetail(
      ProductDetail product,
      RequisitionMaster
          requisition, // TODO: Remove this line as it ain't good being here. can't get
      // RequisitionMaster here. use setter.
      int quantity,
      String description) {
    this.product = product;
    this.requisition = requisition;
    this.quantity = quantity;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ProductDetail getProduct() {
    return product;
  }

  public void setProduct(ProductDetail product) {
    this.product = product;
  }

  public String getProductDetailName() {
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

  public RequisitionMaster getRequisition() {
    return requisition;
  }

  public void setRequisition(RequisitionMaster requisition) {
    this.requisition = requisition;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
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
