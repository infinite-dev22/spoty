package org.infinite.spoty.database.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class PurchaseReturnMaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;
    private String ref;
    private Date date;
    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private Branch branch;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseReturn")
    private List<PurchaseReturnDetail> purchaseReturnDetails;
    private double taxRate;
    private double netTax;
    private double discount;
    private String shipping;
    private double paid;
    private double total;
    private String status;
    private String paymentStatus;
    private String notes;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public PurchaseReturnMaster(User user,
                                String ref,
                                Date date,
                                Supplier supplier,
                                Branch branch,
                                double taxRate,
                                double netTax,
                                double discount,
                                String shipping,
                                double paid,
                                double total,
                                String status,
                                String paymentStatus,
                                String notes) {
        this.user = user;
        this.ref = ref;
        this.date = date;
        this.supplier = supplier;
        this.branch = branch;
        this.taxRate = taxRate;
        this.netTax = netTax;
        this.discount = discount;
        this.shipping = shipping;
        this.paid = paid;
        this.total = total;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.notes = notes;
    }

    public PurchaseReturnMaster() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getSupplierName() {
        return (supplier != null) ? supplier.getName() : null;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getBranchName() {
        return (branch != null) ? branch.getName() : null;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public List<PurchaseReturnDetail> getPurchaseReturnDetails() {
        return purchaseReturnDetails;
    }

    public void setPurchaseReturnDetails(List<PurchaseReturnDetail> purchaseReturnDetails) {
        this.purchaseReturnDetails = purchaseReturnDetails;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getNetTax() {
        return netTax;
    }

    public void setNetTax(double netTax) {
        this.netTax = netTax;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

