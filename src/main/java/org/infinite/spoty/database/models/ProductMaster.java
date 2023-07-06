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
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@DatabaseTable(tableName = "product_masters")
public class ProductMaster implements Serializable {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField private String code;

  @DatabaseField(columnName = "barcode_type")
  private String barcodeType;

  @DatabaseField(canBeNull = false)
  private String name;

  @DatabaseField(foreign = true, canBeNull = false)
  private ProductCategory category;

  @DatabaseField(foreign = true, canBeNull = false)
  private Brand brand;

  @ForeignCollectionField
  private Collection<ProductDetail> productDetails;

  @DatabaseField(dataType = DataType.BYTE_ARRAY ) private byte[] image;
  @DatabaseField private String note;

  @DatabaseField(canBeNull = false, columnName = "not_sale")
  private boolean notForSale;

  @DatabaseField(canBeNull = false, columnName = "is_active")
  private boolean hasVariants;

  @DatabaseField(columnName = "created_at")
  private Date createdAt;

  @DatabaseField(columnName = "created_by")
  private String createdBy;

  @DatabaseField(columnName = "updated_at")
  private Date updatedAt;

  @DatabaseField(columnName = "updated_by")
  private String updatedBy;

  public ProductMaster(
      String barcodeType,
      String name,
      double price,
      ProductCategory category,
      Brand brand,
      String note,
      boolean notForSale,
      boolean hasVariants) {
    this.barcodeType = barcodeType;
    this.name = name;
    this.category = category;
    this.brand = brand;
    this.note = note;
    this.notForSale = notForSale;
    this.hasVariants = hasVariants;
  }

  public ProductMaster() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getBarcodeType() {
    return barcodeType;
  }

  public void setBarcodeType(String barcodeType) {
    this.barcodeType = barcodeType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductCategory getCategory() {
    return category;
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  public String getCategoryName() {
    if (category != null) return category.getName();
    else return null;
  }

  public Brand getBrand() {
    return brand;
  }

  public void setBrand(Brand brand) {
    this.brand = brand;
  }

  public String getBrandName() {
    if (brand != null) return brand.getName();
    else return null;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Collection<ProductDetail> getProductDetails() {
    return productDetails;
  }

  public void setProductDetails(Collection<ProductDetail> productDetails) {
    this.productDetails = productDetails;
  }

  public boolean isNotForSale() {
    return notForSale;
  }

  public void setNotForSale(boolean notForSale) {
    this.notForSale = notForSale;
  }

  public boolean hasVariant() {
    return hasVariants;
  }

  public void canHaveVariants(boolean hasVariants) {
    this.hasVariants = hasVariants;
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
