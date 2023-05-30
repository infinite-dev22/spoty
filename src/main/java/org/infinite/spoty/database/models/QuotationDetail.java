package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class QuotationDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double price;
    @ManyToOne
    private UnitOfMeasure saleUnit;
    @ManyToOne
    private ProductDetail product;
    @ManyToOne
    private QuotationMaster quotation;
    private double netTax;
    private String taxType;
    private double discount;
    private String discountType;
    private double total;
    private int quantity;
    private String serialNumber;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    public QuotationDetail(ProductDetail product,
                           double netTax,
                           double discount,
                           int quantity) {
        this.product = product;
        this.netTax = netTax;
        this.discount = discount;
        this.quantity = quantity;
    }

    public QuotationDetail() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UnitOfMeasure getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(UnitOfMeasure saleUnit) {
        this.saleUnit = saleUnit;
    }

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
    }

    public QuotationMaster getQuotation() {
        return quotation;
    }

    public void setQuotation(QuotationMaster quotation) {
        this.quotation = quotation;
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
