package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class ProductMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String code;
    @Column(name = "barcode_type")
    private String barcodeType;
    private String name;
    @ManyToOne
    private ProductCategory category;
    @ManyToOne
    private Brand brand;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductDetail> productDetails;
    private byte[] image;
    private String note;
    @Column(nullable = false, name = "not_sale")
    private boolean notForSale;
    @Column(nullable = false, name = "is_active")
    private boolean isActive;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public ProductMaster(String barcodeType,
                         String name,
                         double price,
                         ProductCategory category,
                         Brand brand,
                         String note,
                         boolean notForSale,
                         boolean isActive) {
        this.barcodeType = barcodeType;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.note = note;
        this.notForSale = notForSale;
        this.isActive = isActive;
    }

    public ProductMaster() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getCategoryName() {
        return category.getName();
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public String getBrandName() {
        return brand.getName();
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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

    public List<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public boolean isNotForSale() {
        return notForSale;
    }

    public void setNotForSale(boolean notForSale) {
        this.notForSale = notForSale;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
