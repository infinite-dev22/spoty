package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class TransferDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private TransferMaster transfer;
    @ManyToOne
    private ProductDetail product;
    private double cost;
    @ManyToOne
    private UnitOfMeasure purchaseUnit;
    private double netTax;
    private String taxType;
    private double discount;
    private String discountType;
    private int quantity;
    private double total;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    public TransferDetail(TransferMaster transfer, ProductDetail product, double cost, UnitOfMeasure purchaseUnit, double netTax, String taxType, double discount, String discountType, int quantity, double total) {
        this.transfer = transfer;
        this.product = product;
        this.cost = cost;
        this.purchaseUnit = purchaseUnit;
        this.netTax = netTax;
        this.taxType = taxType;
        this.discount = discount;
        this.discountType = discountType;
        this.quantity = quantity;
        this.total = total;
    }

    public TransferDetail() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransferMaster getTransfer() {
        return transfer;
    }

    public void setTransfer(TransferMaster transfer) {
        this.transfer = transfer;
    }

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
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
