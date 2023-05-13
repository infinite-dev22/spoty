package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

// TODO: Add Bi-Directional referencing between ProductMaster and ProductDetail.
// TODO: Remove cost, taxes, stock alerts, serials, price and the units from ProductMaster, add them to ProductDetail.

@Entity
public class ProductMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String code;
    @Column(name = "barcode_type")
    private String barcodeType;
    private String name;
    private double cost;
    private double price;
    @ManyToOne
    private ProductCategory category;
    @ManyToOne
    private Brand brand;
    @ManyToOne
    private UnitOfMeasure unit;
    @ManyToOne
    private UnitOfMeasure saleUnit;
    @ManyToOne
    private UnitOfMeasure purchaseUnit;
    private double netTax;
    @Column(name = "tax_type")
    private String taxType;
    private Blob image;
    private String note;
    @Column(name = "stock_alert")
    private long stockAlert;
    @Column(nullable = false, name = "has_serial")
    private boolean hasSerial;
    @Column(nullable = false, name = "not_sale")
    private boolean notForSale;
    @Column(nullable = false, name = "is_variant")
    private boolean isVariant;
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
    public ProductMaster(String code, String barcodeType, String name, double cost, double price, ProductCategory category, Brand brand, UnitOfMeasure unit, UnitOfMeasure saleUnit, UnitOfMeasure purchaseUnit, double netTax, String taxType, Blob image, String note, long stockAlert, boolean hasSerial, boolean notForSale, boolean isVariant, boolean isActive) {
        this.code = code;
        this.barcodeType = barcodeType;
        this.name = name;
        this.cost = cost;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.unit = unit;
        this.saleUnit = saleUnit;
        this.purchaseUnit = purchaseUnit;
        this.netTax = netTax;
        this.taxType = taxType;
        this.image = image;
        this.note = note;
        this.stockAlert = stockAlert;
        this.hasSerial = hasSerial;
        this.notForSale = notForSale;
        this.isVariant = isVariant;
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

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public UnitOfMeasure getUnit() {
        return unit;
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

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getStockAlert() {
        return stockAlert;
    }

    public void setStockAlert(long stockAlert) {
        this.stockAlert = stockAlert;
    }

    public boolean isHasSerial() {
        return hasSerial;
    }

    public void setHasSerial(boolean hasSerial) {
        this.hasSerial = hasSerial;
    }

    public boolean isNotForSale() {
        return notForSale;
    }

    public void setNotForSale(boolean notForSale) {
        this.notForSale = notForSale;
    }

    public boolean isVariant() {
        return isVariant;
    }

    public void setVariant(boolean variant) {
        isVariant = variant;
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
