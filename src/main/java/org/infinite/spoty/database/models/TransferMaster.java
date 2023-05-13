package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// TODO: Make items a TransferDetail List.

@Entity
public class TransferMaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private User user;
    private String ref;
    private Date date;
    @ManyToOne
    private Branch fromBranch;
    @ManyToOne
    private Branch toBranch;
    private String items;
    @Column(name = "tax_rate")
    private double taxRate;
    private double tax;
    private double discount;
    private String shipping;
    private double total;
    private int status;
    private String notes;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    public TransferMaster() {
    }
    public TransferMaster(User user, String ref, Date date, Branch fromBranch, Branch toBranch, String items, double taxRate, double tax, double discount, String shipping, double total, int status, String notes) {
        this.user = user;
        this.ref = ref;
        this.date = date;
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
        this.items = items;
        this.taxRate = taxRate;
        this.tax = tax;
        this.discount = discount;
        this.shipping = shipping;
        this.total = total;
        this.status = status;
        this.notes = notes;
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

    public Branch getFromBranch() {
        return fromBranch;
    }

    public void setFromBranch(Branch fromBranch) {
        this.fromBranch = fromBranch;
    }

    public Branch getToBranch() {
        return toBranch;
    }

    public void setToBranch(Branch toBranch) {
        this.toBranch = toBranch;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
