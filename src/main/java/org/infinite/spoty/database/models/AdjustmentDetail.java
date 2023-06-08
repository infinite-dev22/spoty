package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class AdjustmentDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private ProductDetail productDetail;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adjustment_id", nullable = false)
    private AdjustmentMaster adjustment;
    private int quantity;
    private String adjustmentType;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public AdjustmentDetail() {
    }

    public AdjustmentDetail(ProductDetail productDetail,
                            int quantity,
                            String adjustmentType) {
        this.productDetail = productDetail;
        this.quantity = quantity;
        this.adjustmentType = adjustmentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public String getProductDetailName() {
        if (productDetail != null)
            return productDetail.getProduct().getName() + " " + productDetail.getName();
        else
            return null;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public AdjustmentMaster getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(AdjustmentMaster adjustment) {
        this.adjustment = adjustment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
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
