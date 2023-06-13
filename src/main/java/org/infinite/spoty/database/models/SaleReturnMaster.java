package org.infinite.spoty.database.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class SaleReturnMaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user_detail;
    private Date date;
    private String ref;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Branch branch;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "saleReturn")
    private List<SaleReturnDetail> saleReturnDetails;
    private double taxRate;
    private double netTax;
    private double discount;
    private double total;
    private double paid;
    private String paymentStatus;
    private String status;
    private String notes;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public SaleReturnMaster() {
    }

    public SaleReturnMaster(Date date,
                            Customer customer,
                            Branch branch,
                            double taxRate,
                            double netTax,
                            double discount,
                            double total,
                            double paid,
                            String paymentStatus,
                            String status,
                            String notes) {
        this.date = date;
        this.customer = customer;
        this.branch = branch;
        this.taxRate = taxRate;
        this.netTax = netTax;
        this.discount = discount;
        this.total = total;
        this.paid = paid;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.notes = notes;
    }

    public User getUser() {
        return user_detail;
    }

    public void setUser(User user_detail) {
        this.user_detail = user_detail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerName() {
        return (customer != null) ? customer.getName() : null;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<SaleReturnDetail> getSaleReturnDetails() {
        return saleReturnDetails;
    }

    public void setSaleReturnDetails(List<SaleReturnDetail> saleReturnDetails) {
        this.saleReturnDetails = saleReturnDetails;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
